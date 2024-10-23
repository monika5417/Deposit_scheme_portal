package com.mpcz.deposit_scheme.backend.api.controller;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.BillDeskPaymentResponse;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.RefundAmount;
import com.mpcz.deposit_scheme.backend.api.dto.BilldeskPaymentRetriveProcess;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.BillDeskPaymentResponseException;
import com.mpcz.deposit_scheme.backend.api.exception.RefundAmountException;
import com.mpcz.deposit_scheme.backend.api.repository.ApplicationStatusRepository;
import com.mpcz.deposit_scheme.backend.api.repository.BillPaymentResponseeeeeeeRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.RefundAmountRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationStatusService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

@RestController
@RequestMapping("/api/user/finance_refund")
public class FinanceRefundController {

	@Value("${client.id}")
	private String clientId;

	@Value("${return.url}")
	private String returnUrl;

	@Value("${hash.key}")
	private String hashKey;

	@Value("${client.key}")
	private String clientKey;

	@Value("${create.order}")
	private String createOrder;

	@Value("${transaction.url}")
	private String transactionUrl;

	@Value("${refund.create}")
	private String refundCreate;

	@Autowired
	private ApplicationStatusRepository applicationStatusRepository;

	@Autowired
	BillPaymentResponseeeeeeeRepository billPaymentResponseeeeeeeRepository;

	@Autowired
	ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@Autowired
	ApplicationStatusService applicationStatusService;

	@Autowired
	RefundAmountRepository refundAmountRepository;

