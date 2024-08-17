package com.mpcz.deposit_scheme.backend.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.request.ErrorDetails;
import com.mpcz.deposit_scheme.backend.api.response.Response;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "MisController", description = "Rest api for Mis Controller ")
@RestController
@RequestMapping("/mis_details")
public class MisController {
	
	@Autowired
	ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@GetMapping("/mis")
	Response getMisDetailsByDcCode(@RequestParam(value = "dcCode",required = false) String dcCode, @RequestParam(value ="circleCode", required = false) String circleCode, @RequestParam(value ="divisionCode", required = false) String divisionCode, @RequestParam(value ="regionCode", required = false) String regionCode) {
		Response response=new Response();
		
		 List<Map<String, ?>> detailsByDcCodeForMis = consumerApplictionDetailRepository.getDetailsByDcCodeForMis(dcCode, circleCode, divisionCode, regionCode);
	if(detailsByDcCodeForMis.isEmpty()) {
		response.setCode("404");
		response.setMessage("Data not found");
		return response;
	}
	response.setCode("200");
	response.setMessage("Data found successfully");
	response.setList(detailsByDcCodeForMis);
	return  response;
	}
	

	@GetMapping("/bill_desk_mis/{toDate}/{fromDate}")
	Response getBillDeskMis(@PathVariable("toDate") String toDate,@PathVariable("fromDate") String fromDate) {
		Response response=new Response();
		
		 List<Map<String, ?>> detailsMisDate = consumerApplictionDetailRepository.getBillDeskMisByDate(toDate,fromDate);
	if(detailsMisDate.isEmpty()) {
		response.setCode("404");
		response.setMessage("Data not found");
		return response;
	}
	response.setCode("200");
	response.setMessage("Data found successfully");
	response.setList(detailsMisDate);
	return  response;
	}
	
	
	

	@GetMapping("/bill_desk/{toDate}/{fromDate}")
	Response getBillDesk(@PathVariable("toDate") String toDate,@PathVariable("fromDate") String fromDate) {
		Response response=new Response();
		
		 List<Map<String, ?>> detailsMisDate = consumerApplictionDetailRepository.getBillDesk(toDate,fromDate);
	if(detailsMisDate.isEmpty()) {
		response.setCode("404");
		response.setMessage("Data not found");
		return response;
	}
	response.setCode("200");
	response.setMessage("Data found successfully");
	response.setList(detailsMisDate);
	return  response;
	}
	
	@GetMapping("/getCraData/{date}")
	Response getCraData(@PathVariable("date") String date) {
		
		Response response = new Response();
		
		List<Map<String, Object>> craData = consumerApplictionDetailRepository.getCraData(date);
		if(craData.isEmpty()) {
			response.setCode("404");
			response.setMessage("Data not found");
			return response;
		}
		response.setCode("200");
		response.setMessage("Data found successfully");
		response.setList(craData);
		return  response;
	}
	
	
	
}
