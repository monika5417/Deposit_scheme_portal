package com.mpcz.deposit_scheme.backend.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.ContractorForBidWorkStatus;
import com.mpcz.deposit_scheme.backend.api.domain.VendorAddMaterial;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.exception.VendorAddMaterialException;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ContractorForBidWorkStatusRepository;
import com.mpcz.deposit_scheme.backend.api.repository.VendorAddMaterialRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationStatusService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("/vendor_add_materials")
@CrossOrigin(origins = "*",maxAge = 3600)
public class VendorAddMaterialController {

	@Autowired
	private VendorAddMaterialRepository vendorAddMaterialRepository;
	
	@Autowired
	private ConsumerApplictionDetailRepository consumerApplictionDetailRepository;
	
	@Autowired
	ApplicationStatusService applicationStatusService;
	
	@Autowired
	ContractorForBidWorkStatusRepository contractorForBidWorkStatusRepository;
	
//	 @PersistenceContext
//	  private EntityManager entityManager;

	@PostMapping("/addMaterial")
//	@Transactional
	public ResponseEntity<?> addMaterial(@RequestBody VendorAddMaterial vendorAddMaterial)
			throws VendorAddMaterialException, ConsumerApplicationDetailException {
		Response response = new Response();
		VendorAddMaterial savedVendor = null;
		if (vendorAddMaterial.getConsumerApplicationNumber() == null
				|| vendorAddMaterial.getConsumerApplicationNumber().equals("")) {

			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Please Fill Consumer Application Number");
			throw new VendorAddMaterialException(response);

		}

		if (vendorAddMaterial.getVendorName() == null || vendorAddMaterial.getVendorName().equals("")) {

			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Please Fill Vendor Name");
			throw new VendorAddMaterialException(response);

		}

		if (vendorAddMaterial.getUserId() == null || vendorAddMaterial.getUserId().equals("")) {

			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Please Fill User Id");
			throw new VendorAddMaterialException(response);

		}

		if (vendorAddMaterial.getVendorMaterialSpecification() == null
				|| vendorAddMaterial.getVendorMaterialSpecification().equals("")) {

			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Please Fill Vendor Material Specification");
			throw new VendorAddMaterialException(response);

		}

		if (vendorAddMaterial.getTransformerSerialNo() == null
				|| vendorAddMaterial.getTransformerSerialNo().equals("")) {

			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Please Fill Transformer Serial No. ");
			throw new VendorAddMaterialException(response);

		}

		if (vendorAddMaterial.getMaterialInstallationDate() == null
				|| vendorAddMaterial.getMaterialInstallationDate().equals("")) {

			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Please Fill Material Installation Date");
			throw new VendorAddMaterialException(response);

		}
		
		ConsumerApplicationDetail consumerAppObject = consumerApplictionDetailRepository.findByConsumerApplicationNumber(vendorAddMaterial.getConsumerApplicationNumber());
		
		if(consumerAppObject==null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Consumer Application Number not found in database. Kindly provide the correct application number");
			throw new ConsumerApplicationDetailException(response);
		}
		
		if(consumerAppObject.getSchemeType().getSchemeTypeId().equals(1L)) {
		ContractorForBidWorkStatus findByConsumerApplicationNo = contractorForBidWorkStatusRepository.findByConsumerApplicationNo(vendorAddMaterial.getConsumerApplicationNumber());
		if(findByConsumerApplicationNo==null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Consumer Application Number not found in Contractor For Bid Work Status");
			throw new ConsumerApplicationDetailException(response);
		}
		
		if(findByConsumerApplicationNo.getActualWorkCompletionDate()!=null && !findByConsumerApplicationNo.getActualWorkCompletionDate().equals("None"))
		{
//			 consumerAppObject = entityManager.merge(consumerAppObject);
		 savedVendor = vendorAddMaterialRepository.save(vendorAddMaterial);
		
		ApplicationStatus appStatusDb=null;
		if(savedVendor.getMaterialInstallationDate()!=null) {
			
			
			 appStatusDb = applicationStatusService
					.findById(ApplicationStatusEnum.PENDING_FOR_CONFIRMATION_OF_WORK_COMPLETION_BY_DGM_STC.getId());
			 consumerAppObject.setApplicationStatus(appStatusDb);
			 
			 consumerApplictionDetailRepository.save(consumerAppObject);
			 
		}
		}else {
			response.setCode("404");
			response.setMessage("Actual Date Not Submitted By Contractor");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}
		response.setCode("200");
		response.setMessage("Application Saved Successfully");
		response.setList(Arrays.asList(savedVendor));
		return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}else {
			response.setCode("406");
			response.setMessage("Data can not be submitted because the application not belongs to Supervision cases");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}
	}

	@GetMapping("/getAllVendorMaterial/{consAppNo}")
	public ResponseEntity<?> getAllVendorMaterial(@PathVariable("consAppNo") String consAppNo ){
		Response response = new Response();
		List<VendorAddMaterial> listOfVendorMaterial = vendorAddMaterialRepository.findByConsumerApplicationNumber(consAppNo);
		if(listOfVendorMaterial.isEmpty()) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Application not found in database");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}
		response.setCode(HttpCode.OK);
		response.setMessage("Data Retrive Successfully !!!!!!!");
		response.setList(Arrays.asList(listOfVendorMaterial));
		return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		
	}
	
}
