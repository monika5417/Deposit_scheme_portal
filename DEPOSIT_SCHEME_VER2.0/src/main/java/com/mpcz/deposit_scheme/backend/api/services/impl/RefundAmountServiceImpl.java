package com.mpcz.deposit_scheme.backend.api.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.domain.BillDeskPaymentResponse;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.ErpEstimateAmountData;
import com.mpcz.deposit_scheme.backend.api.domain.ErpRev;
import com.mpcz.deposit_scheme.backend.api.domain.RefundAmount;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.BillDeskPaymentResponseException;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.repository.ApplicationStatusRepository;
import com.mpcz.deposit_scheme.backend.api.repository.BillPaymentResponseeeeeeeRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ErpRevRepository;
import com.mpcz.deposit_scheme.backend.api.repository.EstimateAmountRepository;
import com.mpcz.deposit_scheme.backend.api.repository.RefundAmountRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.RefundAmountService;

@Service
public class RefundAmountServiceImpl implements RefundAmountService {

	@Autowired
	private ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@Autowired
	private ErpRevRepository erpRevRepository;

	@Autowired
	private RefundAmountRepository refundAmountRepository;

	@Autowired
	private ApplicationStatusRepository applicationStatusRepository;

	@Autowired
	private BillPaymentResponseeeeeeeRepository billPaymentResponseeeeeeeRepository;

	@Autowired
	private EstimateAmountRepository estimateAmountRepository;

	@Override
	public RefundAmount saveRefundDetails(RefundAmount refundAmount) throws ConsumerApplicationDetailException {

		ConsumerApplicationDetail consumerAppDetail = consumerApplictionDetailRepository
				.findByConsumerApplicationNumber(refundAmount.getConsumerApplicationNo());
		if (Objects.isNull(consumerAppDetail)) {
			throw new ConsumerApplicationDetailException(
					new Response(HttpCode.NULL_OBJECT, "Application not found in database"));
		}

		RefundAmount refund = refundAmountRepository
				.findByConsumerApplicationNo(refundAmount.getConsumerApplicationNo());
		if (!Objects.isNull(refund)) {

			throw new ConsumerApplicationDetailException(
					new Response(HttpCode.NOT_ACCEPTABLE, "Refund request already initiated for application no.: "
							+ refundAmount.getConsumerApplicationNo()));
		}
		ErpRev erpRev = erpRevRepository.findByConsAppNo(refundAmount.getConsumerApplicationNo());
		if (Objects.isNull(erpRev)) {

			throw new ConsumerApplicationDetailException(
					new Response(HttpCode.NULL_OBJECT, "Application not found in Revised Estimate database"));
		}
		if (erpRev.getPayAmt() != null && erpRev.getPayAmt().compareTo(BigDecimal.ZERO) < 0) {

			BigDecimal payAmt = erpRev.getPayAmt();
			BigDecimal remCgst = erpRev.getRemCgst();
			BigDecimal remSgst = erpRev.getRemSgst();
			System.err.println("getpayamount : " + payAmt);
			System.err.println("get Rem Cgst : " + remCgst);
			System.err.println("get Rem Sgst : " + remSgst);
			System.err.println("aaaaaaaaaaaa : " + payAmt.subtract(remCgst).subtract(remSgst));
			refundAmount.setRefundAmount(payAmt.subtract(remCgst).subtract(remSgst));

			System.out.println("Pay amount is negative.");
		} else {
			// payAmt is either null or non-negative
			System.out.println("Pay amount is either null or non-negative.");
			throw new ConsumerApplicationDetailException(
					new Response(HttpCode.NOT_ACCEPTABLE, "Revised amount is not negative"));
		}

		refundAmount.setRefundType("Revise_Amount");

		double digit = Math.random();
		double digit1 = Math.random();
		String valueOf = String.valueOf(digit);
		String valueOf1 = String.valueOf(digit1);
		String substring = valueOf.substring(2, 8);
		String substring1 = valueOf1.substring(2, 8);
		refundAmount.setRefundVoucherNo(substring + substring1);

		RefundAmount save = refundAmountRepository.save(refundAmount);
		if (save != null) {
			consumerAppDetail.setApplicationStatus(
					applicationStatusRepository.findById(ApplicationStatusEnum.PENDING_FOR_WORK_ORDER.getId()).get());
			consumerApplictionDetailRepository.save(consumerAppDetail);
		}
		return save;
	}

