package com.mpcz.deposit_scheme.backend.api.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.constant.RestApiUrl;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationDocument;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.ErpRev;
import com.mpcz.deposit_scheme.backend.api.domain.Upload;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.exception.DocumentTypeException;
import com.mpcz.deposit_scheme.backend.api.repository.ApplicationDocumentRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.UploadRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerApplicationDetailService;
import com.mpcz.deposit_scheme.backend.api.services.ErpRevService;
import com.mpcz.deposit_scheme.backend.api.services.UploadService;

@RestController
@RequestMapping("/ErpRev")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ErpRevController {

	@Autowired
	private ErpRevService erpRevService;

	@Autowired
	private ConsumerApplicationDetailService ConsumerApplicationDetailService;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private UploadRepository uploadRepository;

	@Autowired
	ApplicationDocumentRepository applicationDocumentRepository;
	
	@Autowired
	private ConsumerApplictionDetailRepository consumerApplicationDetailRepository;
	
	@GetMapping("/RevErpDataNew/{erpNo}/{applicationNo}/{value}")
	public ResponseEntity<Response<ErpRev>> save(@PathVariable String erpNo, @PathVariable String applicationNo,
			@PathVariable Long value) {
		Response<ErpRev> res = new Response<>();
		try {
			ConsumerApplicationDetail findConsumerApplicationDetailByApplicationNo = ConsumerApplicationDetailService
					.findConsumerApplicationDetailByApplicationNo(applicationNo);
			if (findConsumerApplicationDetailByApplicationNo == null) {
				res.setCode("404");
				res.setMessage("consumer applicationn not found");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(res);
			}
			ErpRev erp = erpRevService.save(erpNo, applicationNo, value);
			if(erp.getSchemeCode().equals("Scheme code not match")) {
				res.setCode("404");
				res.setMessage("Scheme code not match");
				res.setList(Arrays.asList(erp));
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(res);
			}
			if (!erp.getSchemeCode().equals("not data show")) {
				res.setCode("200");
				res.setMessage("data save successfully");
				res.setList(Arrays.asList(erp));
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(res);
			} else if (erp.getSchemeCode().equals("not data show"))
				;
			res.setCode("404");
			res.setMessage("this application of elegle type thats way amount not show");
			res.setList(Arrays.asList(erp));
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(res);

		} catch (Exception e) {

		}
		return null;
	}

	@GetMapping("/getConsumerApplication/{consumerApp}")
	public ResponseEntity<?> getConsumerApplication(@PathVariable("consumerApp") String consumerApp)
			throws ConsumerApplicationDetailException {
		return erpRevService.getConsumerApplication(consumerApp);

	}
	
	@PostMapping("/erpReviseFileSave")
	public Response<?> erpReviseFileSave(@RequestPart String consumerApplicationNumber,
			@RequestPart("docErpRevise") Optional<MultipartFile> docErpRevise)
			throws ConsumerApplicationDetailException, DocumentTypeException {

		Response response = new Response();

		MultipartFile docErpRevised = null;
		Upload uploadFile = null;
		
		ConsumerApplicationDetail consumerApplicationDetail = consumerApplicationDetailRepository
				.findByConsumerApplicationNumber(consumerApplicationNumber);
		
		if (Objects.isNull(consumerApplicationDetail)) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.FEEDER_RECORD_FETCH_ALL_FAILED_MESSAGE);
			return response;
		}
		
		
		ApplicationDocument findByConsumerApplicationDetailId = applicationDocumentRepository.findByConsumerApplicationDetailId(consumerApplicationDetail.getConsumerApplicationId());
		if (Objects.isNull(findByConsumerApplicationDetailId)) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.FEEDER_RECORD_FETCH_ALL_FAILED_MESSAGE);
			return response;
		} 
		
		if (docErpRevise.isPresent()) {
			docErpRevised = docErpRevise.get();
		}

		if (docErpRevised != null) {
			 uploadFile = uploadService.uploadFile(docErpRevised, "ERP_REVISE");
			if (uploadFile == null) {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Erp Revise Not Uploaded.");
				throw new ConsumerApplicationDetailException(response);
			}
			findByConsumerApplicationDetailId.setDocErpReviseFile(uploadFile);
			applicationDocumentRepository.save(findByConsumerApplicationDetailId);
			System.out.println("Uploaded file name: " + docErpRevised.getOriginalFilename());
			
		}
		
			Optional<Upload> findById = uploadRepository.findById(uploadFile.getUploadId());
			Upload upload1 = findById.get();
			upload1.setConsuemrApplicatonID(consumerApplicationDetail.getConsumerApplicationId());
			Upload save = uploadRepository.save(upload1);
			
			response.setList(Arrays.asList(save));
			response.setCode(HttpCode.OK);
			response.setMessage(ResponseMessage.RECORD_FETCH_ALL_MESSAGE);
			return response;
	
	
	}
	
	@GetMapping("/negativeRev/{erpNo}/{applicationNo}/{value}")
	public ResponseEntity<?> negativeRev(@PathVariable String erpNo, @PathVariable String applicationNo,
			@PathVariable Long value){
		try {
			
			
			ResponseEntity<Response<ErpRev>> save = save(erpNo,applicationNo,value);
			Response<ErpRev> responseBody = save.getBody();
	        System.out.println("Response body: " + responseBody);
	        List<ErpRev> erpRevList = responseBody.getList();
	        for (ErpRev erpRev : erpRevList) {
	            System.out.println("ErpNo: " + erpRev.getNewErpNo());
	            BigDecimal newSupervisionAmt = erpRev.getNewSupervisionAmt();
	            BigDecimal oldSupervision = erpRev.getOldSupervision();
	            if (newSupervisionAmt.compareTo(oldSupervision) < 0) {
//	            	System.out.println("New Supervision Amount : " +newSupervisionAmt);
	            	System.out.println("New Estimate Amount : " + erpRev.getNewEstimateAmt());
	            	System.out.println("Old Estimate Amount : " +erpRev.getOldEstimate());
	            	System.out.println("Remaining Estimate Amount : " +erpRev.getRemEstimateAmt());
	            	System.out.println("Old Total Paid Amount : " +erpRev.getOldPayableAmt());
	            	System.out.println("Old Cgst : " + erpRev.getOldCgst());
	            	System.out.println("Old Sgst : " + erpRev.getOldSgst());
	            	
	            	BigDecimal oldBalanceAmount = erpRev.getRemEstimateAmt().add(erpRev.getOldCgst()).add(erpRev.getOldSgst());
//	            	System.out.println("Total Amount with GST : " + oldBalanceAmount);
	            	BigDecimal returnableAmount = erpRev.getOldPayableAmt().subtract(erpRev.getNewEstimateAmt());
	            	
	            	System.out.println("Retunable amount : " +oldBalanceAmount);
	            }
	            
	            // Print other properties as needed
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	return null;
	}

	

}
