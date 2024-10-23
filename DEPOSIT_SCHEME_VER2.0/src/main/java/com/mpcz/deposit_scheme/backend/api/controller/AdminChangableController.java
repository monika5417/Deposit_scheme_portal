package com.mpcz.deposit_scheme.backend.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.domain.AppUpdationRemark;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationDocument;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.Consumer;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.IndividualOrGroup;
import com.mpcz.deposit_scheme.backend.api.domain.SchemeType;
import com.mpcz.deposit_scheme.backend.api.domain.Upload;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.DocumentTypeException;
import com.mpcz.deposit_scheme.backend.api.exception.FormValidationException;
import com.mpcz.deposit_scheme.backend.api.exception.InvalidAuthenticationException;
import com.mpcz.deposit_scheme.backend.api.exception.UserException;
import com.mpcz.deposit_scheme.backend.api.repository.AppUpdationRemarkRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ApplicationDocumentRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ApplicationStatusRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerRepository;
import com.mpcz.deposit_scheme.backend.api.repository.IndividualOrGroupRepository;
import com.mpcz.deposit_scheme.backend.api.repository.NatureOfWorkRepository;
import com.mpcz.deposit_scheme.backend.api.repository.SchemeTypeRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationDocumentService;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerApplicationDetailService;
import com.mpcz.deposit_scheme.backend.api.services.UploadService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/adminChangable/user")
public class AdminChangableController {

	@Autowired
	private ConsumerApplictionDetailRepository consumerApplicationDetailRepository;

	@Autowired
	NatureOfWorkRepository natureOfWorkRepository;

	@Autowired
	SchemeTypeRepository schemeTypeRepository;

	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private IndividualOrGroupRepository individualOrGroupRepository;

	@Autowired
	private ApplicationStatusRepository applicationStatusRepository;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private ApplicationDocumentRepository applicationDocumentRepository;

	@Autowired
	private ApplicationDocumentService applicationDocumentService;

	@Autowired
	private AppUpdationRemarkRepository appUpdationRemarkRepository;
	
	@Autowired
	private ConsumerApplicationDetailService consumerApplicationDetailService;

	@GetMapping("/getConsumerByLoginId/{loginId}")
	public ResponseEntity<Response<?>> getConsumerByLoginId(@PathVariable String loginId,
			HttpServletResponse httpServletResponse)
			throws FormValidationException, InvalidAuthenticationException, UserException {

		Response<Consumer> response = new Response<>();

		Optional<Consumer> findByConusmerMobileNo = consumerRepository.findByConsumerLoginId(loginId);

		if (!findByConusmerMobileNo.isPresent()) {
			response.setCode("100");
			response.setMessage("Consumer Not Found With This Login Id");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);

		}

