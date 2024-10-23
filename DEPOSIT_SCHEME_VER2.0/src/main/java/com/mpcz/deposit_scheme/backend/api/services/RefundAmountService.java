package com.mpcz.deposit_scheme.backend.api.services;

import java.util.List;

import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.RefundAmount;
import com.mpcz.deposit_scheme.backend.api.exception.BillDeskPaymentResponseException;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;

public interface RefundAmountService {
	public RefundAmount saveRefundDetails(RefundAmount refundAmount) throws ConsumerApplicationDetailException;

	public RefundAmount getRefundApplicationDetails(String consumerApplicationNo);
	
	public List<RefundAmount> getAllRefundApplication();
	
	public RefundAmount saveConsumerApplicationCancel(RefundAmount refundAmount) throws ConsumerApplicationDetailException, BillDeskPaymentResponseException;

	public RefundAmount dgmApprovalForRefund(String consumerApplicationNo, boolean dgmApproval, String dgmId) throws ConsumerApplicationDetailException;

	public RefundAmount gmApprovalForRefund(String consumerApplicationNo, boolean gmApproval, String gmId) throws ConsumerApplicationDetailException;
	public List<ConsumerApplicationDetail> getAllApplicationByConsumerId(Long consumerId);
	
	public List<ConsumerApplicationDetail> getAllJeReturnAmountApp(Long consumerId);
}
