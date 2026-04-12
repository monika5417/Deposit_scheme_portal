package com.mpcz.deposit_scheme.backend.api.domain;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
@Table(name = "PROJECT_ESTIMATE")
@Entity
public class ProjectEstimate {

    @Id
    @SequenceGenerator(name = "PROJECT_ESTIMATE_SEQ", sequenceName = "PROJECT_ESTIMATE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_ESTIMATE_SEQ")
    @Column(name = "PROJECT_ID")
    @JsonProperty("PROJECT_ID")
    private Long projectId;

    @Column(name = "PROJECT_NUMBER")
    @JsonProperty("PROJECT_NUMBER")
    private String projectNumber;

    @Column(name = "PROJECT_TYPE")
    @JsonProperty("PROJECT_TYPE")
    private String projectType;

    @Column(name = "LONG_NAME", length = 1000)
    @JsonProperty("LONG_NAME")
    private String longName;

    @Column(name = "STATUS")
    @JsonProperty("STATUS")
    private String status;

    @Column(name = "VERSION_NUMBER")
    @JsonProperty("VERSION_NUMBER")
    private Integer versionNumber;

    @Column(name = "SCHEME_CODE")
    @JsonProperty("SCHEMECODE")
    private String schemeCode;

    @Column(name = "ESTIMATE_NO")
    @JsonProperty("ESTIMATE_NO")
    private String estimateNo;

    @Column(name = "ESTIMATE_DATE")
    @JsonProperty("ESTIMATE_DATE")
    private Instant estimateDate;

    // ── Cost Fields ─────────────────────────────

    @Column(name = "ESTIMATED_COST", precision = 15, scale = 2)
    @JsonProperty("ESTIMATED_COST")
    private BigDecimal estimatedCost;

    @Column(name = "SANCT_COST", precision = 15, scale = 2)
    @JsonProperty("SANCT_COST")
    private BigDecimal sanctCost;

    @Column(name = "MINUS_COST", precision = 15, scale = 2)
    @JsonProperty("MINUS_COST")
    private BigDecimal minusCost;

    @Column(name = "DISMANTALLING_COST", precision = 15, scale = 2)
    @JsonProperty("DISMENTALLING_COST")
    private BigDecimal dismantllingCost;

    @Column(name = "SUPERVISION_COST", precision = 15, scale = 2)
    @JsonProperty("SUPERVISION_COST")
    private BigDecimal supervisionCost;

    // ── Organization Fields ─────────────────────

    @Column(name = "ORG")
    @JsonProperty("ORG")
    private String org;

    @Column(name = "ORG1")
    @JsonProperty("ORG1")
    private String org1;

    @Column(name = "ORGANIZATION_NAME")
    @JsonProperty("ORGANIZATION_NAME")
    private String organizationName;

    // ── User Fields ─────────────────────────────

    @Column(name = "CREATED_USER_ID")
    @JsonProperty("CREATED_USER_ID")
    private String createdUserId;

    @Column(name = "CREATED_USER_NAME")
    @JsonProperty("CREATED_USER_NAME")
    private String createdUserName;

    @Column(name = "SUBMITTED_BY_NAME")
    @JsonProperty("SUBMITTED_BY_NAME")
    private String submittedByName;

    @Column(name = "APPROVED_BY_NAME")
    @JsonProperty("APPROVED_BY_NAME")
    private String approvedByName;

    @Column(name = "DESIG")
    @JsonProperty("DESIG")
    private String desig;

    @Column(name = "JOB1")
    @JsonProperty("JOB1")
    private String job1;

    // ── Constructors ──────────────────────────────────────────────

    public ProjectEstimate() {}

    public ProjectEstimate(Long projectId) {
        this.projectId = projectId;
    }

    // ── Getters & Setters ─────────────────────────────────────────

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getProjectNumber() { return projectNumber; }
    public void setProjectNumber(String projectNumber) { this.projectNumber = projectNumber; }

    public String getProjectType() { return projectType; }
    public void setProjectType(String projectType) { this.projectType = projectType; }

    public String getLongName() { return longName; }
    public void setLongName(String longName) { this.longName = longName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }

    public String getSchemeCode() { return schemeCode; }
    public void setSchemeCode(String schemeCode) { this.schemeCode = schemeCode; }

    public String getEstimateNo() { return estimateNo; }
    public void setEstimateNo(String estimateNo) { this.estimateNo = estimateNo; }

    public Instant getEstimateDate() { return estimateDate; }
    public void setEstimateDate(Instant estimateDate) { this.estimateDate = estimateDate; }

    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }

    public BigDecimal getSanctCost() { return sanctCost; }
    public void setSanctCost(BigDecimal sanctCost) { this.sanctCost = sanctCost; }

    public BigDecimal getMinusCost() { return minusCost; }
    public void setMinusCost(BigDecimal minusCost) { this.minusCost = minusCost; }

    public BigDecimal getDismantllingCost() { return dismantllingCost; }
    public void setDismantllingCost(BigDecimal dismantllingCost) { this.dismantllingCost = dismantllingCost; }

    public BigDecimal getSupervisionCost() { return supervisionCost; }
    public void setSupervisionCost(BigDecimal supervisionCost) { this.supervisionCost = supervisionCost; }

    public String getOrg() { return org; }
    public void setOrg(String org) { this.org = org; }

    public String getOrg1() { return org1; }
    public void setOrg1(String org1) { this.org1 = org1; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getCreatedUserId() { return createdUserId; }
    public void setCreatedUserId(String createdUserId) { this.createdUserId = createdUserId; }

    public String getCreatedUserName() { return createdUserName; }
    public void setCreatedUserName(String createdUserName) { this.createdUserName = createdUserName; }

    public String getSubmittedByName() { return submittedByName; }
    public void setSubmittedByName(String submittedByName) { this.submittedByName = submittedByName; }

    public String getApprovedByName() { return approvedByName; }
    public void setApprovedByName(String approvedByName) { this.approvedByName = approvedByName; }

    public String getDesig() { return desig; }
    public void setDesig(String desig) { this.desig = desig; }

    public String getJob1() { return job1; }
    public void setJob1(String job1) { this.job1 = job1; }

    @Override
    public String toString() {
        return "ProjectEstimate{" +
                "projectId=" + projectId +
                ", projectNumber='" + projectNumber + '\'' +
                ", status='" + status + '\'' +
                ", estimatedCost=" + estimatedCost +
                '}';
    }
}