package com.mpcz.deposit_scheme.backend.api.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.BillDeskPaymentResponse;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.MmkyPayAmount;
import com.mpcz.deposit_scheme.backend.api.domain.NgbStagingData;
import com.mpcz.deposit_scheme.backend.api.dto.NgbDto;
import com.mpcz.deposit_scheme.backend.api.dto.NgbDtoForConusmerNo;
import com.mpcz.deposit_scheme.backend.api.dto.NgbStagingDto;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.repository.BillPaymentResponseeeeeeeRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.MmkyPayAmountRespository;
import com.mpcz.deposit_scheme.backend.api.repository.NgbStagingDataRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationStatusService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/NgbStagingData")
public class NgbDataController {

	@Autowired
	private ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@Autowired
	private BillPaymentResponseeeeeeeRepository billPaymentResponseeeeeeeRepository;

	@Autowired
	private MmkyPayAmountRespository mmkyPayAmountRespository;

	@Autowired
	private NgbStagingDataRepository ngbStagingDataRepository;

	@Autowired
	private ApplicationStatusService applicationStatusService;

	@Value("${ngb.data.url}")
	private String ngbDataUrl;

	@Value("${post.nsc.ssp}")
	private String postNscSsp;
	private static Map<String, String> tokens = new HashMap<>();

// **************************** Posting data to NGB and DSP ************************************************
//	@PostMapping("/saveNgbStagingData")
//	public ResponseEntity<?> saveNgbStagingData(@RequestBody NscStagingDto dto) {
//		Response response = new Response();
//		try {
//			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplictionDetailRepository
//					.findByConsumerApplicationNumber(dto.getPortalReferenceNo());
//			if (findByConsumerApplicationNumber == null) {
//				response.setCode(HttpCode.NULL_OBJECT);
//				response.setMessage("Consumer Application not found");
//				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//			}
//			RestTemplate restTemplate = new RestTemplate();
//			JSONObject nscMiscellaneous = new JSONObject();
//
//			// Add key-value pairs to nscMiscellaneous
//			nscMiscellaneous.put("propertyName", dto.getPropertyName());
//			nscMiscellaneous.put("propertyValue", dto.getPropertyValue());
//			System.out.println("nscMiscellaneous1 : " + nscMiscellaneous);
//
//			JSONArray nscMiscellaneousArray = new JSONArray();
//			nscMiscellaneousArray.put(nscMiscellaneous);
//			System.out.println("nscMiscellaneous2 : " + nscMiscellaneousArray);
//
//			JSONObject nscStaging = new JSONObject();
//			nscStaging.put("connectionDate", dto.getConnectionDate());
//			nscStaging.put("consumerName", dto.getConsumerName());
//			nscStaging.put("consumerNameH", dto.getConsumerNameH());
//			nscStaging.put("relativeName", dto.getRelativeName());
//			nscStaging.put("relation", "FATHER");
//			nscStaging.put("isBPL", "false");
//			nscStaging.put("category", dto.getCategory());
//			nscStaging.put("isEmployee", "false");
//			nscStaging.put("address1", dto.getAddress1());
//			nscStaging.put("address2", dto.getAddress2());
//			nscStaging.put("address3", dto.getAddress3());
//			nscStaging.put("primaryMobileNo", dto.getPrimaryMobileNo());
//			nscStaging.put("aadhaarNo", dto.getAadhaarNo());
//			nscStaging.put("tariffCategory", "LV5");
//			nscStaging.put("connectionType", "PERMANENT");
//			nscStaging.put("meteringStatus", "UNMETERED");
//			nscStaging.put("premiseType", "RURAL");
//			nscStaging.put("sanctionedLoad", dto.getSanctionedLoad());
//			nscStaging.put("sanctionedLoadUnit", dto.getSanctionedLoadUnit());
//			nscStaging.put("contractDemand", 0);
//			nscStaging.put("contractDemandUnit", "KW");
//			nscStaging.put("isSeasonal", "false");
//			nscStaging.put("purposeOfInstallationId", 101);
//			nscStaging.put("purposeOfInstallation", "(FLAT RATE) Permanent agricultural pump");
//			nscStaging.put("tariffCode", "LV5.4");
//			nscStaging.put("subCategoryCode", 512);
//			nscStaging.put("phase", "THREE");
//			nscStaging.put("tcStartDate", "");
//			nscStaging.put("tcEndDate", "");
//			nscStaging.put("isGovernment", "false");
//			nscStaging.put("plotSize", 5); // by default set the value as ankit sir said
//			nscStaging.put("plotSizeUnit", "HCT");
//			nscStaging.put("locationCode", dto.getLocationCode());
//			nscStaging.put("isXray", false);
//			nscStaging.put("isWeldingTransformerSurcharge", false);
//			nscStaging.put("isCapacitorSurcharge", false);
//			nscStaging.put("isDemandside", false);
//			nscStaging.put("isiMotorType", false);
//			nscStaging.put("isBeneficiary", false);
//			nscStaging.put("dtrName", dto.getDtrName());
//			nscStaging.put("poleNo", dto.getPoleNo());
//			nscStaging.put("poleLatitude", "");
//			nscStaging.put("poleLongitude", "");
//			nscStaging.put("feederName", dto.getFeederName());
//			nscStaging.put("poleDistance", dto.getPoleDistance());
//			nscStaging.put("areaStatus", "AUTHORISED");
//			nscStaging.put("groupNo", dto.getGroupNo());
//			nscStaging.put("readingDiaryNo", dto.getReadingDiaryNo());
//			nscStaging.put("dateOfReg", "");
//			nscStaging.put("registrationFeeAmount", dto.getRegistrationFeeAmount());
//			nscStaging.put("registrationFeeAmountMRNo", dto.getRegistrationFeeAmountMRNo());
//			nscStaging.put("securityDepositAmount", dto.getSecurityDepositAmount());
//			nscStaging.put("securityDepositAmountMRNo", dto.getSecurityDepositAmountMRNo());
//			nscStaging.put("isAffiliatedConsumer", false);
//			nscStaging.put("affiliatedConsumerNo", "");
//			nscStaging.put("portalName", "Deposit Supervision Portal");
//			nscStaging.put("portalReferenceNo", dto.getPortalReferenceNo());
//
//			System.out.println("nscStaging : " + nscStaging);
//
//			JSONObject requestData = new JSONObject(new LinkedHashMap<>());
//			requestData.put("nscMiscellaneous", nscMiscellaneousArray);
//			requestData.put("nscStaging", nscStaging);
//
//			System.out.println("requestData : " + requestData);
//			String requestBody = requestData.toString();
//			System.out.println("requestBody : " + requestBody);
//
//			String token = getToken();
//			System.out.println("token : " + token);
//
//			String cleanedToken = token.replace("Bearer ", "");
//
//			System.out.println("cleanedToken : " + cleanedToken);
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", "Bearer " + cleanedToken);
//			headers.setContentType(MediaType.APPLICATION_JSON);
//
//			System.out.println("headers : " + headers);
//
//			HttpEntity httpEntity = new HttpEntity<>(requestBody, headers);
//			System.err.println("httpEntity-->" + httpEntity);
//
//			String url = ngbDataUrl +"/mppkvvcl/nextgenbilling/backend/api/v1/nsc/map";
//			System.out.println(" url : " +url);
//			
//			ResponseEntity<String> postForEntity = null;
//			try {
//				 postForEntity = restTemplate.postForEntity(url, httpEntity, String.class);
//			} catch (HttpClientErrorException.BadRequest e) {
//			    String responseBody = e.getResponseBodyAsString();
//			    System.err.println("Error response: " + responseBody);
//			    response.setCode(ResponseCode.BAD_REQUEST);
//				response.setMessage("Some exception occurred! Please try again.");
//				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//			    
//			} catch (Exception e) {
//			    System.err.println("Error: " + e.getMessage());
//			}
//			
//			System.err.println("postForEntity-->" + postForEntity);
//			String body = postForEntity.getBody();
//			System.out.println("forObject-------------" + body);
//			if (body == null) {
//				response.setMessage("response body is null");
//				response.setCode("200");
//				System.out.println("response body is null ");
//			}
//
//			JSONObject jsonResponse = new JSONObject(body);
//
//			int ngbStagingId = jsonResponse.getInt("id");
//
//			System.out.println("ID: " + ngbStagingId);
//
//			NgbStagingData ngbStagingData = new NgbStagingData();
//
//			ngbStagingData.setAadhaarNo(dto.getAadhaarNo());
//			ngbStagingData.setAddress1(dto.getAddress1());
//			ngbStagingData.setAddress2(dto.getAddress2());
//			ngbStagingData.setAddress3(dto.getAddress3());
//			ngbStagingData.setAffiliatedConsumer(false);
//			ngbStagingData.setAffiliatedConsumerNo(dto.getAffiliatedConsumerNo());
//			ngbStagingData.setAreaStatus("AUTHORISED");
//			ngbStagingData.setBeneficiary(false);
//			ngbStagingData.setBPL(false);
//			ngbStagingData.setCapacitorSurcharge(false);
//			ngbStagingData.setCategory(dto.getCategory());
//			ngbStagingData.setConnectionDate(dto.getConnectionDate());
//			ngbStagingData.setConnectionType("PERMANENT");
//			ngbStagingData.setConsumerApplicationNo(dto.getPortalReferenceNo());
//			ngbStagingData.setConsumerName(dto.getConsumerName());
//			ngbStagingData.setConsumerNameH(dto.getConsumerNameH());
//			ngbStagingData.setContractDemand(0);
//			ngbStagingData.setContractDemandUnit("KW");
//			ngbStagingData.setDateOfReg(dto.getDateOfReg());
//			ngbStagingData.setDemandside(false);
//			ngbStagingData.setDtrName(dto.getDtrName());
//			ngbStagingData.setEmployee(false);
//			ngbStagingData.setFeederName(dto.getFeederName());
//			ngbStagingData.setGovernment(false);
//			ngbStagingData.setGroupNo(dto.getGroupNo());
//			ngbStagingData.setIsiMotorType(false);
//			ngbStagingData.setLocationCode(dto.getLocationCode());
//			ngbStagingData.setMeteringStatus("AUTHORISED");
//			ngbStagingData.setNgbStagingId(dto.getNgbStagingId());
//			ngbStagingData.setPhase("THREE");
//			ngbStagingData.setPlotSize(5);
//			ngbStagingData.setPlotSizeUnit("HCT");
//			ngbStagingData.setPoleNo(dto.getPoleNo());
//			ngbStagingData.setPoleDistance(dto.getPoleDistance());
//			ngbStagingData.setPoleLatitude(dto.getPoleLatitude());
//			ngbStagingData.setPoleLongitude(dto.getPoleLongitude());
//			ngbStagingData.setPortalName(dto.getPortalName());
//			ngbStagingData.setPremiseType("RURAL");
//			ngbStagingData.setPrimaryMobileNo(dto.getPrimaryMobileNo());
////			ngbStagingData.setPropertyName(dto.getPropertyName());
////			ngbStagingData.setPropertyValue(dto.getPropertyValue());
//			ngbStagingData.setPurposeOfInstallation("(FLAT RATE) Permanent agricultural pump");
//			ngbStagingData.setPurposeOfInstallationId(101);
//			ngbStagingData.setReadingDiaryNo(dto.getReadingDiaryNo());
//			ngbStagingData.setRegistrationFeeAmount(dto.getRegistrationFeeAmount());
//			ngbStagingData.setRegistrationFeeAmountMRNo(dto.getRegistrationFeeAmountMRNo());
//			ngbStagingData.setRelation(dto.getRelation());
//			ngbStagingData.setRelativeName(dto.getRelativeName());
//			ngbStagingData.setSanctionedLoad(dto.getSanctionedLoad());
//			ngbStagingData.setSanctionedLoadUnit(dto.getSanctionedLoadUnit());
//			ngbStagingData.setSeasonal(false);
//			ngbStagingData.setSecurityDepositAmount(dto.getSecurityDepositAmount());
//			ngbStagingData.setSecurityDepositAmountMRNo(dto.getSecurityDepositAmountMRNo());
//			ngbStagingData.setSubCategoryCode(512);
//			ngbStagingData.setTariffCategory("LV5");
//			ngbStagingData.setTariffCode("LV5.4");
//			ngbStagingData.setTcEndDate(dto.getTcEndDate());
//			ngbStagingData.setTcStartDate(dto.getTcStartDate());
//			ngbStagingData.setWeldingTransformerSurcharge(false);
//			ngbStagingData.setXray(false);
//			ngbStagingData.setNgbStagingId((long) ngbStagingId);
//			System.out.println("ngbStagingData : " +ngbStagingData);
//			NgbStagingData save = ngbStagingDataRepository.save(ngbStagingData);
//			if (save == null) {
//				response.setCode(ResponseCode.NOT_FOUND);
//				response.setMessage("Data Not Saved In DataBase");
//				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//			}
//			ApplicationStatus applicationStatus = applicationStatusService
//					.findById(ApplicationStatusEnum.CONNECTION_GRANTED_SUCCESSFULLY.getId());
//			findByConsumerApplicationNumber.setApplicationStatus(applicationStatus);
//			consumerApplictionDetailRepository.save(findByConsumerApplicationNumber);
//			response.setCode(ResponseCode.OK);
//			response.setMessage(ResponseMessage.SUCCESS);
//			response.setList(Arrays.asList(save));
//			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
//		}
//	}

//	@PostMapping("/sendDataToNgb")
//	public void sendDataToNgb(@RequestBody NscStagingDto dto) {
//		try {
//
//			Response response = new Response();
//			RestTemplate restTemplate = new RestTemplate();
//
//			JSONObject nscMiscellaneous = new JSONObject();
//
//			// Add key-value pairs to nscMiscellaneous
//			nscMiscellaneous.put("propertyName", dto.getPropertyName());
//			nscMiscellaneous.put("propertyValue", dto.getPropertyValue());
//			System.out.println("nscMiscellaneous1 : " + nscMiscellaneous);
//
//			JSONArray nscMiscellaneousArray = new JSONArray();
//			nscMiscellaneousArray.put(nscMiscellaneous);
//			System.out.println("nscMiscellaneous2 : " + nscMiscellaneousArray);
//
//			JSONObject nscStaging = new JSONObject();
//			nscStaging.put("connectionDate", dto.getConnectionDate());
//			nscStaging.put("consumerName", dto.getConsumerName());
//			nscStaging.put("consumerNameH", dto.getConsumerNameH());
//			nscStaging.put("relativeName", dto.getRelativeName());
//			nscStaging.put("relation", "FATHER");
//			nscStaging.put("isBPL", "false");
//			nscStaging.put("category", dto.getCategory());
//			nscStaging.put("isEmployee", "false");
//			nscStaging.put("address1", dto.getAddress1());
//			nscStaging.put("address2", dto.getAddress2());
//			nscStaging.put("address3", dto.getAddress3());
//			nscStaging.put("primaryMobileNo", dto.getPrimaryMobileNo());
//			nscStaging.put("aadhaarNo", dto.getAadhaarNo());
//			nscStaging.put("tariffCategory", "LV5");
//			nscStaging.put("connectionType", "PERMANENT");
//			nscStaging.put("meteringStatus", "UNMETERED");
//			nscStaging.put("premiseType", "RURAL");
//			nscStaging.put("sanctionedLoad", dto.getSanctionedLoad());
//			nscStaging.put("sanctionedLoadUnit", dto.getSanctionedLoadUnit());
//			nscStaging.put("contractDemand", 0);
//			nscStaging.put("contractDemandUnit", "KW");
//			nscStaging.put("isSeasonal", "false");
//			nscStaging.put("purposeOfInstallationId", 101);
//			nscStaging.put("purposeOfInstallation", "(FLAT RATE) Permanent agricultural pump");
//			nscStaging.put("tariffCode", "LV5.4");
//			nscStaging.put("subCategoryCode", 512);
//			nscStaging.put("phase", "THREE");
//			nscStaging.put("tcStartDate", "");
//			nscStaging.put("tcEndDate", "");
//			nscStaging.put("isGovernment", "false");
//			nscStaging.put("plotSize", 5); // by default set the value as ankit sir said
//			nscStaging.put("plotSizeUnit", "HCT");
//			nscStaging.put("locationCode", dto.getLocationCode());
//			nscStaging.put("isXray", false);
//			nscStaging.put("isWeldingTransformerSurcharge", false);
//			nscStaging.put("isCapacitorSurcharge", false);
//			nscStaging.put("isDemandside", false);
//			nscStaging.put("isiMotorType", false);
//			nscStaging.put("isBeneficiary", false);
//			nscStaging.put("dtrName", dto.getDtrName());
//			nscStaging.put("poleNo", dto.getPoleNo());
//			nscStaging.put("poleLatitude", "");
//			nscStaging.put("poleLongitude", "");
//			nscStaging.put("feederName", dto.getFeederName());
//			nscStaging.put("poleDistance", dto.getPoleDistance());
//			nscStaging.put("areaStatus", "AUTHORISED");
//			nscStaging.put("groupNo", dto.getGroupNo());
//			nscStaging.put("readingDiaryNo", dto.getReadingDiaryNo());
//			nscStaging.put("dateOfReg", "");
//			nscStaging.put("registrationFeeAmount", dto.getRegistrationFeeAmount());
//			nscStaging.put("registrationFeeAmountMRNo", dto.getRegistrationFeeAmountMRNo());
//			nscStaging.put("securityDepositAmount", dto.getSecurityDepositAmount());
//			nscStaging.put("securityDepositAmountMRNo", dto.getSecurityDepositAmountMRNo());
//			nscStaging.put("isAffiliatedConsumer", false);
//			nscStaging.put("affiliatedConsumerNo", "");
//			nscStaging.put("portalName", "Deposit Supervision Portal");
//			nscStaging.put("portalReferenceNo", dto.getPortalReferenceNo());
//
//			System.out.println("nscStaging : " + nscStaging);
//
//			JSONObject requestData = new JSONObject(new LinkedHashMap<>());
//			requestData.put("nscMiscellaneous", nscMiscellaneousArray);
//			requestData.put("nscStaging", nscStaging);
//
//			System.out.println("requestData : " + requestData);
//			String requestBody = requestData.toString();
//			System.out.println("requestBody : " + requestBody);
//
//			String token = getToken();
//			System.out.println("token : " + token);
//
//			String cleanedToken = token.replace("Bearer ", "");
//
//			System.out.println("cleanedToken : " + cleanedToken);
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", "Bearer " + cleanedToken);
//			headers.setContentType(MediaType.APPLICATION_JSON);
//
//			System.out.println("headers : " + headers);
//
//			HttpEntity httpEntity = new HttpEntity<>(requestBody, headers);
//			System.err.println("httpEntity-->" + httpEntity);
//
//			String url = ngbDataUrl + "/mppkvvcl/nextgenbilling/backend/api/v1/nsc/map";
//			ResponseEntity<String> postForEntity = null;
//			try {
//				postForEntity = restTemplate.postForEntity(url, httpEntity, String.class);
//			} catch (HttpClientErrorException.BadRequest e) {
//				String responseBody = e.getResponseBodyAsString();
//				System.err.println("Error response: " + responseBody);
//
//			} catch (Exception e) {
//				System.err.println("Error: " + e.getMessage());
//			}
//
//			System.err.println("postForEntity-->" + postForEntity);
//			String body = postForEntity.getBody();
//			System.out.println("forObject-------------" + body);
////			if (mapBody == null) {
////				response.setMessage("response body is null");
////				response.setCode("200");
////				System.out.println("response body is null ");
////			}
//
//			JSONObject jsonResponse = new JSONObject(body);
//
//			// Get the id from the JSONObject
//			int id = jsonResponse.getInt("id");
//
//			// Now you can use the 'id' variable as needed
//			System.out.println("ID: " + id);
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//
//	}

// *************************** Fetching List data of emptyConsumer and saving the consumerNo in it *********************		
//	@GetMapping("/getConsumerIdByNgbId")
//	public ResponseEntity<?> getConsumerIdByNgbId() {
//		RestTemplate restTemplate = new RestTemplate();
//		Response res = new Response();
//		NgbStagingData save = null;
//		try {
//			String token = getToken();
//
//			List<NgbStagingData> emptyConsumerId = ngbStagingDataRepository.findAllDataWithEmptyConsumerId();
//
//			if (emptyConsumerId.isEmpty()) {
//				res.setCode("404");
//				res.setMessage("No empty consumer IDs found");
//				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(res);
//			}
//
//			for (NgbStagingData ngbData : emptyConsumerId) {
//				Long ngbStagingId = ngbData.getNgbStagingId();
//
//				String apiUrl = ngbDataUrl
//						+ "/mppkvvcl/nextgenbilling/backend/api/v1/nsc/staging/status/nsc-staging-id/" + ngbStagingId;
//
//				HttpHeaders headers = new HttpHeaders();
//				headers.setBearerAuth(token);
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity<String> entity = new HttpEntity<>(headers);
//
//				ResponseEntity<String> response;
//				try {
//					response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
//				} catch (HttpClientErrorException.BadRequest ex) {
//					String responseBody = ex.getResponseBodyAsString();
//					System.err.println("Response from API: " + responseBody + "  NgbId is : " + ngbStagingId);
//					continue;
//				}
//
//				String responseBody = response.getBody();
//
//				if (response.getStatusCode() == HttpStatus.OK) {
//					JSONObject jsonResponse = new JSONObject(responseBody);
//					String consumerNo = jsonResponse.getString("consumerNo");
//
//					ngbData.setNgbConsumerNo(Long.parseLong(consumerNo));
//					save = ngbStagingDataRepository.save(ngbData);
//					if (save == null) {
//						res.setCode(ResponseCode.NOT_FOUND);
//						res.setMessage("Data Not Saved In DataBase");
//						return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(res);
//					}
//				} else {
//					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//							.body("An error occurred while getting data from NGB");
//				}
//			}
//			res.setCode("200");
//			res.setMessage("Consumer IDs updated successfully");
//			res.setList(Arrays.asList(save));
//			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(res);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
//		}
//	}

// *******************************code for sending details to shamshad (4-june-2024)************************************
//	@GetMapping("/getNgbDataForSSP/{consumerAppNo}")
//	public ResponseEntity<Response> getNgbDataForSSP(@PathVariable String consumerAppNo) {
//		Response response = new Response();
//		NgbStagingDto dto = new NgbStagingDto();
//
//		try {
//
//			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplictionDetailRepository
//					.findByConsumerApplicationNumber(consumerAppNo);
//			if (findByConsumerApplicationNumber == null) {
//				response.setCode(HttpCode.NULL_OBJECT);
//				response.setMessage("Consumer Application not found");
//				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//			}
//
//			Date createdDate = findByConsumerApplicationNumber.getCreated();
//
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Specify the desired date format
//
//			String formattedDate = dateFormat.format(createdDate);
//			System.out.println("formattedDate : " + formattedDate);
//
//			dto.setConsumerName(findByConsumerApplicationNumber.getConsumerName());
//			dto.setRelativeName(findByConsumerApplicationNumber.getGuardianName());
//			dto.setRelation("FATHER");
//			dto.setCategory(findByConsumerApplicationNumber.getCastCategory());
//			dto.setAadhaarNo(findByConsumerApplicationNumber.getAadharNo());
//			dto.setAddress1(findByConsumerApplicationNumber.getAddress());
//			dto.setAddress2(findByConsumerApplicationNumber.getPincode());
//			dto.setAddress3(findByConsumerApplicationNumber.getDistrict().getDistrictName());
//			dto.setDateOfReg(formattedDate);
//			dto.setLocationCode(Long.parseLong(findByConsumerApplicationNumber.getDistributionCenter().getNgbDcCd()));
//			dto.setPlotSize(5.0);
//			dto.setPlotSizeUnit("HCT");
//			dto.setPortalName("DSP");
//			dto.setApplicationNumber(consumerAppNo);
//			dto.setPrimaryMobileNo(findByConsumerApplicationNumber.getConsumers().getConsumerMobileNo());
//			dto.setConnectionType("PERMANENT");
//			dto.setPurposeOfInstallation("(FLAT RATE) Permanent agricultural pump");
//			dto.setPurposeOfInstallationId(101L);
//			dto.setSanctionedLoad(Double.parseDouble(findByConsumerApplicationNumber.getJeLoad()));
//			dto.setSanctionedLoadUnit(findByConsumerApplicationNumber.getJeLoadUnitKwYaKva());
//			dto.setSubCategoryCode(512L);
//			dto.setTariffCategory("LV5");
//			dto.setTarrifCode("LV5.4");
//			dto.setPremiseType("RURAL");
//			dto.setMeteringStatus("UNMETERED");
//			dto.setConsumerNameH("");
//			dto.setPropertyName("khasra_no");
//			dto.setPropertyValue(findByConsumerApplicationNumber.getKhasra());
//			dto.setAreaStatus("AUTHORISED");
//			dto.setPhase("THREE");
//			dto.setIsBpl("false");
//			dto.setIsEmployee("false");
//			dto.setIsSeasonal("false");
//			dto.setIsXray("false");
//			dto.setIsWeldingTransformerSurcharge("false");
//			dto.setIsCapacitorSurcharge("false");
//			dto.setIsDemandside("false");
//			dto.setIsiMotorType("false");
//			dto.setIsBeneficiary("false");
//			dto.setIsAffiliatedConsumer("false");
//			dto.setIsGovernment("false");
//			dto.setContractDemand(0.0);
//			dto.setContractDemandUnit("KW");
//			dto.setRegistrationFeeAmount(new BigDecimal(2500.00));
//		
//			List<BillDeskPaymentResponse> findByConsumerApplicationNo = billPaymentResponseeeeeeeRepository
//					.findByConsumerApplicationNo(consumerAppNo);
//			if (findByConsumerApplicationNo == null || findByConsumerApplicationNo.isEmpty()) {
//				response.setCode(HttpCode.NULL_OBJECT);
//				response.setMessage("Consumer Application not found in BillDesk Payment Response");
//				
//				
//				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//			}
//			for (BillDeskPaymentResponse bill : findByConsumerApplicationNo) {
//				if (bill.getAdditionalInfo().equals("Demand_fees") && bill.getAuth_status().equals("0300")) {
//
//					String tranId = bill.getTranId();
//					int startIndex = Math.max(0, tranId.length() - 10);
//					String lastTenChars = tranId.substring(startIndex);
//					dto.setRegistrationFeeAmountMrNo(lastTenChars);
//					dto.setSecurityDepositAmountMrNo(lastTenChars);
//
//				}
//			}
//			MmkyPayAmount mmkyPayAmnt = mmkyPayAmountRespository.findByConsumerApplicationNumber(consumerAppNo);
//			if (mmkyPayAmnt == null) {
//				response.setCode(HttpCode.NULL_OBJECT);
//				response.setMessage("Consumer Application not found in MmkyPayAmount");
//				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//			}
//			dto.setSecurityDepositAmount(mmkyPayAmnt.getSecurityDeposit());
//
//			response.setCode(ResponseCode.OK);
//			response.setMessage(ResponseMessage.SUCCESS);
//			response.setList(Arrays.asList(dto));
//			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//
//		} catch (Exception e) {
//			response.setCode("500");
//			response.setMessage("Internal Server Error");
//			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
//		}
//	}

// ***********************  Get Token for NGB Data Request ******************************************	
	@GetMapping("/getToken")
	public String getToken() {
		RestTemplate rest = new RestTemplate();
		try {

//			    "username": "ADMIN_CZ",
//			    "password": "ADMIN_CZ"
			String url = ngbDataUrl + "/mppkvvcl/nextgenbilling/backend/api/v1/authentication/login";
			System.out.println(" url : " + url);
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("username", "ADMIN_CZ");
			requestBody.put("password", "ADMIN_CZ");

			System.out.println("requestBody : " + requestBody);

			ResponseEntity<Map> postForEntity = rest.postForEntity(url, requestBody, Map.class);

			System.out.println("postForEntity : " + postForEntity);
			if (postForEntity.getStatusCode() == HttpStatus.OK) {
				HttpHeaders responseHeaders = postForEntity.getHeaders();
				String authToken = responseHeaders.getFirst(HttpHeaders.AUTHORIZATION);

				tokens.put("authorizationToken", authToken);

				System.out.println("Authorization Token: " + authToken);
				return authToken;
			} else {
				throw new RuntimeException("Failed to retrieve authorization token");
			}

		} catch (Exception e) {
			return null;
		}

	}

