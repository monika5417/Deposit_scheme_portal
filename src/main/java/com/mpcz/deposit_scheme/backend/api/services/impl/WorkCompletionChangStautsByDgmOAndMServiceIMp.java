package com.mpcz.deposit_scheme.backend.api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.BillDeskPaymentResponse;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.ErpEstimateAmountData;
import com.mpcz.deposit_scheme.backend.api.domain.PoseMachinePostData;
import com.mpcz.deposit_scheme.backend.api.domain.WorkCompletionChangStautsByDgmOAndM;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.exception.ErpEstimateAmountException;
import com.mpcz.deposit_scheme.backend.api.repository.BillPaymentResponseeeeeeeRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.EstimateAmountRepository;
import com.mpcz.deposit_scheme.backend.api.repository.PoseMachinePostDataRepository;
import com.mpcz.deposit_scheme.backend.api.repository.WorkCompletionChangStautsByDgmOAndMRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationStatusService;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerApplicationDetailService;
import com.mpcz.deposit_scheme.backend.api.services.WorkCompletionChangStautsByDgmOAndMService;
import com.mpcz.deposit_scheme.feignClient.SSPFeignClientAPI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkCompletionChangStautsByDgmOAndMServiceIMp implements WorkCompletionChangStautsByDgmOAndMService {

	@Value("${post.nsc.oyt.ssp}")
	private String postNscOytSSP;

	@Autowired
	private WorkCompletionChangStautsByDgmOAndMRepository workCompletionChangStautsByDgmOAndMService;

	@Autowired
	private ConsumerApplicationDetailService consumerApplicationDetailService;

	@Autowired
	private ApplicationStatusService applicationStatusService;

	@Autowired
	private ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@Autowired
	private EstimateAmountRepository estimateAmountRepository;

	@Autowired
	private BillPaymentResponseeeeeeeRepository billPaymentResponseeeeeeeRepository;

	@Autowired
	private PoseMachinePostDataRepository poseMachinePostDataRepository;

	@Autowired
	private SSPFeignClientAPI sSPFeignClientAPI;

	private static final Logger log = LoggerFactory.getLogger(WorkCompletionChangStautsByDgmOAndMServiceIMp.class);

	@Override
//	@Transactional(readOnly = true)
	public WorkCompletionChangStautsByDgmOAndM saveAndChange(
			WorkCompletionChangStautsByDgmOAndM workCompletionChangStautsByDgmOAndM)
			throws ConsumerApplicationDetailException, ErpEstimateAmountException {

		ApplicationStatus applicationStatus = null;
		try {

			BigDecimal securityDeposit = BigDecimal.ZERO;
			
			log.info("[START] saveAndChange | consumerApplicationId: {}",
					workCompletionChangStautsByDgmOAndM.getConsumerApplicationId());

			log.info("Fetching ConsumerApplicationDetail | consumerApplicationId: {}",
					workCompletionChangStautsByDgmOAndM.getConsumerApplicationId());

			ConsumerApplicationDetail applicationDetail = consumerApplicationDetailService
					.findByConsumerApplicationId(workCompletionChangStautsByDgmOAndM.getConsumerApplicationId());
			MDC.put(
		            "applicationNo",
		            applicationDetail.getConsumerApplicationNo()
		    );
			log.info("Application fetched successfully | applicationNo: {} | nscNo: {}",
					applicationDetail.getConsumerApplicationNo(), applicationDetail.getNscApplicationNo());
			log.info("workComplationChangedByDgmOrendum value: {}",
					workCompletionChangStautsByDgmOAndM.getWorkComplationChangedByDgmOrendum());
			if (applicationDetail.getNscApplicationNo() != null) {
				if (workCompletionChangStautsByDgmOAndM.getWorkComplationChangedByDgmOrendum()
						.equalsIgnoreCase("false")) {
					if (applicationDetail.getIvrsNo() == null) {
						// added this check for new scheme 09-04-2026
						if (applicationDetail.getSchemeType().getSchemeTypeId().equals(3L)) {
							log.info("ND Scheme detected | applicationNo: {}",
									applicationDetail.getConsumerApplicationNo());
							applicationStatus = applicationStatusService
									.findById(ApplicationStatusEnum.WORK_FINISED.getId());
							applicationDetail.setApplicationStatus(applicationStatus);
							Map<String, Object> map = new HashMap<>();
							map.put("applicationNumber", applicationDetail.getNscApplicationNo());
							map.put("dspApplicationNo", applicationDetail.getConsumerApplicationNo());
							map.put("dspStatus", "Work Completed");
							map.put("workStatus", applicationDetail.getApplicationStatus().getApplicationStatusId());
							log.info("Sending ND Scheme data to SSP | applicationNo: {} | payload: {}",
									applicationDetail.getConsumerApplicationNo(), new Gson().toJson(map));
							ResponseEntity<?> sendNDSchemeDataToSSP = sSPFeignClientAPI.sendNDSchemeDataToSSP(map);
							System.err.println("aaaaaaaaaaaaaaaaaa : " + new Gson().toJson(sendNDSchemeDataToSSP));
							JSONObject responseJson = new JSONObject(sendNDSchemeDataToSSP.getBody());
							log.info("SSP response received | applicationNo: {} | response: {}",
									applicationDetail.getConsumerApplicationNo(),
									new Gson().toJson(sendNDSchemeDataToSSP.getBody()));

							// Extract the message and statusCode
							try {
//								String message = responseJson.getString("message");
								int statusCode = responseJson.getInt("statusCode");
								String message = responseJson.getString("message");
								if (statusCode == 200) {
									applicationDetail.setSspApplicationCompleted("true");
									log.info("SSP integration successful | applicationNo: {}",
											applicationDetail.getConsumerApplicationNo());
								} else {
									applicationDetail.setSspApplicationCompleted(message);
									log.warn("SSP integration failed | applicationNo: {} | reason: {}",
											applicationDetail.getConsumerApplicationNo(), message);
								}
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								String formattedDate = LocalDateTime.now().format(formatter);
								applicationDetail.setSspApplicationCompleteDate(formattedDate);
								consumerApplictionDetailRepository.save(applicationDetail);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// added this check for new scheme 09-04-2026
						} else {
							applicationStatus = applicationStatusService.findById(
									ApplicationStatusEnum.WORK_FINISHED_NOW_PENDING_FOR_CONNECTION_PUNCHING.getId());
						}

					} else {
						applicationStatus = applicationStatusService
								.findById(ApplicationStatusEnum.CONNECTION_GRANTED_SUCCESSFULLY.getId());
					}
					applicationDetail.setDateOfDgmOandM(workCompletionChangStautsByDgmOAndM.getDateOfDgmOandM());
					applicationDetail.setApplicationStatus(applicationStatus);
//					ConsumerApplicationDetail saveConsumerApplicationDB = consumerApplicationDetailService
//							.saveConsumerApplication(applicationDetail);

//					02-05-2025
					ErpEstimateAmountData erpDataDB = estimateAmountRepository
							.findById(applicationDetail.getErpWorkFlowNumber())
							.orElseThrow(() -> new ErpEstimateAmountException(
									new Response(HttpCode.NULL_OBJECT, "Erp data not found in table")));
					log.info("ERP data fetched | workflowNo: {} | securityDeposit: {}",
							applicationDetail.getErpWorkFlowNumber(), securityDeposit);
					securityDeposit = erpDataDB.getSecurityDeposit() != null ? erpDataDB.getSecurityDeposit()
							: BigDecimal.ZERO;

					if (!applicationDetail.getSchemeType().getSchemeTypeId().equals(3l)) {
						if (applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() == 2L
								&& Objects.nonNull(erpDataDB.getSspTotalAmount())
								&& erpDataDB.getSspTotalAmount().compareTo(BigDecimal.ZERO) >= 0) {
							log.info(
									"NSC Application detected and SSP Total Amount >0 | applicationNo: {}  | sspTotalAmount: {}",
									applicationDetail.getConsumerApplicationNo(), erpDataDB.getSspTotalAmount());
							log.info("Calling sendDataToSSp1 | applicationNo: {}",
									applicationDetail.getConsumerApplicationNo());
							String sendDataToSSp = sendDataToSSp1(applicationDetail);
							System.err.println("sendDataToSSp : " + sendDataToSSp);
							log.info("sendDataToSSp1 response | applicationNo: {} | response: {}",
									applicationDetail.getConsumerApplicationNo(), sendDataToSSp);
							if ("Success".equals(sendDataToSSp)) {
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								String formattedDate = LocalDateTime.now().format(formatter);
								applicationDetail.setSspApplicationCompleted("true");
								applicationDetail.setSspApplicationCompleteDate(formattedDate);
								consumerApplictionDetailRepository.save(applicationDetail);
							} else {
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								String formattedDate = LocalDateTime.now().format(formatter);
								applicationDetail.setSspApplicationCompleteDate(formattedDate);
								applicationDetail.setSspApplicationCompleted(sendDataToSSp);
								consumerApplictionDetailRepository.save(applicationDetail);
							}
							log.info(
									"Application detail saved successfully | applicationNo: {} | applicationDetails: {}",
									applicationDetail.getConsumerApplicationNo(), new Gson().toJson(applicationDetail));
						} else {
//					02-05-2025 added the above check for oyt application
							if (applicationDetail.getSspApplicationCompleteDate() == null
									&& securityDeposit.compareTo(BigDecimal.ZERO) <= 0
									&& !applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId().equals(5l)) {
								log.info(
										"NSC Application detected and securityDepositAmount <=0 | applicationNo: {}  | securityDepositAmount: {}",
										applicationDetail.getConsumerApplicationNo(), securityDeposit);
//				String sendDataToSSp = sendDataToSSp(saveConsumerApplicationDB.getNscApplicationNo(), 32L,
//								applicationDetail.getConsumerApplicationNo());

//						//		given line is new code 19-05-2025 commented due to production early deployment
								log.info(
										"Calling sendDataToSSp1 for NSC application & security deposit amount <=0 | applicationNo: {}",
										applicationDetail.getConsumerApplicationNo());
								String sendDataToSSp = sendDataToSSp1(applicationDetail);
								System.err.println("sendDataToSSp : " + sendDataToSSp);
								log.info("sendDataToSSp1 response | applicationNo: {} | response: {}",
										applicationDetail.getConsumerApplicationNo(), sendDataToSSp);
//				end
								if ("Success".equals(sendDataToSSp)) {
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
									String formattedDate = LocalDateTime.now().format(formatter);
									applicationDetail.setSspApplicationCompleted("true");
									applicationDetail.setSspApplicationCompleteDate(formattedDate);
									consumerApplictionDetailRepository.save(applicationDetail);
								} else {
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
									String formattedDate = LocalDateTime.now().format(formatter);
									applicationDetail.setSspApplicationCompleteDate(formattedDate);
									applicationDetail.setSspApplicationCompleted(sendDataToSSp);
									consumerApplictionDetailRepository.save(applicationDetail);
								}
								log.info(
										"Application detail saved successfully | applicationNo: {} | applicationDetails: {}",
										applicationDetail.getConsumerApplicationNo(),
										new Gson().toJson(applicationDetail));
							}
						}
					}
				} else {
					log.warn("Application moved for re-confirmation by DGM/STC | applicationNo: {}",
							applicationDetail.getConsumerApplicationNo());
					applicationStatus = applicationStatusService.findById(
							ApplicationStatusEnum.PENDING_FOR_CONFIRMATION_OF_WORK_COMPLETION_BY_DGM_STC.getId());
					applicationDetail.setDateOfDgmOandM(workCompletionChangStautsByDgmOAndM.getDateOfDgmOandM());
					applicationDetail.setApplicationStatus(applicationStatus);
					consumerApplicationDetailService.saveConsumerApplication(applicationDetail);
				}
			}
			if (applicationDetail.getNscApplicationNo() == null) {
				if (workCompletionChangStautsByDgmOAndM.getWorkComplationChangedByDgmOrendum()
						.equalsIgnoreCase("false")) {
					log.info("workComplationChangedByDgmOrendum value: {} | NscApplicationNo:{} ",
							workCompletionChangStautsByDgmOAndM.getWorkComplationChangedByDgmOrendum(),
							applicationDetail.getNscApplicationNo());
					if (applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() == 2L
							|| applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() == 8L) {
						log.info("Application status updated | applicationNo: {} | statusId: {} | natureOfWorkId: {}",
								applicationDetail.getConsumerApplicationNo(),
								applicationStatus.getApplicationStatusId(),
								applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId());
						applicationStatus = applicationStatusService.findById(
								ApplicationStatusEnum.WORK_FINISHED_NOW_PENDING_FOR_CONNECTION_PUNCHING.getId());
					} else if (applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() == 5L) { // added this
																										// check for oyt
																										// 18-dec
						log.info("Application status updated | applicationNo: {} | statusId: {} | natureOfWorkId: {}",
								applicationDetail.getConsumerApplicationNo(),
								applicationStatus.getApplicationStatusId(),
								applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId());
						applicationStatus = applicationStatusService.findById(applicationDetail.getIvrsNo() == null
								? ApplicationStatusEnum.WORK_FINISHED_NOW_PENDING_FOR_CONNECTION_PUNCHING.getId()
								: ApplicationStatusEnum.CONNECTION_GRANTED_SUCCESSFULLY.getId());
					} else { // added this check for oyt 18-dec
						log.info("Application status updated | applicationNo: {} | statusId: {} | natureOfWorkId: {}",
								applicationDetail.getConsumerApplicationNo(),
								applicationStatus.getApplicationStatusId(),
								applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId());
						applicationStatus = applicationStatusService
								.findById(ApplicationStatusEnum.WORK_FINISED.getId());
					}
					applicationDetail.setDateOfDgmOandM(workCompletionChangStautsByDgmOAndM.getDateOfDgmOandM());
					applicationDetail.setApplicationStatus(applicationStatus);
					consumerApplicationDetailService.saveConsumerApplication(applicationDetail);
				} else {
					log.info("Application status updated | applicationNo: {} | statusId: {} | natureOfWorkId: {}",
							applicationDetail.getConsumerApplicationNo(), applicationStatus.getApplicationStatusId(),
							applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId());
					applicationStatus = applicationStatusService.findById(
							ApplicationStatusEnum.PENDING_FOR_CONFIRMATION_OF_WORK_COMPLETION_BY_DGM_STC.getId());
					applicationDetail.setDateOfDgmOandM(workCompletionChangStautsByDgmOAndM.getDateOfDgmOandM());
					applicationDetail.setApplicationStatus(applicationStatus);
					consumerApplicationDetailService.saveConsumerApplication(applicationDetail);
				}
			}
		} catch (ConsumerApplicationDetailException e) {
			log.error("Error occurred in saveAndChange | applicationId: {}",
					workCompletionChangStautsByDgmOAndM.getConsumerApplicationId(), e);
			e.printStackTrace();
		}finally {
			MDC.clear();
		}
		log.info("[END] saveAndChange completed successfully | consumerApplicationId: {}",
				workCompletionChangStautsByDgmOAndM.getConsumerApplicationId());
		return workCompletionChangStautsByDgmOAndMService.save(workCompletionChangStautsByDgmOAndM);
	}

//	public String sendDataToSSp(String nscApplicationNo, Long applicationStatusId, String consumerApplicationNo) {
//		Response response = new Response();
//
//		String url = postNscOytSSP;
////		String url = "https://survey.mpcz.in:8080/ssp-web/department/post/work/status/ssp";
//		System.err.println("url : " + url);
//		RestTemplate restTemplate = new RestTemplate();
//
//		Map<String, Object> map = new HashMap<>();
//		map.put("nscApplicationNo", nscApplicationNo);
//		map.put("workStatus", applicationStatusId);
//		map.put("dspApplicationNo", consumerApplicationNo);
//		map.put("applicationType", "NSC");
//		System.out.println("map Data : " + map.toString());
//
//		try {
//			ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, map, String.class);
//			System.out.println(postForEntity.getBody() + "aadadddddddddddddd");
//			JSONObject responseJson = new JSONObject(postForEntity.getBody());
//
//			// Extract the message and statusCode
//			String message = responseJson.getString("message");
//			int statusCode = responseJson.getInt("statusCode");
//
//			// Check if statusCode is 208 and handle accordingly
//			if (statusCode == 208) {
//				System.out.println("Work Status already updated: " + message);
//				return "Work Status already updated In Saral Sanyojan Portal !!!";
//			} else if (statusCode == 200) {
//				System.out.println("Request was successful.");
//				return "Success";
//			} else {
//				System.out.println("Request failed with status: " + statusCode);
//				return "Request failed";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "Internal Server Error";
//		}
//
//	}

	public String sendDataToSSp1(ConsumerApplicationDetail consumerApplicationDetail) {

		log.info("[START] sendDataToSSp1 in WorkCompletionChangStautsByDgmOAndMServiceIMp| consumerApplicationNo: {}",
				consumerApplicationDetail.getConsumerApplicationNo());
		Response response = new Response();

		log.info("[START] sendDataToSSp1 | applicationNo: {}", consumerApplicationDetail.getConsumerApplicationNo());
		String url = postNscOytSSP;
		log.info("SSP API URL: {} | applicationNo: {}", url, consumerApplicationDetail.getConsumerApplicationNo());
		RestTemplate restTemplate = new RestTemplate();

		Map<String, Object> map = new HashMap<>();
		map.put("nscApplicationNo", consumerApplicationDetail.getNscApplicationNo());

		Long workStatus = (consumerApplicationDetail.getApplicationStatus().getApplicationStatusId() == 25L) ? 32L
				: consumerApplicationDetail.getApplicationStatus().getApplicationStatusId();
		log.info("Calculated workStatus: {} | applicationNo: {}", workStatus,
				consumerApplicationDetail.getConsumerApplicationNo());
		map.put("workStatus", workStatus);
//		code added for sending the data to ssp for cancel application also 10-10-2025
		if (workStatus == 29L) {
			log.warn("Rejected application flow detected | applicationNo: {}",
					consumerApplicationDetail.getConsumerApplicationNo());
			map.put("message", "Application Rejected Before Demand Payment on DSP");
		} else if (workStatus == 42L) {
			log.warn("Refunded application flow detected | applicationNo: {}",
					consumerApplicationDetail.getConsumerApplicationNo());
			map.put("message", "Amount Refunded To Applicant Successfully. So Application can not move forward");
		} else {
			map.put("message", "Work Completed On DSP");
		}
//		code added for sending the data to ssp for cancel application also 10-10-2025
		map.put("dspApplicationNo", consumerApplicationDetail.getConsumerApplicationNo());
		String applicationType = (consumerApplicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() == 7L) ? "LC"
				: "NSC";
		map.put("applicationType", applicationType);
		log.info("Application type: {} | applicationNo: {}", applicationType,
				consumerApplicationDetail.getConsumerApplicationNo());
		System.err.println("ssp data sending before payment : " + new Gson().toJson(map));
//		Added this code for sending the data of SSP application for which payment is taken in DSP Portal 11-09-2025 start
//		|| consumerApplicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() == 5L ANKIT SIR SE puch kr ise bhi add krna hai 07-11-2025
		if ((consumerApplicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() == 2L)
				&& Objects.nonNull(consumerApplicationDetail.getSspTotalAmount()) && workStatus != 29L
				&& workStatus != 42L) {
			log.info("Fetching payment details for SSP | applicationNo: {}",
					consumerApplicationDetail.getConsumerApplicationNo());
			String transactionDate;
			String transactionId;
			BillDeskPaymentResponse checkDemandFeesExistOrNot = billPaymentResponseeeeeeeRepository
					.checkDemandFeesExistOrNot(consumerApplicationDetail.getConsumerApplicationNo());
			if (Objects.isNull(checkDemandFeesExistOrNot)) {
				log.info("BillDesk payment not found, checking POS data | applicationNo: {}",
						consumerApplicationDetail.getConsumerApplicationNo());
				PoseMachinePostData dataByApplicationNumber = poseMachinePostDataRepository.findDataByApplicationNumber(
						consumerApplicationDetail.getConsumerApplicationNo(), "Demand_fees");
				transactionDate = getPosPaymentDate(dataByApplicationNumber.getDateOfPayment());
				transactionId = dataByApplicationNumber.getMrNo();

			} else {
				log.info("BillDesk payment found | applicationNo: {}",
						consumerApplicationDetail.getConsumerApplicationNo());
				transactionDate = checkDemandFeesExistOrNot.getTransactionDate();
				transactionId = checkDemandFeesExistOrNot.getTranId();

			}

			if (Objects.nonNull(transactionDate) && Objects.nonNull(transactionId)) {
				log.info("Payment details added in SSP payload | transactionId: {} | applicationNo: {}", transactionId,
						consumerApplicationDetail.getConsumerApplicationNo());
				map.put("dateOfPayment", transactionDate);
				map.put("tranasactionNumber", transactionId);
			}
		}

//		Added this code for sending the data of SSP application for which payment is taken in DSP Portal 11-09-2025 end
		try {
			log.info("Sending request to SSP | applicationNo: {} | payload: {}",
					consumerApplicationDetail.getConsumerApplicationNo(), new Gson().toJson(map));
			ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, map, String.class);

			JSONObject responseJson = new JSONObject(postForEntity.getBody());
			log.info("Response received from SSP | applicationNo: {} | response: {}",
					consumerApplicationDetail.getConsumerApplicationNo(), postForEntity.getBody());
			// Extract the message and statusCode
			String message = responseJson.getString("message");
			int statusCode = responseJson.getInt("statusCode");
			log.info("Parsed SSP response | statusCode: {} | message: {} | applicationNo: {}", statusCode, message,
					consumerApplicationDetail.getConsumerApplicationNo());
			if (statusCode == 208) {

				log.warn("Work status already updated in SSP | applicationNo: {}",
						consumerApplicationDetail.getConsumerApplicationNo());
				return "Work Status already updated In Saral Sanyojan Portal !!!";
			} else if (statusCode == 200) {

				log.info("[SUCCESS] SSP API call successful | applicationNo: {}",
						consumerApplicationDetail.getConsumerApplicationNo());
				return "Success";
			} else {
				log.error("Unexpected SSP response | statusCode: {} | message: {} | applicationNo: {}", statusCode,
						message, consumerApplicationDetail.getConsumerApplicationNo());

				return "Request failed";
			}
		} catch (Exception e) {
			log.error("Exception while calling SSP API | applicationNo: {}",
					consumerApplicationDetail.getConsumerApplicationNo(), e);
			return "Internal Server Error";
		}

	}

	public String rejectLoadChangeApplication(String nscApplicationNo, Long applicationStatusId,
			String consumerApplicationNo) {
		Response response = new Response();

		String url = postNscOytSSP;
//		String url = "https://survey.mpcz.in:8080/ssp-web/department/post/work/status/ssp";
		System.err.println("url : " + url);
		RestTemplate restTemplate = new RestTemplate();

		Map<String, Object> map = new HashMap<>();
		map.put("nscApplicationNo", nscApplicationNo);
		map.put("workStatus", applicationStatusId);
		map.put("dspApplicationNo", consumerApplicationNo);
		map.put("applicationType", "LC");
		System.out.println("map Data : " + map.toString());

		try {
			ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, map, String.class);
			System.out.println(postForEntity.getBody() + "aadadddddddddddddd");
			JSONObject responseJson = new JSONObject(postForEntity.getBody());

			// Extract the message and statusCode
			String message = responseJson.getString("message");
			int statusCode = responseJson.getInt("statusCode");

			// Check if statusCode is 208 and handle accordingly
			if (statusCode == 208) {
				System.out.println("Work Status already updated: " + message);
				return "Work Status already updated In Saral Sanyojan Portal !!!";
			} else if (statusCode == 200) {
				System.out.println("Request was successful.");
				return "Success";
			} else {
				System.out.println("Request failed with status: " + statusCode);
				return "Request failed";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Internal Server Error";
		}

	}

	public static String getPosPaymentDate(Date dateOfPayment) {
		String localDateTime = dateOfPayment.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+05:30"));
		return localDateTime;
	}

}