	@Override
	public RefundAmount getRefundApplicationDetails(String consumerApplicationNo) {
		return refundAmountRepository.findByConsumerApplicationNo(consumerApplicationNo);
	}

	@Override
	public List<RefundAmount> getAllRefundApplication() {
		return refundAmountRepository.findAll();
	}

	@Override
	public RefundAmount saveConsumerApplicationCancel(RefundAmount refundAmount)
			throws ConsumerApplicationDetailException {

		BigDecimal superVisionAmnt = new BigDecimal(0.0);
		BigDecimal cgst = new BigDecimal(0.0);
		BigDecimal sgst = new BigDecimal(0.0);
		BigDecimal remCgst = new BigDecimal(0.0);
		BigDecimal remSgst = new BigDecimal(0.0);
		BigDecimal refundAmntErp = new BigDecimal(0.0);
		BigDecimal refundAmntRev = new BigDecimal(0.0);
		BigDecimal consumerTakenAmnt = new BigDecimal(0.0);

		ConsumerApplicationDetail consumerApplicationDetail = consumerApplictionDetailRepository
				.findByConsumerApplicationNumber(refundAmount.getConsumerApplicationNo());
		if (Objects.isNull(consumerApplicationDetail)) {
			throw new ConsumerApplicationDetailException(
					new Response(HttpCode.NULL_OBJECT, "Application Not Found In Database"));
		} else {
			List<BillDeskPaymentResponse> allPaymentDetails = billPaymentResponseeeeeeeRepository
					.getAllPaymentDetails(refundAmount.getConsumerApplicationNo());
			for (BillDeskPaymentResponse bill : allPaymentDetails) {
				if (bill.getAdditionalInfo() != null) {

					if (bill.getAdditionalInfo().equals("Revised_Demand_fees")) {
						consumerTakenAmnt = new BigDecimal(bill.getAmount());
						ErpRev erpRev = erpRevRepository.findByConsAppNo(refundAmount.getConsumerApplicationNo());
						if (erpRev.getRemCgst() != null) {
							remCgst = erpRev.getRemCgst();
							remSgst = erpRev.getRemSgst();
						}
						refundAmntRev = consumerTakenAmnt.subtract(remCgst).subtract(remSgst);
					}

					if (bill.getAdditionalInfo().equals("Demand_fees")) {
						consumerTakenAmnt = new BigDecimal(bill.getAmount());
						ErpEstimateAmountData erpEstimateAmountData = estimateAmountRepository
								.findByErpNo(consumerApplicationDetail.getErpWorkFlowNumber());
						if (erpEstimateAmountData.getCgst() != null) {
							cgst = erpEstimateAmountData.getCgst();
							sgst = erpEstimateAmountData.getSgst();
						}
						refundAmntErp = consumerTakenAmnt.subtract(cgst).subtract(sgst);
					}
					refundAmount.setRefundAmount(refundAmntRev.add(refundAmntErp));
				}
			}
			refundAmount.setRefundType("Cancellation_Amount");
			double digit = Math.random();
			double digit1 = Math.random();
			String valueOf = String.valueOf(digit);
			String valueOf1 = String.valueOf(digit1);
			String substring = valueOf.substring(2, 8);
			String substring1 = valueOf1.substring(2, 8);
			refundAmount.setRefundVoucherNo(substring + substring1);
			RefundAmount save = refundAmountRepository.save(refundAmount);
			if (Objects.isNull(save)) {
				throw new ConsumerApplicationDetailException(new Response(HttpCode.NULL_OBJECT, "Data Not Saved"));
			} else {
				consumerApplicationDetail.setApplicationStatus(applicationStatusRepository
						.findById(ApplicationStatusEnum.APPLICATION_PENDING_AT_DGM_FOR_REFUND.getId()).get());
				consumerApplictionDetailRepository.save(consumerApplicationDetail);
				return save;
			}
		}

	}