	@PostMapping("/refundDemandAmount/{consumerApplicaitonNo}")
	public Response<Object> refundDemandAmount(@PathVariable String consumerApplicaitonNo) {
		Response<Object> response = new Response<Object>();
		try {

			BillDeskPaymentResponse billDeskLatestDemand = billPaymentResponseeeeeeeRepository
					.getBillDeskLatestDemand(consumerApplicaitonNo);
			if (Objects.isNull(billDeskLatestDemand)) {
				return new Response(HttpCode.NULL_OBJECT, "Payment not found ");
			}
			BillDeskPaymentResponse billDeskPaymentResponse = billPaymentResponseeeeeeeRepository
					.findByOrderidAndTranId(billDeskLatestDemand.getOrderid(), billDeskLatestDemand.getTranId());

			if (billDeskPaymentResponse == null) {
				response.setMessage("data not found please enter a vaild order id and transaction id");
				return response;
			}
//		create refund numner
			Random random = new Random();
			int digit = random.nextInt(900);
			int digit1 = random.nextInt(90000000);

			String refundId = "REF" + digit + "D" + digit1;

			System.err.println(refundId);

			RefundAmount refundAmnt = refundAmountRepository.findByConsumerApplicationNo(consumerApplicaitonNo);
			if (Objects.isNull(refundAmnt)) {
				return new Response(HttpCode.NULL_OBJECT, "Refund Amount Request Application Not Found in Database");
			}
			JSONObject sendDataBillDesk = new JSONObject();

			sendDataBillDesk.put("transactionid", billDeskPaymentResponse.getTranId());
			sendDataBillDesk.put("orderid", billDeskPaymentResponse.getOrderid());
			sendDataBillDesk.put("mercid", clientId);
			sendDataBillDesk.put("transaction_date", billDeskPaymentResponse.getTransactionDate());
			sendDataBillDesk.put("txn_amount", billDeskPaymentResponse.getAmount());
			sendDataBillDesk.put("refund_amount", refundAmnt.getRefundAmount().abs());
			sendDataBillDesk.put("currency", "356");
			sendDataBillDesk.put("merc_refund_ref_no", refundId);

			JSONObject deviceMap = new JSONObject();
			deviceMap.put("accept_header", "text/html");
			deviceMap.put("init_channel", "internet");
			deviceMap.put("ip", "172.24.200.191");
			deviceMap.put("fingerprintid", "61b12c18b5d0cf901be34a23ca64bb19");

			sendDataBillDesk.put("device", deviceMap);

			System.err.println("sendDataBillDesk : " + new Gson().toJson(sendDataBillDesk));
			HttpHeaders headers = new HttpHeaders();

			Long time = Timestamp.valueOf(LocalDateTime.now()).getTime();
			headers.set("Content-Type", "application/jose");
			headers.set("BD-timestamp", time.toString());
			headers.set("Accept", "application/jose");
			headers.set("BD-traceid", time.toString() + "123");

			String convertObjectInString = encryptAndSignJWSWithHMAC(sendDataBillDesk.toString(), hashKey, clientKey);

			SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.mpcz.in", 8080));
			clientHttpReq.setProxy(proxy);

			RestTemplate restTemplate = new RestTemplate(clientHttpReq);

			String url = refundCreate;

			HttpEntity httpEntity = new HttpEntity<>(convertObjectInString, headers);
			System.err.println("httpEntity-->" + httpEntity);

//			ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, httpEntity, String.class);
//
//			System.err.println("postForEntity-->" + postForEntity);
//			String forObject = postForEntity.getBody();
//			System.out.println("forObject-------------" + forObject);
//			if (forObject == null && forObject.length() <= 0) {
//				response.setMessage("response body is null");
//				response.setCode("200");
//				return response;
//			}
//
//			String verifyAndDecryptJWSWithHMAC = verifyAndDecryptJWSWithHMAC(forObject,
//					hashKey);
//
//			JSONObject jobObject = new JSONObject(verifyAndDecryptJWSWithHMAC);
//			billDeskPaymentResponse.setRefundId(jobObject.getString("refundid"));
//			billDeskPaymentResponse.setAmount(jobObject.getString("refund_amount"));
//			billDeskPaymentResponse.setTxnAmount(jobObject.getString("txn_amount"));
//			billDeskPaymentResponse.setRefundDate(jobObject.getString("refund_date"));
//			billDeskPaymentResponse.setMercRefundRefNo(jobObject.getString("transactionid"));
//			billDeskPaymentResponse.setRefundStatus(jobObject.getString("refund_status"));
//			billDeskPaymentResponse.setRefundAmount(jobObject.getString("refund_amount"));

			BillDeskPaymentResponse save = billPaymentResponseeeeeeeRepository.save(billDeskPaymentResponse);

//			String consumerdetails = consumerApplicationStatusBack(findByConsumerApplicationNumber);
			if (save != null) {
				if (refundAmnt.getRefundType().equals("Cancellation_Amount")) {
					ConsumerApplicationDetail consumerApplicationDetail = consumerApplictionDetailRepository
							.findByConsumerApplicationNumber(billDeskPaymentResponse.getConsumerApplicationNo());
					consumerApplicationDetail.setApplicationStatus(applicationStatusRepository.findById(
							ApplicationStatusEnum.CANCELLATION_AMOUNT_REFUNDED_TO_APPLICANT_SUCCESSFULLY.getId())
							.get());
					consumerApplictionDetailRepository.save(consumerApplicationDetail);
				}
				response.setMessage("Amount refund successfull");
				return response;
			} else {
				response.setMessage("Refund Not Successfull");
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new Response("500", "Internal Server Error");
		}

	}

	private String encryptAndSignJWSWithHMAC(String reqStr, String secretKey, String clientid) throws JOSEException {

		JWSSigner signer = new MACSigner(secretKey);
		HashMap<String, Object> customParams = new HashMap<String, Object>();
		customParams.put("clientid", clientid);
		JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256, null, null, null, null, null, null, null, null, null,
				null, customParams, null);
		JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(reqStr)); // Apply the HMAC
		jwsObject.sign(signer);
		return jwsObject.serialize();
	}

	public String verifyAndDecryptJWSWithHMAC(String encryptedSignedMessage, String verificationKey) throws Exception {

		JWSObject jwsObject = JWSObject.parse(encryptedSignedMessage);

		String clientId = jwsObject.getHeader().getCustomParam("clientid").toString();
		System.out.println("clientId = " + clientId);
		JWSVerifier verifier = new MACVerifier(verificationKey);
		boolean isVerified = jwsObject.verify(verifier);
		System.out.println("is valid " + isVerified);
		String message = jwsObject.getPayload().toString();
		return message;
	}

	public String consumerApplicationStatusBack(ConsumerApplicationDetail consumerApplicationDetail) {

		ApplicationStatus appStatusDb = consumerApplicationDetail.getApplicationStatus();
		try {

			if (consumerApplicationDetail.getApplicationStatus().getApplicationStatusId() == 20
					|| consumerApplicationDetail.getApplicationStatus().getApplicationStatusId() == 21
					|| consumerApplicationDetail.getApplicationStatus().getApplicationStatusId() == 23) {
				if (consumerApplicationDetail.getSchemeType().getSchemeTypeName().equalsIgnoreCase("Deposit")) {
					appStatusDb = applicationStatusService
							.findById(ApplicationStatusEnum.DEMAND_PAYMENT_PENDING_BY_CONSUMER.getId());
				} else if (consumerApplicationDetail.getSchemeType().getSchemeTypeName().equalsIgnoreCase("Supervision")
						&& consumerApplicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId() != 5l) {
					appStatusDb = applicationStatusService
							.findById(ApplicationStatusEnum.DEMAND_PAYMENT_PENDING_BY_CONSUMER.getId());
				} else {
					appStatusDb = applicationStatusService
							.findById(ApplicationStatusEnum.DEMAND_PAYMENT_PENDING_BY_CONSUMER.getId());
				}
				Date currentDate = new Date();

				// Create a calendar instance and set it to the current date
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(currentDate);

				// Add three days to the current date
				calendar.add(Calendar.DAY_OF_MONTH, 3);

				// Get the new date
				Date newDate = calendar.getTime();

				// Create a formatter for the desired date format
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

				// Format the dates using the formatter
				String currentDateFormatted = formatter.format(currentDate);
				String newDateFormatted = formatter.format(newDate);

				// Display the current date and the new date
				System.out.println("Current Date: " + currentDateFormatted);
				System.out.println("New Date: " + newDateFormatted);

				if (consumerApplicationDetail.getSchemeType().getSchemeTypeName().equalsIgnoreCase("Supervision")) {
					consumerApplicationDetail.setPaymentDate(newDateFormatted);
				}
			} else {
				appStatusDb = applicationStatusService
						.findById(ApplicationStatusEnum.PENDING_FOR_REGISTRATION_FEES.getId());
			}
			consumerApplicationDetail.setApplicationStatus(appStatusDb);
			consumerApplictionDetailRepository.save(consumerApplicationDetail);

			return "data save";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
