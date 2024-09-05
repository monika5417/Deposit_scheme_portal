package com.mpcz.deposit_scheme.backend.api.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.domain.PoseMachinePostData;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.PoseMachinePostDataService;

@RestController
@RequestMapping("/poseMachine")
@CrossOrigin(origins = "*",maxAge = 3600)
public class PoseMachinePostDataController {
	
	@Autowired
	private PoseMachinePostDataService poseMachinePostDataService;
	
	@PostMapping("/savedata")
	public Response savePoseData(@RequestBody PoseMachinePostData poseMachinePostData){
		Response response = new Response();
		
		if(poseMachinePostData.getApplicationNumber().isEmpty() || poseMachinePostData.getApplicationNumber()==null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Application number is empty ");
			return response;
		}
		int compareTo = poseMachinePostData.getTxnAmount().compareTo(new BigDecimal(0.0));
		if(0==compareTo) {
			response.setCode(HttpCode.NOT_ACCEPTABLE);
			response.setMessage("Amount  is zero");
			return response;
		}
		if(0>=compareTo) {
			response.setCode(HttpCode.NOT_ACCEPTABLE);
			response.setMessage("Amount is nagitive ");
			return response;
		}
		
		try {
			String poseMachinePostDatadb =	poseMachinePostDataService.save(poseMachinePostData);
			if(poseMachinePostDatadb.equals("data update succefully")) {
				response.setCode(HttpCode.OK);
				response.setMessage("Data Saved Successfully ");
				response.setList(Arrays.asList(poseMachinePostDatadb));
				return response;
				
			}else {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Data Not Updated ");
				return response;
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
