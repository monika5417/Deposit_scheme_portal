package com.mpcz.deposit_scheme.backend.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.RefundAmount;
import com.mpcz.deposit_scheme.backend.api.exception.BillDeskPaymentResponseException;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.RefundAmountService;

@RestController
@RequestMapping("/api/refundAmount")
public class RefundAmountController {

	@Autowired
	private RefundAmountService refundAmountService;
	
	@PostMapping("/user/saveRefundDetails")
	public ResponseEntity<?> saveRefundDetails(@RequestBody RefundAmount refund) throws ConsumerApplicationDetailException{
	Response<RefundAmount> response = new Response<RefundAmount>();
		
		if(refund==null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Payload is null");
			return ResponseEntity.ok(response);
		}
		
		RefundAmount saveRefundDetails = refundAmountService.saveRefundDetails(refund);
		if(saveRefundDetails==null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Data not saved");
			return ResponseEntity.ok(response);
		}
		response.setCode(HttpCode.CREATED);
		response.setMessage("Data  saved");
		response.setList(Arrays.asList(saveRefundDetails));
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/user/getRefundApplication/{consumerApplicationNo}")
	public ResponseEntity<?> getRefundApplication(@PathVariable String consumerApplicationNo){
		Response<RefundAmount> response = new Response<RefundAmount>();
		RefundAmount refundApplicationDetails = refundAmountService.getRefundApplicationDetails(consumerApplicationNo);
	if(Objects.isNull(refundApplicationDetails)) {
		response.setCode(HttpCode.NULL_OBJECT);
		response.setMessage("Data not found in database");
		return ResponseEntity.ok(response);
	}else {
	response.setCode(HttpCode.OK);
	response.setMessage("Data  Retrived Successfully");
	response.setList(Arrays.asList(refundApplicationDetails));
	return ResponseEntity.ok(response);
	}
	}
	
	@GetMapping("/user/getAllData")
	public ResponseEntity<?> getAllData(){
		List<RefundAmount> allRefundApplication = refundAmountService.getAllRefundApplication();
		if(Objects.isNull(allRefundApplication)) {
			return ResponseEntity.ok(new Response<Object>(HttpCode.NULL_OBJECT,"Data not found in database"));
		}else {
		return ResponseEntity.ok(new Response<RefundAmount>(HttpCode.OK,"Data  Retrived Successfully",allRefundApplication));
		}
	}
	
	@PostMapping("/consumer/saveApplicationCancelletion")
	public ResponseEntity<?> saveApplicationCancelletion(@RequestBody RefundAmount refundAmount) throws ConsumerApplicationDetailException, BillDeskPaymentResponseException{
		
		if(Objects.isNull(refundAmount)) {
			return ResponseEntity.ok(new Response<RefundAmount>(HttpCode.NULL_OBJECT,"Payload is null"));
		}else {
			RefundAmount saved = refundAmountService.saveConsumerApplicationCancel(refundAmount);
			if(Objects.isNull(saved)) {
				return ResponseEntity.ok(new Response(HttpCode.NULL_OBJECT,"Data Not Saved"));
			}
			return ResponseEntity.ok(new Response(HttpCode.CREATED,"Data Saved Successfully ", Arrays.asList(saved)));
		}
	}
	
	@PutMapping("/user/dgmApprovalForRefund")
	public ResponseEntity<?> dgmApprovalForRefund(@RequestParam String consumerApplicationNo,
			@RequestParam boolean dgmApproval,
			@RequestParam String dgmId) throws ConsumerApplicationDetailException{
		
		RefundAmount dgmApprovalForRefund = refundAmountService.dgmApprovalForRefund(consumerApplicationNo, dgmApproval, dgmId);
		if(Objects.isNull(dgmApprovalForRefund)) {
			return ResponseEntity.ok(new Response(HttpCode.NULL_OBJECT,"Data not updated"));
		}else {
			return ResponseEntity.ok(new Response(HttpCode.UPDATED,"Record Updated Successfully ",Arrays.asList(dgmApprovalForRefund)));
		}
	}
	
	
	@PutMapping("/user/gmApprovalForRefund")
	public ResponseEntity<?> gmApprovalForRefund(@RequestParam String consumerApplicationNo,
			@RequestParam boolean gmApproval,
			@RequestParam String gmId) throws ConsumerApplicationDetailException{
		
		RefundAmount gmApprovalForRefund = refundAmountService.gmApprovalForRefund(consumerApplicationNo, gmApproval, gmId);
		if(Objects.isNull(gmApprovalForRefund)) {
			return ResponseEntity.ok(new Response(HttpCode.NULL_OBJECT,"Data not updated"));
		}else {
			return ResponseEntity.ok(new Response(HttpCode.UPDATED,"Record Updated Successfully ",Arrays.asList(gmApprovalForRefund)));
		}
	}
	
	@GetMapping("/getAllApplicationByConsumerId/{consumerId}")
	public ResponseEntity<?> getAllApplicationByConsumerId(@PathVariable Long consumerId){
		List<ConsumerApplicationDetail> allApplicationByConsumerId = refundAmountService.getAllApplicationByConsumerId(consumerId);
		if(allApplicationByConsumerId.isEmpty()) {
			return ResponseEntity.ok(new Response(HttpCode.NULL_OBJECT,"Application Not Found For This ConsumerId"));
		}else {
			return ResponseEntity.ok(new Response(HttpCode.OK,"Data Retrieved Successfully",allApplicationByConsumerId));
		}
	}
	
	@GetMapping("/getAllJeReturnAmountApp/{consumerId}")
	public ResponseEntity<?> getAllJeReturnAmountApp(@PathVariable Long consumerId){
		List<ConsumerApplicationDetail> allJeReturnAmountApp = refundAmountService.getAllJeReturnAmountApp(consumerId);
		return ResponseEntity.ok(
				allJeReturnAmountApp.isEmpty() 
			    ? new Response(HttpCode.NULL_OBJECT, "Application Not Found For This ConsumerId") 
			    : new Response(HttpCode.OK, "Data Retrieved Successfully", allJeReturnAmountApp)
			);
	}
	
}