	// *************************** Fetching data by location code and sending it to
	// front end *********************
	@GetMapping("/fetchDataByLocationCode/{locationCode}")
	public ResponseEntity<?> fetchDataByLocationCode(@PathVariable Long locationCode) {
		RestTemplate restTemplate = new RestTemplate();
		Response res = new Response();
		try {

			String token = getToken();

			System.out.println("Token : " + token);
			String apiUrl = ngbDataUrl + "/mppkvvcl/nextgenbilling/backend/api/v1/group//location-code/" + locationCode
					+ "/is-deleted/false";

			System.out.println("apiUrl : " + apiUrl);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				String responseBody = response.getBody();

				System.out.println("Response: " + responseBody);
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.body(responseBody);

			} else {
				System.out.println("Error: " + response.getStatusCodeValue());
				return ResponseEntity.status(response.getStatusCode()).body("Error occurred while fetching data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}

	}

	// *************************** Fetching data by Group No and sending it to front
	// end *********************
	@GetMapping("/fetchDataWithGRNo/{GrNo}")
	public ResponseEntity<?> fetchDataWithGRNo(@PathVariable String GrNo) {
		RestTemplate restTemplate = new RestTemplate();
		Response res = new Response();
		try {

			String token = getToken();
			System.out.println("Token : " + token);
//				http://172.16.17.110:8080/mppkvvcl/nextgenbilling/backend/api/v1/reading/diary/group/no/RMP21

			String apiUrl = ngbDataUrl + "/mppkvvcl/nextgenbilling/backend/api/v1/reading/diary/group/no/" + GrNo;

			System.out.println("apiUrl : " + apiUrl);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				String responseBody = response.getBody();

				System.out.println("Response: " + responseBody);

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.body(responseBody);

			} else {
				System.out.println("Error: " + response.getStatusCodeValue());
				return ResponseEntity.status(response.getStatusCode()).body("Error occurred while fetching data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}

	}

	// *************************** Fetching data by Isampark Code and sending it to
	// front end *********************
	@GetMapping("/getFeederDataWithIsamparkCode/{iSamparkCode}")
	public ResponseEntity<?> getFeederDataWithIsamparkCode(@PathVariable Long iSamparkCode) {
		RestTemplate restTemplate = new RestTemplate();
		Response res = new Response();
		try {

			String apiUrl = "http://isampark.mpcz.in/UrjasApi/UrjasApi.aspx?api=1&var=" + iSamparkCode;

			System.out.println("apiUrl : " + apiUrl);
			HttpHeaders headers = new HttpHeaders();
//				headers.setBearerAuth(token);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				String responseBody = response.getBody();

				System.out.println("Response: " + responseBody);

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.body(responseBody);

			} else {
				System.out.println("Error: " + response.getStatusCodeValue());
				return ResponseEntity.status(response.getStatusCode()).body("Error occurred while fetching data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}

	}

	// *************************** Fetching data by Feeder Code and sending it to
	// front end *********************
	@GetMapping("/getDataWithFeederCode/{feederCode}")
	public ResponseEntity<?> getDataWithFeederCode(@PathVariable Long feederCode) {
		RestTemplate restTemplate = new RestTemplate();
		Response res = new Response();
		try {

			String apiUrl = "http://isampark.mpcz.in/UrjasApi/UrjasApi.aspx?api=0&var=" + feederCode;

			System.out.println("apiUrl : " + apiUrl);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				String responseBody = response.getBody();

				System.out.println("Response: " + responseBody);

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.body(responseBody);

			} else {
				System.out.println("Error: " + response.getStatusCodeValue());
				return ResponseEntity.status(response.getStatusCode()).body("Error occurred while fetching data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}

	}

//		*************************** Sending data to front end which is present in Database for NGB ********************************
	@GetMapping("/getNgbData/{consumerAppNo}")
	public ResponseEntity<Response> getNgbData(@PathVariable String consumerAppNo) {
		Response response = new Response();
		NgbDto dto = new NgbDto();

		try {
			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplictionDetailRepository
					.findByConsumerApplicationNumber(consumerAppNo);
			if (findByConsumerApplicationNumber == null) {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Consumer Application not found");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			Date createdDate = findByConsumerApplicationNumber.getCreated();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Specify the desired date format

			String formattedDate = dateFormat.format(createdDate);
			System.out.println("formattedDate : " + formattedDate);

			dto.setConsumerName(findByConsumerApplicationNumber.getConsumerName());
			dto.setRelativeName(findByConsumerApplicationNumber.getGuardianName());
			dto.setRelation("FATHER");
			dto.setCategory(findByConsumerApplicationNumber.getCastCategory());
			dto.setAadhaarNo(findByConsumerApplicationNumber.getAadharNo());
			dto.setAddress1(findByConsumerApplicationNumber.getAddress());
			dto.setAddress2(findByConsumerApplicationNumber.getPincode());
			dto.setAddress3(findByConsumerApplicationNumber.getDistrict().getDistrictName());
			dto.setDateOfReg(formattedDate);
			dto.setLocationCode(Long.parseLong(findByConsumerApplicationNumber.getDistributionCenter().getNgbDcCd()));
			dto.setPlotSize(5.0);
			dto.setPlotSizeUnit("HCT");
			dto.setPortalName("DSP");
			dto.setApplicationNumber(consumerAppNo);
			dto.setPrimaryMobileNo(findByConsumerApplicationNumber.getConsumers().getConsumerMobileNo());
			dto.setConnectionType("PERMANENT");
			dto.setPurposeOfInstallation("(FLAT RATE) Permanent agricultural pump");
			dto.setPurposeOfInstallationId(101L);
			dto.setSanctionedLoad(Double.parseDouble(findByConsumerApplicationNumber.getJeLoad()));
			dto.setSanctionedLoadUnit(findByConsumerApplicationNumber.getJeLoadUnitKwYaKva());
			dto.setSubCategoryCode(512L);
			dto.setTariffCategory("LV5");
			dto.setTarrifCode("LV5.4");
			dto.setPremiseType("RURAL");
			dto.setMeteringStatus("UNMETERED");
			dto.setConsumerNameH("");
			dto.setPropertyName("khasra_no");
			dto.setPropertyValue(findByConsumerApplicationNumber.getKhasra());
			dto.setAreaStatus("AUTHORISED");
			dto.setPhase("THREE");
			dto.setIsBpl("false");
			dto.setIsEmployee("false");
			dto.setIsSeasonal("false");
			dto.setIsXray("false");
			dto.setIsWeldingTransformerSurcharge("false");
			dto.setIsCapacitorSurcharge("false");
			dto.setIsDemandside("false");
			dto.setIsiMotorType("false");
			dto.setIsBeneficiary("false");
			dto.setIsAffiliatedConsumer("false");
			dto.setIsGovernment("false");
			dto.setContractDemand(0.0);
			dto.setContractDemandUnit("KW");
			dto.setRegistrationFeeAmount(new BigDecimal(2500.00));
			dto.setiSamparkLocationCode(
					Long.parseLong(findByConsumerApplicationNumber.getDistributionCenter().getDcCode()));

			List<BillDeskPaymentResponse> findByConsumerApplicationNo = billPaymentResponseeeeeeeRepository
					.findByConsumerApplicationNo(consumerAppNo);
			if (findByConsumerApplicationNo == null || findByConsumerApplicationNo.isEmpty()) {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Consumer Application not found in BillDesk Payment Response");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			for (BillDeskPaymentResponse bill : findByConsumerApplicationNo) {
				if (bill.getAdditionalInfo().equals("Demand_fees") && bill.getAuth_status().equals("0300")) {

					String tranId = bill.getTranId();
					int startIndex = Math.max(0, tranId.length() - 10);
					String lastTenChars = tranId.substring(startIndex);
					dto.setRegistrationFeeAmountMrNo(lastTenChars);
					dto.setSecurityDepositAmountMrNo(lastTenChars);

				}
			}
			MmkyPayAmount mmkyPayAmnt = mmkyPayAmountRespository.findByConsumerApplicationNumber(consumerAppNo);
			if (mmkyPayAmnt == null) {
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Consumer Application not found in MmkyPayAmount");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
			dto.setSecurityDepositAmount(mmkyPayAmnt.getSecurityDeposit());

			response.setCode(ResponseCode.OK);
			response.setMessage(ResponseMessage.SUCCESS);
			response.setList(Arrays.asList(dto));
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);

		} catch (Exception e) {
			response.setCode("500");
			response.setMessage("Internal Server Error");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}
	}

//		Post data to SSP Portal
	@PostMapping("/saveDataToSsp")
	public ResponseEntity<?> saveDataToSsp(@RequestBody NgbStagingDto dto) {
		Response response = new Response();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> postForEntity = null;
		ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplictionDetailRepository
				.findByConsumerApplicationNumber(dto.getApplicationNumber());
		if (findByConsumerApplicationNumber == null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Consumer Application not found");

			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}


		System.out.println("dto : " + dto);
		String url = postNscSsp;
		try {

			postForEntity = restTemplate.postForEntity(url, dto, Map.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("postForEntity : " + postForEntity.getBody());
		System.out.println("postForEntity : " + postForEntity.getBody().get("message"));
		System.out.println("postForEntity : " + postForEntity.getBody().get("statusCode"));
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb : ");
		System.out.println("postForEntity : " + postForEntity.getBody().containsValue(201));
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa : ");

		if (postForEntity != null) {
			Map<String, Object> responseBody = postForEntity.getBody();
			if (responseBody != null && responseBody.containsKey("statusCode")
					&& responseBody.get("statusCode").equals(201)) {
				NgbStagingData saveToNgbStagingData = saveToNgbStagingData(dto);
				NgbStagingData save = ngbStagingDataRepository.save(saveToNgbStagingData);

				if (save != null) {
					ApplicationStatus applicationStatus = applicationStatusService
							.findById(ApplicationStatusEnum.CONNECTION_GRANTED_SUCCESSFULLY.getId());
					findByConsumerApplicationNumber.setApplicationStatus(applicationStatus);
					ConsumerApplicationDetail save2 = consumerApplictionDetailRepository
							.save(findByConsumerApplicationNumber);
					response.setCode(HttpCode.OK);
					response.setMessage("Data sent to SSP Portal and saved in DSP database");
					response.setList(Arrays.asList(save2));
					return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
				} else {
					response.setCode(HttpCode.NULL_OBJECT);
					response.setMessage("Failed to save data in DSP database");
					return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
				}
			} else if (responseBody.get("statusCode").equals(208)) {
				response.setCode(HttpCode.NOT_ACCEPTABLE);
				response.setMessage("Already Pushed in saral sanyojan");
				return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
			}
		}

		response.setCode(HttpCode.NULL_OBJECT);
		response.setMessage("Failed to post data to SSP Portal");
		return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
	}

	public NgbStagingData saveToNgbStagingData(NgbStagingDto dto) {

		NgbStagingData data = new NgbStagingData();
		data.setAadhaarNo(dto.getAadhaarNo());
		data.setAddress1(dto.getAddress1());
		data.setAddress2(dto.getAddress2());
		data.setAddress3(dto.getAddress3());
		data.setAffiliatedConsumerNo(dto.getAffiliatedConsumerNo());
		data.setApplicationNumber(dto.getApplicationNumber());
		data.setAreaStatus(dto.getAreaStatus());
		data.setCategory(dto.getCategory());
		data.setConnectionDate(dto.getConnectionDate());
		data.setConnectionType("PERMANENT");
		data.setConsumerName(dto.getConsumerName());
		data.setConsumerNameH(dto.getConsumerNameH());
		data.setContractDemand(0.0);
		data.setContractDemandUnit("KW");
		data.setDateOfReg(dto.getDateOfReg());
		data.setDtrName(dto.getDtrName());
		data.setFeederName(dto.getFeederName());
		data.setGroupNo(dto.getGroupNo());
		data.setIsAffiliatedConsumer("false");
		data.setIsBeneficiary("false");
		data.setIsBpl("false");
		data.setIsCapacitorSurcharge("false");
		data.setIsDemandside("false");
		data.setIsEmployee("false");
		data.setIsGovernment("false");
		data.setIsiMotorType("false");
		data.setIsSeasonal("false");
		data.setIsWeldingTransformerSurcharge("false");
		data.setIsXray("false");
		data.setLocationCode(dto.getLocationCode());
		data.setMeteringStatus("UNMETERED");
		data.setPhase("THREE");
		data.setPlotSize(5.0);
		data.setPlotSizeUnit("HCT");
		data.setPoleDistance(dto.getPoleDistance());
		data.setPoleLatitude(dto.getPoleLatitude());
		data.setPoleLongitude(dto.getPoleLongitude());
		data.setPoleNo(dto.getPoleNo());
		data.setPortalName("DSP");
		data.setPremiseType("RURAL");
		data.setPrimaryMobileNo(dto.getPrimaryMobileNo());
		data.setPropertyName(dto.getPropertyName());
		data.setPropertyValue(dto.getPropertyValue());
		data.setPurposeOfInstallation("(FLAT RATE) Permanent agricultural pump");
		data.setPurposeOfInstallationId(101L);
		data.setReadingDiaryNo(dto.getReadingDiaryNo());
		data.setRegistrationFeeAmount(dto.getRegistrationFeeAmount());
		data.setRegistrationFeeAmountMrNo(dto.getRegistrationFeeAmountMrNo());
		data.setRelation("FATHER");
		data.setRelativeName(dto.getRelativeName());
		data.setSanctionedLoad(dto.getSanctionedLoad());
		data.setSanctionedLoadUnit(dto.getSanctionedLoadUnit());
		data.setSecurityDepositAmount(dto.getSecurityDepositAmount());
		data.setSecurityDepositAmountMrNo(dto.getSecurityDepositAmountMrNo());
		data.setSubCategoryCode(512L);
		data.setTariffCategory("LV5");
		data.setTarrifCode("LV5.4");
		data.setTcEndDate(dto.getTcEndDate());
		data.setTcStartDate(dto.getTcStartDate());



		return data;
	}

//	 Put api to update consumerNo, nscAppId, pushDate from SSP portal

	@PutMapping("/putConsumerNoFromSSP")
	public ResponseEntity<?> putConsumerNoFromSSP(@RequestBody NgbDtoForConusmerNo dto) {
		Response response = new Response();
		ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplictionDetailRepository
				.findByConsumerApplicationNumber(dto.getApplicationNo());
		if (findByConsumerApplicationNumber == null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Consumer Application not found");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}
		NgbStagingData findByApplicationNumber = ngbStagingDataRepository
				.findByApplicationNumber(dto.getApplicationNo());
		if (findByApplicationNumber == null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Consumer Application not found in NGB Staging Data Table");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		} else {

			findByApplicationNumber.setNscAppId(dto.getNscAppId());
			findByApplicationNumber.setNgbPushDate(dto.getNgbPushDate());
			findByApplicationNumber.setNgbConsumerNo(dto.getNgbConsumerNo());
			NgbStagingData save = ngbStagingDataRepository.save(findByApplicationNumber);
			response.setCode(HttpCode.OK);
			response.setMessage("Consumer Number Inserted Successfully");
			return ResponseEntity.ok().header(ResponseMessage.APPLICATION_TYPE_JSON).body(response);
		}
	}

	@GetMapping("/getGroupNoByLocationCode/{locationCode}")
	public ResponseEntity<?> getGroupNoByLocationCode(@PathVariable Long locationCode) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;

		String url = "https://survey.mpcz.in:8080/ssp-web/ngb/getGroupNo?locationCode=" + locationCode;
		System.out.println("url : " + url);
		// Prepare headers if needed
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// You might need to set a request body, in this case it's just an empty body
		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			 response = restTemplate.postForEntity(url, request, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("response : " + response);
		return response;

	}

	@GetMapping("/getDiaryNoByGroupNo/{groupNo}")
	public ResponseEntity<?> getDiaryNoByGroupNo(@PathVariable String groupNo){
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		
		String url = "https://survey.mpcz.in:8080/ssp-web/ngb/getReadingDiaryNo?groupNo=" +groupNo;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(headers);
		
		try {
			 response = restTemplate.postForEntity(url, request, String.class);
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return response;
	}
	
	@GetMapping("/getNgbDataByApplicationNo/{consumerAppNo}")
	public ResponseEntity<?> getNgbDataByApplicationNo(@PathVariable String consumerAppNo){
		Response response = new Response();
		NgbStagingData findByApplicationNumber = ngbStagingDataRepository.findByApplicationNumber(consumerAppNo);
		if(findByApplicationNumber==null) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage("Conusmer Application Number Not Found In NGB Staging Data Table");
			return ResponseEntity.ok(response);
		}
		response.setCode(HttpCode.OK);
		response.setMessage("Application Data Retrived Successfully ");
		response.setList(Arrays.asList(findByApplicationNumber));
		return ResponseEntity.ok(response);
	}
	
	
}
