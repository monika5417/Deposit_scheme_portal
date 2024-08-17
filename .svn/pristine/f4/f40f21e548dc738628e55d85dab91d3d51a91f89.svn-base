package com.mpcz.deposit_scheme.backend.api.services.impl;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mpcz.deposit_scheme.backend.api.controller.ConsumerApplicationDcCorrectionController;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.ErpEstimateAmountData;
import com.mpcz.deposit_scheme.backend.api.domain.MmkyPayAmount;
import com.mpcz.deposit_scheme.backend.api.domain.PoseMachinePostData;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.exception.TransactionAmountException;
import com.mpcz.deposit_scheme.backend.api.repository.ApplicationStatusRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.EstimateAmountRepository;
import com.mpcz.deposit_scheme.backend.api.repository.MmkyPayAmountRespository;
import com.mpcz.deposit_scheme.backend.api.repository.PoseMachinePostDataRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationStatusService;
import com.mpcz.deposit_scheme.backend.api.services.PoseMachinePostDataService;
import com.mpcz.deposit_scheme.projection.CadProjection;

@Service
public class PoseMachinePostDataServiceIMP implements PoseMachinePostDataService {

	@Autowired
	private ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@Autowired
	private PoseMachinePostDataRepository poseMachinePostDataRepository;

	@Autowired
	private ApplicationStatusRepository applicationStatusRepository;

	@Autowired
	private ApplicationStatusService applicationStatusService;
	
	@Autowired
	MmkyPayAmountRespository mmkyPayAmountRespository;
	
	@Autowired
	EstimateAmountRepository estimateAmountRepository;