	@Override
	public RefundAmount dgmApprovalForRefund(String consumerApplicationNo, boolean dgmApproval, String dgmId)
			throws ConsumerApplicationDetailException {
		RefundAmount save = null;

		ConsumerApplicationDetail consumerApplicationDetail = consumerApplictionDetailRepository
				.findByConsumerApplicationNumber(consumerApplicationNo);
		if (Objects.isNull(consumerApplicationDetail)) {
			throw new ConsumerApplicationDetailException(
					new Response(HttpCode.NULL_OBJECT, "Application Not Found In Database"));
		} else {
			RefundAmount refundAmount = refundAmountRepository.findByConsumerApplicationNo(consumerApplicationNo);
			if (Objects.isNull(refundAmount)) {
				throw new ConsumerApplicationDetailException(
						new Response(HttpCode.NULL_OBJECT, "Application Not Found Refund Table"));
			} else {
				if (dgmApproval == true) {
					refundAmount.setDgmApproval("true");
					refundAmount.setDgmApprovedId(dgmId);
					save = refundAmountRepository.save(refundAmount);
					consumerApplicationDetail.setApplicationStatus(applicationStatusRepository
							.findById(ApplicationStatusEnum.APPLICATION_PENDING_AT_GM_FOR_REFUND.getId()).get());
					consumerApplictionDetailRepository.save(consumerApplicationDetail);
				} else {
					refundAmount.setDgmApproval("false");
					refundAmount.setDgmApprovedId(dgmId);
					save = refundAmountRepository.save(refundAmount);
				}

				return save;
			}
		}

	}

	
	@Override
	public RefundAmount gmApprovalForRefund(String consumerApplicationNo, boolean gmApproval, String gmId)
			throws ConsumerApplicationDetailException {
		RefundAmount save = null;

		ConsumerApplicationDetail consumerApplicationDetail = consumerApplictionDetailRepository
				.findByConsumerApplicationNumber(consumerApplicationNo);
		if (Objects.isNull(consumerApplicationDetail)) {
			throw new ConsumerApplicationDetailException(
					new Response(HttpCode.NULL_OBJECT, "Application Not Found In Database"));
		} else {
			RefundAmount refundAmount = refundAmountRepository.findByConsumerApplicationNo(consumerApplicationNo);
			if (Objects.isNull(refundAmount)) {
				throw new ConsumerApplicationDetailException(
						new Response(HttpCode.NULL_OBJECT, "Application Not Found Refund Table"));
			} else {
				if (gmApproval == true) {
					refundAmount.setGmApproval("true");
					refundAmount.setGmApprovedId(gmId);
					save = refundAmountRepository.save(refundAmount);
					consumerApplicationDetail.setApplicationStatus(applicationStatusRepository
							.findById(ApplicationStatusEnum.APPLICATION_PENDING_AT_FINANCE_AO_FOR_REFUND.getId()).get());
					consumerApplictionDetailRepository.save(consumerApplicationDetail);
				} else {
					refundAmount.setGmApproval("false");
					refundAmount.setGmApprovedId(gmId);
					save = refundAmountRepository.save(refundAmount);
				}

				return save;
			}
		}

	}
	
	
	@Override
	public List<ConsumerApplicationDetail> getAllApplicationByConsumerId(Long consumerId){
		return consumerApplictionDetailRepository.findByConsumerId(consumerId);
	}
	
	@Override
	public List<ConsumerApplicationDetail> getAllJeReturnAmountApp(Long consumerId){
		return consumerApplictionDetailRepository.findAllJeReturnAmountApp(consumerId);
	}
	
}
