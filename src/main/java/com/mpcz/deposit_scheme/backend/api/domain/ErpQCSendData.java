/**
 * 
 */
package com.mpcz.deposit_scheme.backend.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Monika Rajpoot
 * @since 27-Jan-2026
 * @description ErpQCSendData.java class description
 */


@Entity
@Table(name = "ERP_QC_SEND_DATA")
public class ErpQCSendData extends Auditable<Long>{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ERP_QC_SEND_DATA_SEQ", sequenceName = "ERP_QC_SEND_DATA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ERP_QC_SEND_DATA_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name="RESOURCE_ITEM")
	private String resourceItem;
	
	@Column(name="ITEM_QTY")
	private Long itemQty;
	
	@Column(name="ERP_NO")
	private String erpNo;
	
	@Column(name="PROJECT_ID")
	private String projectId;
	
	@Column(name="CONSUMER_APPLICATION_NO")
	private String consumerApplicationNo;
	
	@Column(name="VERSION")
	private Long Version;
	
	@Column(name="TESTING_STATUS")
	private String testingStatus;
	
	@Column(name="SCHEDULE_NO")
	private String scheduleNo;
	
	
	
	
	
	public String getScheduleNo() {
		return scheduleNo;
	}
	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getResourceItem() {
		return resourceItem;
	}
	public void setResourceItem(String resourceItem) {
		this.resourceItem = resourceItem;
	}
	public Long getItemQty() {
		return itemQty;
	}
	public void setItemQty(Long itemQty) {
		this.itemQty = itemQty;
	}
	public String getErpNo() {
		return erpNo;
	}
	public void setErpNo(String erpNo) {
		this.erpNo = erpNo;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getConsumerApplicationNo() {
		return consumerApplicationNo;
	}
	public void setConsumerApplicationNo(String consumerApplicationNo) {
		this.consumerApplicationNo = consumerApplicationNo;
	}
	public Long getVersion() {
		return Version;
	}
	public void setVersion(Long version) {
		Version = version;
	}
	public String getTestingStatus() {
		return testingStatus;
	}
	public void setTestingStatus(String testingStatus) {
		this.testingStatus = testingStatus;
	}
	
	
	
	
	
}