		response.setCode("200");
		response.setMessage("Consumer found");
		response.setList(Arrays.asList(findByConusmerMobileNo.get()));
		return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);

	}

	@PutMapping("/changeConsumerMobileNo/{oldLoginId}/{newMobileNo}")
	public ResponseEntity<?> changeConsumerMobileNo(@PathVariable String oldLoginId, @PathVariable String newMobileNo) {
		Response<Consumer> response = new Response<>();
		try {
			Consumer consumer = consumerRepository.findByConsumerLoginId(oldLoginId).orElse(null);
			if (consumer == null) {
				response.setCode("100");
				response.setMessage("Consuer Not Found With This Login Id");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			consumer.setConsumerLoginId(newMobileNo);
			consumer.setConsumerMobileNo(newMobileNo);

			List<ConsumerApplicationDetail> findByConsumerId = consumerApplicationDetailRepository
					.findByConsumerId(consumer.getConsumerId());
			for (ConsumerApplicationDetail consumerDetail : findByConsumerId) {
				consumerDetail.setConsumerphonNumber(newMobileNo);
				consumerApplicationDetailRepository.save(consumerDetail);
			}

			Consumer save = consumerRepository.save(consumer);
			response.setCode("200");
			response.setMessage("Consumer Mobile Number Updated Successfully");
			response.setList(Arrays.asList(save));
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode("404");
			response.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	@PutMapping("/changeConsumerNameAndAddress/{consumerApplicationNo}/{newConsumerName}/{newAddress}")
	public ResponseEntity<?> changeConsumerNameAndAddress(@PathVariable String consumerApplicationNo,
			@PathVariable String newConsumerName, @PathVariable String newAddress) {
		Response response = new Response();
		ConsumerApplicationDetail save = null;
		try {
			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplicationDetailRepository
					.findByConsumerApplicationNumber(consumerApplicationNo);
			if (findByConsumerApplicationNumber == null) {
				response.setCode("404");
				response.setMessage("Consumer Application No Not Found In Database");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			if (findByConsumerApplicationNumber.getConsumerName().equals(newConsumerName)
					&& findByConsumerApplicationNumber.getAddress().equals(newAddress)) {
				response.setCode("202");
				response.setMessage("No changes needed");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			if (!findByConsumerApplicationNumber.getConsumerName().equals(newConsumerName)) {
				findByConsumerApplicationNumber.setConsumerName(newConsumerName);
			}
			if (!findByConsumerApplicationNumber.getAddress().equals(newAddress)) {
				findByConsumerApplicationNumber.setAddress(newAddress);
			}

			save = consumerApplicationDetailRepository.save(findByConsumerApplicationNumber);
			if (save != null) {
				response.setCode("202");
				response.setMessage("Changes saved successfully");
				response.setList(Arrays.asList(save));
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			} else {
				response.setCode("500");
				response.setMessage("Failed to save changes");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	@PutMapping("/changeConsumerNameAndAddressByConsumerMobileNo/{consumerMobileNo}/{newConsumerName}/{newAddress}")
	public ResponseEntity<?> changeConsumerNameAndAddress1(@PathVariable String consumerMobileNo,
			@PathVariable String newConsumerName, @PathVariable String newAddress) {
		Response response = new Response();
		Consumer save = null;
		try {
			Consumer findByConsumerLoginId = consumerRepository.findByConsumerLoginId(consumerMobileNo).orElse(null);

			if (findByConsumerLoginId == null) {
				response.setCode("404");
				response.setMessage("Consumer Application No Not Found In Database");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			if (findByConsumerLoginId.getConsumerName().equals(newConsumerName)
					&& findByConsumerLoginId.getAddress().equals(newAddress)) {
				response.setCode("202");
				response.setMessage("No changes needed");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			if (!findByConsumerLoginId.getConsumerName().equals(newConsumerName)) {
				findByConsumerLoginId.setConsumerName(newConsumerName);

			}
			if (!findByConsumerLoginId.getAddress().equals(newAddress)) {
				findByConsumerLoginId.setAddress(newAddress);
			}

			save = consumerRepository.save(findByConsumerLoginId);

			if (save != null) {
				response.setCode("202");
				response.setMessage("Changes saved successfully");
				response.setList(Arrays.asList(save));
			} else {
				response.setCode("500");
				response.setMessage("Failed to save changes");
			}

			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	@PutMapping("/changeApplicationStatus/{consumerApplicationNo}/{applicationStatusNo}")
	public ResponseEntity<?> changeApplicationStatus(@PathVariable String consumerApplicationNo,
			@PathVariable Long applicationStatusNo) {
		Response response = new Response();
		try {

			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplicationDetailRepository
					.findByConsumerApplicationNumber(consumerApplicationNo);
			if (findByConsumerApplicationNumber == null) {
				response.setCode("404");
				response.setMessage("Consumer Application No Not Found In Database");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			if (findByConsumerApplicationNumber.getApplicationStatus().getApplicationStatusId()
					.equals(applicationStatusNo)) {
				response.setCode("202");
				response.setMessage("No changes needed");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			ApplicationStatus applicationStatus = applicationStatusRepository.findById(applicationStatusNo).get();
			findByConsumerApplicationNumber.setApplicationStatus(applicationStatus);
			ConsumerApplicationDetail save = consumerApplicationDetailRepository.save(findByConsumerApplicationNumber);
			response.setCode("202");
			response.setMessage("Changes saved successfully");
			response.setList(Arrays.asList(save));
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}

	}

	@PutMapping("/changeSchemeType/{consumerApplicationNo}/{schemeTypeId}")
	public ResponseEntity<?> changeSchemeType(@PathVariable String consumerApplicationNo,
			@PathVariable long schemeTypeId) {
		Response response = new Response();
		try {
			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplicationDetailRepository
					.findByConsumerApplicationNumber(consumerApplicationNo);
			if (findByConsumerApplicationNumber == null) {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Consumer Application not found");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			if (findByConsumerApplicationNumber.getNatureOfWorkType().getNatureOfWorkTypeId().equals(5L)
					|| findByConsumerApplicationNumber.getNatureOfWorkType().getNatureOfWorkTypeId().equals(8L)) {
				response.setCode(HttpCode.NOT_ACCEPTABLE);
				response.setMessage("Can not change the scheme type for OYT Or MKMY Application");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			SchemeType schemeType = null;
			ConsumerApplicationDetail save = null;
			try {
				schemeType = schemeTypeRepository.findById(schemeTypeId).get();

				if (findByConsumerApplicationNumber.getSchemeType().getSchemeTypeId() == schemeTypeId) {
					response.setCode("202");
					response.setMessage("No changes needed");
					return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
				}

				if (findByConsumerApplicationNumber.getSchemeType().getSchemeTypeId() != schemeTypeId) {
					findByConsumerApplicationNumber.setSchemeType(schemeType);
				}

				save = consumerApplicationDetailRepository.save(findByConsumerApplicationNumber);

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (save != null) {
				response.setCode("202");
				response.setMessage("Changes saved successfully");
				response.setList(Arrays.asList(save));
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			} else {
				response.setCode("500");
				response.setMessage("Failed to save changes");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	@PutMapping("/changeIndividualOrGroup/{consumerApplicationNo}/{individualOrGroupId}")
	public ResponseEntity<?> changeIndividualOrGroup(@PathVariable String consumerApplicationNo,
			@PathVariable Long individualOrGroupId,
			@RequestPart("docIndividualOrGroup") Optional<MultipartFile> docIndividualOrGroupOptional) {

		Response response = new Response();
		try {
			ConsumerApplicationDetail applicationDetail = consumerApplicationDetailRepository
					.findByConsumerApplicationNumber(consumerApplicationNo);
			if (applicationDetail == null) {
				response.setCode("404");
				response.setMessage("Consumer Application No Not Found In Database");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			Long natureOfWorkTypeId = applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId();
			if (!(natureOfWorkTypeId.equals(4L) || natureOfWorkTypeId.equals(8L) ||natureOfWorkTypeId.equals(2L))) {
				response.setCode(HttpCode.NOT_ACCEPTABLE);
				response.setMessage(
						"Individual And Group Is not allowed for this Nature Of Work : " + individualOrGroupId);
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
		
			IndividualOrGroup individualOrGroup = individualOrGroupRepository.findById(individualOrGroupId)
					.orElse(null);
			if (individualOrGroup == null) {
				response.setCode("404");
				response.setMessage("IndividualOrGroup ID not found");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			if (applicationDetail.getIndividualOrGroup() == null
					|| !individualOrGroupId.equals(applicationDetail.getIndividualOrGroup().getIndividualOrGroupId())) {
				
				MultipartFile docIndividualOrGroup = docIndividualOrGroupOptional.orElse(null);
				if (individualOrGroupId.equals(2L) && docIndividualOrGroup == null) {
					response.setCode(HttpCode.NULL_OBJECT);
					response.setMessage("Please upload Group File document");
					return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
				}

				Upload individualGroupUpload = null;
				if (docIndividualOrGroup != null) {
					individualGroupUpload = uploadService.uploadFile(docIndividualOrGroup, "Group_File");
					if (individualGroupUpload == null) {
						response.setCode(HttpCode.NULL_OBJECT);
						response.setMessage("Document Groupfile not uploaded.");
						return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
					}
				}
				
				applicationDetail.setIndividualOrGroup(individualOrGroup);
				ConsumerApplicationDetail savedDetail = consumerApplicationDetailRepository.save(applicationDetail);

				if (individualOrGroupId.equals(2L)) {
					ApplicationDocument appDocument = applicationDocumentRepository
							.findByconsumerApplicationDetail_consumerApplicationId(
									savedDetail.getConsumerApplicationId());
					if (individualGroupUpload != null) {
						appDocument.setDocGroup(individualGroupUpload);
						applicationDocumentRepository.save(appDocument);
					}
				}
				response.setCode("200");
				response.setMessage("Changes saved successfully");
				response.setList(Arrays.asList(savedDetail));
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			} else {
				response.setCode("202");
				response.setMessage("No changes needed");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode("404");
			response.setMessage(e.getMessage());
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}
	}

	@PostMapping("/saveJeRemarkForApplicationUpdation")
	public ResponseEntity<?> saveJeRemarkForApplicationUpdation(@RequestBody AppUpdationRemark remark) {
		try {
			Response response = new Response();

			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplicationDetailRepository
					.findByConsumerApplicationNumber(remark.getConsumerAppNo());
			if (findByConsumerApplicationNumber == null) {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Consumer Application not found");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			AppUpdationRemark save = appUpdationRemarkRepository.save(remark);
			response.setCode("200");
			response.setMessage("Data Saved Successfully");
			response.setList(Arrays.asList(save));
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}

	}
	
	@PutMapping("/revertApplcaicationForNatureOfWork/{consumerApplicationNo}/{natrueOfWorkChange}")
	public ResponseEntity<?> revertApplcaicationForNatureOfWork(@PathVariable String consumerApplicationNo,
			@PathVariable Boolean natrueOfWorkChange) {
		try {
			Response response = new Response();

			ConsumerApplicationDetail save = null;
			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplicationDetailRepository
					.findByConsumerApplicationNumber(consumerApplicationNo);
			if (findByConsumerApplicationNumber == null) {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Consumer Application not found");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}

			if (natrueOfWorkChange) {
				findByConsumerApplicationNumber.setApplicationStatus(applicationStatusRepository
						.findById(ApplicationStatusEnum.APPLIACATION_BACK_TO_CONSUMER_END_FOR_UPDATION.getId()).get());
				save = consumerApplicationDetailRepository.save(findByConsumerApplicationNumber);
			} else {
				response.setCode(HttpCode.NOT_ACCEPTABLE);
				response.setMessage("No need to change status");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			response.setCode("200");
			response.setMessage("Application Return Back To 38 Status Successfully");
			response.setList(Arrays.asList(save));
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}

	}
	
	
//	14- Oct-2024 MKMY Document Update

	@PutMapping("/updateMKMYDocument")
	public ResponseEntity<?> updateMKMYDocument(
	        @RequestPart("docSamagraFile") Optional<MultipartFile> docSamagraFileOptional,
	        @RequestPart("docKhasraKhatoni") Optional<MultipartFile> docKhasraKhatoniOptional,
	        @RequestPart("consumerApplicationNo") String consumerApplicationNo) throws DocumentTypeException {

	    MultipartFile samagraFile = docSamagraFileOptional.orElse(null);
	    MultipartFile khasraFile = docKhasraKhatoniOptional.orElse(null);
	    
	    Response updateResponse = consumerApplicationDetailService.updateMKMYDocument(samagraFile, khasraFile, consumerApplicationNo);

	    return ResponseEntity.ok(updateResponse);
	}
	
}
