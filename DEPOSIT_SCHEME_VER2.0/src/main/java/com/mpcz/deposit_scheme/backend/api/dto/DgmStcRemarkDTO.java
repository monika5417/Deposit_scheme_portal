package com.mpcz.deposit_scheme.backend.api.dto;

import java.util.List;

import com.mpcz.deposit_scheme.backend.api.domain.DgmStcReason;

public class DgmStcRemarkDTO {

    
    private String consumerAppNo;
    private String remark;
    
    private String stcName;
    private long stcId;
    private long remarkCount;

    private List<DgmStcReason> reasons;
    
    
    // Constructors, getters, and setters

   
	public DgmStcRemarkDTO() {
        // Default constructor
    }

 
	public List<DgmStcReason> getReasons() {
		return reasons;
	}


	public void setReasons(List<DgmStcReason> reasons) {
		this.reasons = reasons;
	}






	public DgmStcRemarkDTO( String consumerAppNo, String remark,  String stcName, long stcId, long remarkCount) {
        
        this.consumerAppNo = consumerAppNo;
        this.remark = remark;
        
        this.stcName = stcName;
        this.stcId = stcId;
        this.remarkCount = remarkCount;
    }

    // Getters and Setters for all fields

  

    public String getConsumerAppNo() {
        return consumerAppNo;
    }

    public void setConsumerAppNo(String consumerAppNo) {
        this.consumerAppNo = consumerAppNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

   
    public String getStcName() {
        return stcName;
    }

    public void setStcName(String stcName) {
        this.stcName = stcName;
    }

    public long getStcId() {
        return stcId;
    }

    public void setStcId(long stcId) {
        this.stcId = stcId;
    }

    public long getRemarkCount() {
        return remarkCount;
    }

    public void setRemarkCount(long remarkCount) {
        this.remarkCount = remarkCount;
    }
}
