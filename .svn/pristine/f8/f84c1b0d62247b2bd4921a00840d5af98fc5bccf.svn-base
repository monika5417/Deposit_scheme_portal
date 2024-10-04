package com.mpcz.deposit_scheme.backend.api.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.dto.SSPDto;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerException;
import com.mpcz.deposit_scheme.backend.api.exception.DistributionCenterException;
import com.mpcz.deposit_scheme.backend.api.exception.DocumentTypeException;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.response.SignUpResponse;
import com.mpcz.deposit_scheme.backend.api.services.SSPService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ssp")
public class SSPController {

	@Autowired
	private SSPService sSPService;
	
	
	@PostMapping("/sspSignUp")
	public ResponseEntity<?> sspSignUp(@RequestBody SSPDto sspDto,HttpServletRequest request) throws ConsumerException, DocumentTypeException, DistributionCenterException {
		Response response = new Response<>();
		if(sspDto==null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Object having null value");
			return ResponseEntity.ok(response);
		}else {
		SignUpResponse sspSignUp = sSPService.sspSignUp(sspDto,request);
		if(!sspSignUp.equals(null)) {
			response.setCode("200");
			response.setMessage("Record Inserted Successfully");
			response.setList(Arrays.asList(sspSignUp.getConsumer()));
			response.setToken(sspSignUp.getToken());
			return ResponseEntity.ok(response);
		}else {
			response.setCode("100");
			response.setMessage("Not successfull");
			return ResponseEntity.ok(response);
		}
		}
		
	}

	
}
