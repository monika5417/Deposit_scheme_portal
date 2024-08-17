package com.mpcz.deposit_scheme.backend.api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpcz.deposit_scheme.backend.api.constant.RestApiUrl;
import com.mpcz.deposit_scheme.backend.api.domain.VendorWorkOrder;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.VendorWorkOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "Work OrderController", description = "Rest api for Vendor Work Order Controller.")
@RestController
@RequestMapping(RestApiUrl.WORK_ORDEER_SAVE_API)
public class VendorWorkOrderController {
	
	@Autowired
	private VendorWorkOrderService vendorWorkOrderService;
	
	@PostMapping("/save")
	public ResponseEntity<Response> save(@RequestBody VendorWorkOrder venderWorkOrder) {

		VendorWorkOrder workOrder = vendorWorkOrderService.save(venderWorkOrder);
	
		// logger.info("TableController response" + dataResponse);
		Response response = new Response();
			response.setCode("200");
			response.setMessage("Work Order save successfully ");
			response.setList(Arrays.asList(workOrder));


		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(value = "update Work Order", response = VendorWorkOrder.class)
	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestBody VendorWorkOrder vendorWorkOrder) {

		VendorWorkOrder workOrder = vendorWorkOrderService.update(vendorWorkOrder);
		// logger.info("TableController response" + dataResponse);
		
		
		Response response = new Response();
			response.setCode("200");
			response.setMessage("Contractor Information update successfully");
			response.setList(Arrays.asList(workOrder));
		

		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(value = "Find All Work Order Information", response = VendorWorkOrder.class)
	@GetMapping("/")
	public ResponseEntity<Response> findAll() {
		List<VendorWorkOrder> workOrder = vendorWorkOrderService.findAll();
		Response response = new Response();
		response.setCode("200");
		response.setMessage("success");
		response.setList(Arrays.asList(workOrder));
		return ResponseEntity.ok().body(response);

	}
	
	@ApiOperation(value = "find by work Order Id Vendor Work Order information", response = VendorWorkOrder.class)
	@GetMapping("workorder/{workOrderId}")
	public ResponseEntity<Response> findById(@PathVariable("workOrderId") Long workOrderId) {
		VendorWorkOrder workOrder = vendorWorkOrderService.findById(workOrderId);
		Response response = new Response();
		response.setCode("200");
		response.setMessage("success");
		response.setList(Arrays.asList(workOrder));
		return ResponseEntity.ok().body(response);
	}

	

	@ApiOperation(value = "find by work Order Id Vendor Work Order information", response = VendorWorkOrder.class)
	@GetMapping("workorders/{applicationNo}")
	public ResponseEntity<Response> findByApplicationNo(@PathVariable("applicationNo") String applicationNo) {
		VendorWorkOrder workOrder = vendorWorkOrderService.findByApplicationNo(applicationNo);
		Response response = new Response();
		response.setCode("200");
		response.setMessage("success");
		response.setList(Arrays.asList(workOrder));
		return ResponseEntity.ok().body(response);
	}

}
