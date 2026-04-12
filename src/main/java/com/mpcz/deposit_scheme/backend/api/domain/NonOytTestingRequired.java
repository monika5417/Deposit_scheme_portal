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
 * @since 10-Feb-2026
 * @description NonOytTestingRequired.java class description
 */

@Entity
@Table(name="NON_OYT_TESTING_REQ")
public class NonOytTestingRequired {
	
	@Id
	@SequenceGenerator(name = "NON_OYT_TESTING_REQ_SEQ", sequenceName = "NON_OYT_TESTING_REQ_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NON_OYT_TESTING_REQ_SEQ")
	@Column(name = "ID", columnDefinition = "serial", updatable = false)
	private Long id;
	
	@Column(name="CONSUMER_APPLICATION_NO")
	private String consumerApplicationNo;
	
	@Column(name="PROJECT_NUMBER")
	private String projectNo;
	
	@Column(name="ITEM_CODE")
	private String itemCode;
	
	@Column(name="ITEM_DESC")
	private String itemDesc;
	
	@Column(name="ITEM_UOM")
	private String itemUom;
	
	@Column(name="QTY")
	private Long qty;
	
	@Column(name="TEST")
	private String test;
	
	@Column(name="MATERIAL_CERTIFICATE_NAME")
	private String materialCertificateName;

	
	
	
	
	public String getMaterialCertificateName() {
		return materialCertificateName;
	}

	public void setMaterialCertificateName(String materialCertificateName) {
		this.materialCertificateName = materialCertificateName;
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

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getItemUom() {
		return itemUom;
	}

	public void setItemUom(String itemUom) {
		this.itemUom = itemUom;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
	
	

}
