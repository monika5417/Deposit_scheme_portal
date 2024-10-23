package com.mpcz.deposit_scheme.backend.api.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="REFUND_AMOUNT")
@Entity
public class RefundAmount extends Auditable<Long>{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "REFUND_AMOUNT_SEQ", sequenceName = "REFUND_AMOUNT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFUND_AMOUNT_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name="CONSUMER_APPLICATION_NO")
	private String consumerApplicationNo;
	
	@Column(name="DGM_RAISED_REQ_NAME")
	private String dgmRaisedRequestName;
	
	@Column(name="DGM_ID_RAISED_REQ")
	private String dgmIdRaisedRequest;
	
	@Column(name="REFUND_VOUCHER_NO")
	private String refundVoucherNo;
	
	@Column(name="REFUND_AMOUNT")
	private BigDecimal refundAmount;
	
	@Column(name="REFUND_TYPE")
	private String refundType;
	
	@Column(name="CONSUMER_NAME")
	private String consumerName;
	
	@Column(name="DGM_APPROVED_ID")
	private String dgmApprovedId; 
	
	@Column(name="DGM_APPROAL")
	private String dgmApproval;
	
	@Column(name="GM_APPROVED_ID")
	private String gmApprovedId;
	
	@Column(name="DGM_APPROVAL")
	private String gmApproval;
	
	
	public String getDgmRaisedRequestName() {
		return dgmRaisedRequestName;
	}
	public void setDgmRaisedRequestName(String dgmRaisedRequestName) {
		this.dgmRaisedRequestName = dgmRaisedRequestName;
	}
	public String getDgmIdRaisedRequest() {
		return dgmIdRaisedRequest;
	}
	public void setDgmIdRaisedRequest(String dgmIdRaisedRequest) {
		this.dgmIdRaisedRequest = dgmIdRaisedRequest;
	}
	public String getDgmApprovedId() {
		return dgmApprovedId;
	}
	public void setDgmApprovedId(String dgmApprovedId) {
		this.dgmApprovedId = dgmApprovedId;
	}
	public String getDgmApproval() {
		return dgmApproval;
	}
	public void setDgmApproval(String dgmApproval) {
		this.dgmApproval = dgmApproval;
	}
	public String getGmApprovedId() {
		return gmApprovedId;
	}
	public void setGmApprovedId(String gmApprovedId) {
		this.gmApprovedId = gmApprovedId;
	}
	public String getGmApproval() {
		return gmApproval;
	}
	public void setGmApproval(String gmApproval) {
		this.gmApproval = gmApproval;
	}
	public String getConsumerName() {
		return consumerName;
	}
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getConsumerApplicationNo() {
		return consumerApplicationNo;
	}
	public void setConsumerApplicationNo(String consumerApplicationNo) {
		this.consumerApplicationNo = consumerApplicationNo;
	}
	
	public String getRefundVoucherNo() {
		return refundVoucherNo;
	}
	public void setRefundVoucherNo(String refundVoucherNo) {
		this.refundVoucherNo = refundVoucherNo;
	}
	
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	
	
	
	
	

	
}