	@Override
	public String save(PoseMachinePostData pose)   {

		Response<?> response = new Response<>();
		PoseMachinePostData poseMachinePostData = new PoseMachinePostData();
		String conAppNo = pose.getApplicationNumber();
		BigDecimal txnAmount = pose.getTxnAmount();

		ApplicationStatus appStatusDb = null;

	
		try {
			ConsumerApplicationDetail findByConsumerApplicationNumber = consumerApplictionDetailRepository
					.findByConsumerApplicationNumber(conAppNo);

			if (findByConsumerApplicationNumber == null) {
				response.setCode("404");
				response.setMessage("application not found");
				throw new TransactionAmountException(response);
			}
			poseMachinePostData.setApplicationNumber(conAppNo);
			poseMachinePostData.setBankName(pose.getBankName());
			poseMachinePostData.setCashAcceptedDate(pose.getCashAcceptedDate());
			poseMachinePostData.setDateOfPayment(pose.getDateOfPayment());
			poseMachinePostData.setDeviceId(pose.getDeviceId());
			poseMachinePostData.setInstrumentDate(pose.getInstrumentDate());
			poseMachinePostData.setInstrumentNo(pose.getInstrumentNo());
			poseMachinePostData.setLocationCode(pose.getLocationCode());
			poseMachinePostData.setMrNo(pose.getMrNo());
			poseMachinePostData.setPaymentMode(pose.getPaymentMode());
			poseMachinePostData.setTxnAmount(pose.getTxnAmount());

			PoseMachinePostData save = poseMachinePostDataRepository.save(poseMachinePostData);
			if (save != null) {
				if (findByConsumerApplicationNumber.getApplicationStatus().getApplicationStatusId() == 5l) {

					appStatusDb = applicationStatusService
							.findById(ApplicationStatusEnum.ACCEPTANCE_OF_APPLICATION_AT_DC.getId());
				} else if (findByConsumerApplicationNumber.getApplicationStatus().getApplicationStatusId() == 12l) {
					if (findByConsumerApplicationNumber.getSchemeType().getSchemeTypeName().equalsIgnoreCase("Deposit")
							|| findByConsumerApplicationNumber.getNatureOfWorkType().getNatureOfWorkTypeId()
									.equals(8L)) {
						appStatusDb = applicationStatusService
								.findById(ApplicationStatusEnum.PENDING_FOR_WORK_ORDER.getId());
					} else if (findByConsumerApplicationNumber.getSchemeType().getSchemeTypeName()
							.equalsIgnoreCase("Supervision")
							&& findByConsumerApplicationNumber.getNatureOfWorkType().getNatureOfWorkTypeId() != 5l) {
						appStatusDb = applicationStatusService
								.findById(ApplicationStatusEnum.PENDING_FOR_SELECTING_CONTRACTOR.getId());
					} else {
						appStatusDb = applicationStatusService
								.findById(ApplicationStatusEnum.WAITING_FOR_72_HOURS.getId());
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

					if (!findByConsumerApplicationNumber.getSchemeType().getSchemeTypeName()
							.equalsIgnoreCase("Supervision")) {
						findByConsumerApplicationNumber.setPaymentDate(newDateFormatted);
					}
				}

				else {
					appStatusDb = applicationStatusService
							.findById(ApplicationStatusEnum.PENDING_FOR_REGISTRATION_FEES.getId());
				}

			} else {
				if (findByConsumerApplicationNumber.getApplicationStatus().getApplicationStatusId() == 5l) {
					appStatusDb = applicationStatusService
							.findById(ApplicationStatusEnum.PENDING_FOR_REGISTRATION_FEES.getId());
				} else {
					appStatusDb = applicationStatusService
							.findById(ApplicationStatusEnum.DEMAND_PAYMENT_PENDING_BY_CONSUMER.getId());
				}
			}

			findByConsumerApplicationNumber.setApplicationStatus(appStatusDb);
			ConsumerApplicationDetail save2 = consumerApplictionDetailRepository.save(findByConsumerApplicationNumber);
			if (save2 != null) {
				
			//	ConsumerApplicationDetail findByConsumerApplicationNumber1 = consumerApplictionDetailRepository.findByConsumerApplicationNumber(bill.getApplicationNumber());
//				ErpEstimateAmountData findById = estimateAmountRepository.findById(findByConsumerApplicationNumber.getErpWorkFlowNumber()).get();

				Map<String, String> requestBody = new HashMap();
			
				if (findByConsumerApplicationNumber.getNatureOfWorkType().getNatureOfWorkTypeId() == 8L) {
					MmkyPayAmount findByConsumer = mmkyPayAmountRespository.findByConsumerApplicationNumber(
							findByConsumerApplicationNumber.getConsumerApplicationNo());
					requestBody.put("consumerApplicationNo",
							findByConsumerApplicationNumber.getConsumerApplicationNo());
					requestBody.put("schema", findByConsumer.getSchemeCode());
				} else {

					Optional<ErpEstimateAmountData> findById1 = estimateAmountRepository
							.findById(findByConsumerApplicationNumber.getErpWorkFlowNumber());
					if (findById1.isPresent()) {
						ErpEstimateAmountData erpEstimateAmountData = findById1.get();
						String kwLoad = String.valueOf(erpEstimateAmountData.getKwLoad());
						requestBody.put("kwload", kwLoad);

						String kvaLoad = String.valueOf(erpEstimateAmountData.getKvaLoad());
						requestBody.put("kvaload", kvaLoad);

						String depositAmount = String.valueOf(erpEstimateAmountData.getDepositAmount());
						requestBody.put("deposit_amount", depositAmount);

						String superVisionAmt = String.valueOf(erpEstimateAmountData.getSupervisionAmount());
						requestBody.put("supervision_amount", superVisionAmt);

						String sgst = String.valueOf(erpEstimateAmountData.getSgst());
						requestBody.put("sgst", sgst);

						String cgst = String.valueOf(erpEstimateAmountData.getCgst());
						requestBody.put("cgst", cgst);

						requestBody.put("estimate_name", erpEstimateAmountData.getEstimateName());
						requestBody.put("schema", erpEstimateAmountData.getSchemeCode());

					}
				}

				requestBody.put("consumer_mobile_no",
						findByConsumerApplicationNumber.getConsumers().getConsumerMobileNo());
				requestBody.put("consumerApplicationNo",
						findByConsumerApplicationNumber.getConsumerApplicationNo());
				requestBody.put("consumer_email_id",
						findByConsumerApplicationNumber.getConsumers().getConsumerEmailId());

				requestBody.put("address", findByConsumerApplicationNumber.getAddress());

				requestBody.put("shortDescriptionOfWork",
						findByConsumerApplicationNumber.getShortDescriptionOfWork());
				requestBody.put("erp_no", findByConsumerApplicationNumber.getErpWorkFlowNumber());
				requestBody.put("consumerName", findByConsumerApplicationNumber.getConsumerName());
				requestBody.put("is_bid_submitted", "false");

				RestTemplate restTemplate = new RestTemplate();

//	        Production Code
				try {
				ResponseEntity<Map> postForEntity = restTemplate
						.postForEntity("https://qcportal.mpcz.in/tkc/get_consumer/", requestBody, Map.class);
				System.out.println("The result of Post api is :" + postForEntity.getBody());
				}catch(Exception e){
					e.printStackTrace();
				}
				return "data update succefully";
			} else {
				return "data not update";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "data not update ";
	}

}
