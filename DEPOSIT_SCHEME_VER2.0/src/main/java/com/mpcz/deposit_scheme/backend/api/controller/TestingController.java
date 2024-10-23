package com.mpcz.deposit_scheme.backend.api.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.constant.RestApiUrl;
import com.mpcz.deposit_scheme.backend.api.domain.Consumer;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.User;
import com.mpcz.deposit_scheme.backend.api.dto.ConsumerApplicationDetailsFilterDTO;
import com.mpcz.deposit_scheme.backend.api.dto.TestDto;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.exception.FormValidationException;
import com.mpcz.deposit_scheme.backend.api.exception.InvalidAuthenticationException;
import com.mpcz.deposit_scheme.backend.api.exception.PaymentTypeException;
import com.mpcz.deposit_scheme.backend.api.repository.BillPaymentResponseeeeeeeRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.EstimateAmountRepository;
import com.mpcz.deposit_scheme.backend.api.response.Meta;
import com.mpcz.deposit_scheme.backend.api.response.PageConsumerApplicationDetailDTO;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerApplicationDetailService;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerService;
import com.mpcz.deposit_scheme.backend.api.services.SSPService;
import com.mpcz.deposit_scheme.backend.api.services.UserService;

import io.swagger.annotations.Api;
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "Testing Controller", description = "Rest api for ContractorDetailForBidController.")
@RestController
@RequestMapping("/api/user")
public class TestingController {
	
	
	@GetMapping("/getsp/{circleId}")
	// ParticipantAndNotParticipantDto getQcConsumerbid() throws Exception {
	ResponseEntity<Response<?>> getQcConsumerbid(@PathVariable Long circleId) throws Exception {
		
		Response<TestDto> response = new Response<>();

		TestDto dto=null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			String url = "https://rooftop-uat.mpcz.in:8443/security-deposit/circlemaster/circle/"+circleId;
			

			HttpEntity<String> entity = new HttpEntity<>(url, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			
			String body = responseEntity.getBody();
			
			if(body != null) {
				
				JSONObject job = new JSONObject(body);
				
				if(job.getString("code").equals("200")) {
					
					
					String string = job.getString("message");
					System.out.println(string);
					
					
					JSONObject jsonObject = job.getJSONObject("object");
					System.out.println(jsonObject.getString("circleId"));
					System.out.println(jsonObject.getString("circleName"));
					System.out.println(jsonObject.getString("gmName"));
					System.out.println(jsonObject.getString("dgmStcName"));
					//System.out.println(jsonObject.getString("circleCode));
				
					 dto = new TestDto();
					
					dto.setMessage(string);
					//dto.setCircleCode(string);
					dto.setCircleId(circleId);
					dto.setCircleName(jsonObject.getString("circleName"));
					dto.setGmName(jsonObject.getString("gmName"));
					dto.setDgmStcName(jsonObject.getString("dgmStcName"));
			
				}
			
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		response.setList(Arrays.asList(dto));
		response.setCode(HttpCode.OK);
		response.setMessage("Record Retrieve Successfully");

		return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
	}
	
	
//	@GetMapping("/sendDataToSSp")
//	public  void sendDataToSSp() {
//		Response response = new Response();
//		
//		
//		String url = "https://survey.mpcz.in:8080/ssp-web/department/post/work/status/ssp";
//		RestTemplate restTemplate = new RestTemplate();
//		
//		Map<String,Object> map = new HashMap<>();
//		map.put("nscApplicationNo", "CZNSCT645");
//		map.put("workStatus", 33);
//		
//		try {
//		ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, map, String.class);
//		System.out.println(postForEntity.getBody() + "aaaaaaaaaaaaaaaaaaa");
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	
//	}

	@Autowired
	private SSPService sSPService;
	
//	@GetMapping("/sendDataToSSp")
//	public void dspFronEndUrl(HttpServletResponse response1) throws IOException {
//
//	    RestTemplate restTemplate = new RestTemplate(); // Use the RestTemplate with logging interceptor
//	    ResponseEntity<String> postForEntity = null;
//	    Map<String, Object> requestMap = new HashMap<>();
//	    requestMap.put("consumerName", "fjdhjkhkjhdjfhj");
//	    requestMap.put("mobileNumber", "8770672335");
//	    requestMap.put("emailId", "testaaaaaaaaaaaa@gmail.com");
//	    requestMap.put("address", "aaaaaaaaaaaaaaaaaa");
//	    requestMap.put("nscApplicationNo", "trttryr1223324323rw");  // Set NSC Application No
//	    requestMap.put("schemeType", 2L);
//	    requestMap.put("natureOfWork", 2L);
//	    requestMap.put("dcCode", "2304402");
//
//	    System.err.println("requestMap : " +new Gson().toJson(requestMap));
//	    String url = "https://rooftop-uat.mpcz.in:8888/deposit_scheme/api/ssp/sspSignUp";
//	    try {
//	        postForEntity = restTemplate.postForEntity(url, requestMap, String.class);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//
//	    System.err.println("postForEntity : " +new Gson().toJson(postForEntity));
//	    
//	    if (postForEntity != null) {
//	        // Parse the response from the 3rd party API
//	        String responseBody = postForEntity.getBody();
//	        JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
//	        String token = jsonResponse.get("token").getAsString();
//	        JsonObject consumerObject = jsonResponse.getAsJsonArray("list").get(0).getAsJsonObject();
//	        
//	        String redirectUrl = "https://rooftop-uat.mpcz.in:8888/deposit-scheme/#/consumer/new-application" +
//	                "?token=" + URLEncoder.encode(token, "UTF-8") +
//	                "&consumer=" + URLEncoder.encode(consumerObject.toString(), "UTF-8");
//
//	        // Perform the redirection
//	        response1.sendRedirect(redirectUrl);
//	      
//	        // Prepare the data for redirection
////	        Map<String, Object> responseMap = new HashMap<>();
////	        responseMap.put("token", token);
////	        responseMap.put("consumer", consumerObject);
////	        responseMap.put("redirectUrl", "https://rooftop-uat.mpcz.in:8888/deposit-scheme/#/consumer/new-application");
////
////	        System.err.println("token  : " +token.toString());
////	        System.err.println("consumer  : " +consumerObject);
////	        System.err.println("responseMap : " + new Gson().toJson(responseMap));
//	         // Return the token and consumer object to the frontend
//	    } else {
//	    	 response1.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to process the request");
//	    }
//	}
	
	
	@GetMapping("/sendDataToSSp")
	public void dspFronEndUrl(HttpServletResponse response1, HttpServletRequest request) throws IOException {

	    // Instead of calling the 3rd party API, use predefined values
	    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NzcwNjcyMzM1IiwiaXNGaXJzdExvZ2luIjp0cnVlLCJjcmVhdGVkIjoxNzI5MDc0MDg0MDMwLCJpcCI6IjA6MDowOjA6MDowOjA6MSIsImNvbnN1bWVyZnVsbG5hbWUiOiJNb25pa2EiLCJ1YSI6IlBvc3RtYW5SdW50aW1lLzcuNDIuMCIsImV4cCI6MTcyOTEwNDA4NCwiaWF0IjoxNzI5MDc0MDg0fQ.7tJSFQkHPgWxqx6DSGDg6Y0iXIkpzy-l_mU0UC81Lu5rK9rLVzcclt_38xePJCTGjQV4SvsZ7Q_d__tH3C9ZIA";

	    // Predefined consumerObject
	    JsonObject consumerObject = new JsonObject();
	    consumerObject.addProperty("created", "2023-09-21T10:06:22.256+00:00");
	    consumerObject.addProperty("consumerId", 5223);
	    consumerObject.addProperty("consumerName", "Monika");
	    consumerObject.addProperty("consumerEmailId", "dufysdchukdychchhc@gmail.com");
	    consumerObject.addProperty("consumerMobileNo", "8770672335");
	    consumerObject.addProperty("accountNonExpired", false);
	    consumerObject.addProperty("accountNonLocked", false);
	    consumerObject.addProperty("isFirstLogin", true);
	    consumerObject.addProperty("consumerLoginId", "8770672335");
	    consumerObject.addProperty("loginAttemp", 0);
	    consumerObject.addProperty("address", "e'prfuk2tduelidlifoewitdedoudyedyt3d;iuewfufo;erkterEPOf67ero;j");
	    consumerObject.addProperty("active", true);
	    consumerObject.addProperty("deleted", false);

	    // Build the redirect URL using the predefined token and consumerObject
	    String redirectUrl = "https://rooftop-uat.mpcz.in:8888/deposit-scheme/#/consumer/new-application";
	   
	    // Perform the redirection
	    response1.sendRedirect(redirectUrl);
	    response1.setHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NzcwNjcyMzM1IiwiaXNGaXJzdExvZ2luIjp0cnVlLCJjcmVhdGVkIjoxNzI5MDc0MDg0MDMwLCJpcCI6IjA6MDowOjA6MDowOjA6MSIsImNvbnN1bWVyZnVsbG5hbWUiOiJNb25pa2EiLCJ1YSI6IlBvc3RtYW5SdW50aW1lLzcuNDIuMCIsImV4cCI6MTcyOTEwNDA4NCwiaWF0IjoxNzI5MDc0MDg0fQ.7tJSFQkHPgWxqx6DSGDg6Y0iXIkpzy-l_mU0UC81Lu5rK9rLVzcclt_38xePJCTGjQV4SvsZ7Q_d__tH3C9ZIA");
	    // Optionally, log the redirect URL and objects for debugging purposes
	   
	    System.err.println("Token: " + token);
	    String header = response1.getHeader("Authorization");
	    System.err.println("heasdddddd : " +header);
	    
	    System.err.println("Consumer Object: " + consumerObject.toString());
	    System.err.println("Redirect URL: " + redirectUrl);
	}

	
}
