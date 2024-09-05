package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerNotFoundByApplicationIdException;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerNotFoundByVendorIdException;
import com.mpcz.deposit_scheme.projection.CadProjection;
import com.mpcz.deposit_scheme.projection.ConsumerApplicationDetailPro;

@Repository
public interface ConsumerApplictionDetailRepository extends JpaRepository<ConsumerApplicationDetail, Long> {

	@Query(value = " SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
			+ " ", countQuery = "  SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad  ", nativeQuery = true)
	public Page<ConsumerApplicationDetail> findByConsumerApplicationDetailsPaginate(Pageable pageable);

	@Query(value = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
			+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
			+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
			+ "left join division div on div.div_id = sub_div.division_id "
			+ "left join circle cir  on cir.circle_id =  div.circle_id "
			+ "left join region reg  on reg.region_id =  cir.region_id "
			+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
			+ " (:discomId =0 or disc.discom_id =:discomId ) " + "and (:regionId =0 or reg.region_id =:regionId ) "
			+ "and  (:circleId =0 or cir.circle_id =:circleId ) " + "and  (:divisionId =0 or div.div_id =:divisionId ) "
			+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) " + "and  (:dcId =0 or dc.dc_id =:dcId ) "
			+ "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) " + "and  (cad.is_active= 1 ) "
			+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
			+ " ", countQuery = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
					+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
					+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
					+ "left join division div on div.div_id = sub_div.division_id "
					+ "left join circle cir  on cir.circle_id =  div.circle_id "
					+ "left join region reg  on reg.region_id =  cir.region_id "
					+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
					+ " (:discomId =0 or disc.discom_id =:discomId ) "
					+ "and (:regionId =0 or reg.region_id =:regionId ) "
					+ "and  (:circleId =0 or cir.circle_id =:circleId ) "
					+ "and  (:divisionId =0 or div.div_id =:divisionId ) "
					+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) "
					+ "and  (:dcId =0 or dc.dc_id =:dcId ) " + "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
					+ "and  (cad.is_active= 1 ) " + "ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
					+ " ", nativeQuery = true)
	public Page<ConsumerApplicationDetail> findByConsumerApplicationDetailsUserWisePaginate(
			@Param("discomId") Long discomId, @Param("regionId") Long regionId, @Param("circleId") Long circleId,
			@Param("divisionId") Long divisionId, @Param("subDivisionId") Long subDivisionId, @Param("dcId") Long dcId,
			@Param("consumerId") Long consumerId, Pageable pageable);

	@Query(value = " SELECT * from CONSUMER_APPLICATION_DETAIL where VENDOR_ID =:id and IS_ACTIVE = 1", nativeQuery = true)
	public List<ConsumerApplicationDetail> findAllConsumerApplicationsByVendorId(@Param("id") Long id)
			throws ConsumerNotFoundByVendorIdException;

	@Query(value = " SELECT * from CONSUMER_APPLICATION_DETAIL where CONSUMER_APPLICATION_ID =:consumerAppId", nativeQuery = true)
	public ConsumerApplicationDetail findConsumerApplicationDetailsByConsumerApplicationId(
			@Param("consumerAppId") Long consumerApplicationId) throws ConsumerNotFoundByApplicationIdException;

	// charitra start
	@Query(value = "SELECT * from CONSUMER_APPLICATION_DETAIL where CONSUMER_APPLICATION_NUMBER =:consumerApplicationNO and IS_ACTIVE = 1", nativeQuery = true)
	public ConsumerApplicationDetail findByConsumerApplicationNumber(String consumerApplicationNO);

	Optional<ConsumerApplicationDetail> findByConsumerApplicationId(Long consumerApplicationId);

//	@Query(value = "select Max(consumer_application_number) from  consumer_application_detail where consumer_application_number like :codeFormat", nativeQuery = true)
//	public String findMaxApplicationNo(@Param("codeFormat") String codeFormat);

	@Query(value = "select CONCAT('SV',To_CHAR(MAX(TO_NUMBER(SUBSTR(consumer_application_number,3,LENGTH(consumer_application_number)))))) from consumer_application_detail where consumer_application_number like :codeFormat", nativeQuery = true)
	public String findMaxApplicationNo(@Param("codeFormat") String codeFormat);

	@Query(value = "select CONCAT('DS',To_CHAR(MAX(TO_NUMBER(SUBSTR(consumer_application_number,3,LENGTH(consumer_application_number)))))) from consumer_application_detail where consumer_application_number like :codeFormat", nativeQuery = true)
	public String findMaxApplicationNo1(@Param("codeFormat") String codeFormat);

	@Query(value = "select CONCAT('DP',To_CHAR(MAX(TO_NUMBER(SUBSTR(consumer_application_number,3,LENGTH(consumer_application_number)))))) from consumer_application_detail where consumer_application_number like :codeFormat", nativeQuery = true)
	public String findMaxApplicationNo2(@Param("codeFormat") String codeFormat);

	public List<ConsumerApplicationDetail> findConsumerApplicationDetailsByDistributionCenterDcIdAndApplicationStatusApplicationStatusId(
			Long dcId, Long ApplicationStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update CONSUMER_APPLICATION_DETAIL  set DISTRICT_ID = :district  where CONSUMER_APPLICATION_NUMBER = :consumerApplicationNumber", nativeQuery = true)
	public void updatedistrict(@Param("district") Long district,
			@Param("consumerApplicationNumber") String consumerApplicationNumber);

	@Query(value = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
			+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
			+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
			+ "left join division div on div.div_id = sub_div.division_id "
			+ "left join circle cir  on cir.circle_id =  div.circle_id "
			+ "left join region reg  on reg.region_id =  cir.region_id "
			+ "left join discom disc  on disc.discom_id =  reg.discom_id "
			+ "where "
			+ " (:discomId =0 or disc.discom_id =:discomId ) " + "and (:regionId =0 or reg.region_id =:regionId ) "
			+ "and  (:circleId =0 or cir.circle_id =:circleId ) " + "and  (:divisionId =0 or div.div_id =:divisionId ) "
			+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) " + "and  (:dcId =0 or dc.dc_id =:dcId ) "
			+ "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) " + "and  (APPLICATION_STATUS > 5 ) "
			+ "and  (cad.is_active= 1 ) " + "ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
			+ " ", countQuery = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
					+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
					+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
					+ "left join division div on div.div_id = sub_div.division_id "
					+ "left join circle cir  on cir.circle_id =  div.circle_id "
					+ "left join region reg  on reg.region_id =  cir.region_id "
					+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
					+ " (:discomId =0 or disc.discom_id =:discomId ) "
					+ "and (:regionId =0 or reg.region_id =:regionId ) "
					+ "and  (:circleId =0 or cir.circle_id =:circleId ) "
					+ "and  (:divisionId =0 or div.div_id =:divisionId ) "
					+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) "
					+ "and  (:dcId =0 or dc.dc_id =:dcId ) " + "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
					+ "and  (APPLICATION_STATUS > 5) " + "and  (cad.is_active= 1 ) "
					+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC " + " ", nativeQuery = true)
	public Page<ConsumerApplicationDetail> findByConsumerApplicationDetailsByApplicationStatusPaginate(
			@Param("discomId") Long discomId, @Param("regionId") Long regionId, @Param("circleId") Long circleId,
			@Param("divisionId") Long divisionId, @Param("subDivisionId") Long subDivisionId, @Param("dcId") Long dcId,
			@Param("consumerId") Long consumerId, Pageable pageable);

	@Query(value = "SELECT * from CONSUMER_APPLICATION_DETAIL where CONSUMER_ID =:consumerId and IS_ACTIVE = 1", nativeQuery = true)
	public List<ConsumerApplicationDetail> findByConsumerId(Long consumerId);

//	@Query(value = "select  to_char(sysdate,'DD-MON-YYYY HH:MM') ,cad.short_description_of_work,cad.consumer_application_number,cad.address,c.consumer_name,c.consumer_mobile_no,c.consumer_email_id,ebwa.erp_no,ebwa.estimate_name,ebwa.cgst,ebwa.sgst,ebwa.schema,ebwa.approved_by,ebwa.deposit_amount,ebwa.supervision_amount,ebwa.estimate_date,ebwa.kwload,ebwa.kvaload from consumer_application_detail cad join erp_budget_workflow_amount ebwa on ebwa.erp_budget_workflow_number=cad.erp_workflow_number join consumer c on c.consumer_id=cad.consumer_id and cad.IS_ACTIVE = 1", nativeQuery = true)
//	public List<Map<String,String>> findAllConsumerDetails();

	@Query(value = "SELECT \r\n"
			+ "    TO_CHAR(SYSDATE, 'DD-MON-YYYY HH:MM') AS current_date,\r\n"
			+ "    cad.short_description_of_work,\r\n"
			+ "    cad.consumer_application_number,\r\n"
			+ "    cad.address,\r\n"
			+ "    c.consumer_name,\r\n"
			+ "    c.consumer_mobile_no,\r\n"
			+ "    c.consumer_email_id,\r\n"
			+ "    ebwa.erp_no,\r\n"
			+ "    ebwa.estimate_name,\r\n"
			+ "    ebwa.cgst,\r\n"
			+ "    ebwa.sgst,\r\n"
			+ "    ebwa.schema,\r\n"
			+ "    ebwa.approved_by,\r\n"
			+ "    ebwa.deposit_amount,\r\n"
			+ "    ebwa.supervision_amount,\r\n"
			+ "    ebwa.estimate_date,\r\n"
			+ "    ebwa.kwload,\r\n"
			+ "    ebwa.kvaload \r\n"
			+ "FROM \r\n"
			+ "    consumer_application_detail cad \r\n"
			+ "left JOIN \r\n"
			+ "    erp_budget_workflow_amount ebwa ON ebwa.erp_budget_workflow_number = cad.erp_workflow_number \r\n"
			+ "    left join MKY_PAY_AMNT mpa on cad.consumer_application_number = mpa.CON_APP_NUM\r\n"
			+ "    \r\n"
			+ "JOIN \r\n"
			+ "    consumer c ON c.consumer_id = cad.consumer_id \r\n"
			+ "WHERE \r\n"
			+ "    cad.application_status IN (20, 21)", nativeQuery = true)
	public List<Map<String, String>> findAllConsumerDetails();

	@Query(value = "select to_char(updated+3,'DD/MM/YYYY') from CONSUMER_APPLICATION_DETAIL  where CONSUMER_APPLICATION_NUMBER =:consumerAppNo", nativeQuery = true)
	public String findConsumerDetails(String consumerAppNo);

	@Query(value = "select * from consumer_application_detail cad where cad.Application_Status=20 and IS_ACTIVE = 1", nativeQuery = true)
	public List<ConsumerApplicationDetail> findConsumerStatus();

	public List<ConsumerApplicationDetail> findByHt11KvOrHt33Kv(long idsix, long idseven);

	public List<ConsumerApplicationDetail> findByHt11KvIsNullOrHt33KvIsNull();

	public List<ConsumerApplicationDetail> findByLtIsNotNull();

	public List<ConsumerApplicationDetail> findByLtIsNull();

	@Query(value = "SELECT r.region,r.region_code,\r\n" + "    c.circle,\r\n" + "    c.circle_code,\r\n"
			+ "    d.division,\r\n" + "    d.division_code,\r\n" + "    dc.dc_name,\r\n" + "    dc.dc_code,\r\n"
			+ "    co.consumer_name,\r\n" + "    cad.consumer_application_number,\r\n"
			+ "    co.consumer_mobile_no,\r\n" + "    TO_CHAR(cad.created, 'DD-MM-YY') AS application_date,\r\n"
			+ "    cad.nature_of_work_id,\r\n" + "    n.nature_of_work_name,\r\n" + "    cad.scheme_type_id,\r\n"
			+ "    st.scheme_type_name,\r\n" + "    a.application_status_name,\r\n" + "    a.application_status_id\r\n"
			+ "FROM\r\n" + "     circle c \r\n" + "JOIN region r ON c.region_id = r.region_id\r\n"
			+ "JOIN division d ON c.circle_id = d.circle_id\r\n"
			+ "JOIN sub_division sd ON d.div_id = sd.division_id\r\n"
			+ "JOIN distribution_center dc ON sd.subdiv_id = dc.subdiv_id\r\n"
			+ "JOIN consumer_application_detail cad ON dc.dc_id = cad.dc_id\r\n"
			+ "JOIN application_status a ON cad.application_status = a.application_status_id\r\n"
			+ "JOIN consumer co ON co.consumer_id = cad.consumer_id\r\n"
			+ "JOIN nature_of_work n ON cad.nature_of_work_id = n.nature_of_work_id\r\n"
			+ "JOIN scheme_type st ON cad.scheme_type_id = st.scheme_type_id\r\n"
			+ "WHERE    c.circle_code=:circleCode or d.division_code=:divisionCode or r.region_code=:regionCode or dc.dc_code =:dcCode \r\n"
			+ "\r\n" + "ORDER BY c.circle", nativeQuery = true)
	List<Map<String, ?>> getDetailsByDcCodeForMis(@Param("dcCode") String dcCode,
			@Param("circleCode") String circleCode, @Param("divisionCode") String divisionCode,
			@Param("regionCode") String regionCode);

	public List<ConsumerApplicationDetail> findByNatureOfWorkType_NatureOfWorkTypeId(long idsix);

//	o&M
//	public List<ConsumerApplicationDetail> findByLtIsNotNullAndNatureOfWorkType_NatureOfWorkTypeIdOrNatureOfWorkType_NatureOfWorkTypeId(long six,long one);

	// o&M
	public List<ConsumerApplicationDetail> findByNatureOfWorkType_NatureOfWorkTypeIdAndLtIsNotNullOrNatureOfWorkType_NatureOfWorkTypeIdAndLtIsNotNull(
			long six, long one);

//	HTM
//	public List<ConsumerApplicationDetail> findByLtIsNullAndNatureOfWorkType_NatureOfWorkTypeIdOrNatureOfWorkType_NatureOfWorkTypeId(long six,long one);

	// HTM
	public List<ConsumerApplicationDetail> findByNatureOfWorkType_NatureOfWorkTypeIdAndLtIsNullOrNatureOfWorkType_NatureOfWorkTypeIdAndLtIsNull(
			long six, long one);

	@Query(value = "select * from consumer_application_detail\r\n" + "where nature_of_work_id in (1,6)", nativeQuery = true)
	public List<ConsumerApplicationDetail> findConsumerApplicationDetails();

	@Query(value = "select * from consumer_application_detail where samagra_id=:id", nativeQuery = true)
	public ConsumerApplicationDetail getConsumerApplicationDetailsBySamagraId(@Param("id") String samagraId);

	@Query(value = "select * from consumer_application_detail where district_id =:id", nativeQuery = true)
	public List<ConsumerApplicationDetail> getConsumerApplicationDetailsByDistrictId(@Param("id") String districtId);

//	@Query(value = "select * from consumer_application_detail where natureOfWorkType_NATURE_OF_WORK_ID =:i ", nativeQuery = true)
	public List<ConsumerApplicationDetail> findByNatureOfWorkType_NatureOfWorkTypeId(Long i);

	public List<ConsumerApplicationDetail> findByNatureOfWorkType_NatureOfWorkTypeIdAndDistributionCenter_dcCode(Long i,
			String dcCode);

	@Query(value = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
			+ " left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
			+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
			+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
			+ "left join division div on div.div_id = sub_div.division_id "
			+ "left join circle cir  on cir.circle_id =  div.circle_id "
			+ "left join region reg  on reg.region_id =  cir.region_id "
			+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
			+ "(:discomId =0 or disc.discom_id =:discomId ) AND" + "(:regionId =0 or reg.region_id =:regionId ) "
			+ "and  (:circleId =0 or cir.circle_id =:circleId ) " + "and  (:divisionId =0 or div.div_id =:divisionId ) "
			+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) " + "and  (:dcId =0 or dc.dc_id =:dcId ) "
			+ "and  (APPLICATION_STATUS > 3 ) " + "and  (cad.is_active= 1 )"
			+ "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
			+ "AND UPPER(cad.CONSUMER_NAME) LIKE UPPER(CONCAT(CONCAT('%',:filterValue),'%') )"
			+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
			+ " ", countQuery = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
					+ " left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
					+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
					+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
					+ "left join division div on div.div_id = sub_div.division_id "
					+ "left join circle cir  on cir.circle_id =  div.circle_id "
					+ "left join region reg  on reg.region_id =  cir.region_id "
					+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
					+ "(:discomId =0 or disc.discom_id =:discomId ) AND"
					+ "(:regionId =0 or reg.region_id =:regionId ) "
					+ "and  (:circleId =0 or cir.circle_id =:circleId ) "
					+ "and  (:divisionId =0 or div.div_id =:divisionId ) "
					+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) "
					+ "and  (:dcId =0 or dc.dc_id =:dcId ) " + "and  (APPLICATION_STATUS > 3 ) "
					+ "and  (cad.is_active= 1 )" + "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
					+ "AND UPPER(cad.CONSUMER_NAME) LIKE UPPER(CONCAT(CONCAT('%',:filterValue),'%') )"
					+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC " + " ", nativeQuery = true)
	public Page<ConsumerApplicationDetail> findByConsumerApplicationDetailsUserWiseApplicantNamePaginate(
			@Param("discomId") Long discomId, @Param("regionId") Long regionId, @Param("circleId") Long circleId,
			@Param("divisionId") Long divisionId, @Param("subDivisionId") Long subDivisionId, @Param("dcId") Long dcId,
			@Param("consumerId") Long consumerId, @Param("filterValue") String filterValue, Pageable pageable);

	@Query(value = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
			+ " left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
			+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
			+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
			+ "left join division div on div.div_id = sub_div.division_id "
			+ "left join circle cir  on cir.circle_id =  div.circle_id "
			+ "left join region reg  on reg.region_id =  cir.region_id "
			+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
			+ "(:discomId =0 or disc.discom_id =:discomId ) AND" + "(:regionId =0 or reg.region_id =:regionId ) "
			+ "and  (:circleId =0 or cir.circle_id =:circleId ) " + "and  (:divisionId =0 or div.div_id =:divisionId ) "
			+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) " + "and  (:dcId =0 or dc.dc_id =:dcId ) "
			+ "and  (APPLICATION_STATUS > 5 ) " + "and  (cad.is_active= 1 )"
			+ "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
			+ "AND UPPER(cad.CONSUMER_APPLICATION_NUMBER) LIKE UPPER(CONCAT(CONCAT('%',:filterValue),'%') )"
			+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
			+ " ", countQuery = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
					+ " left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
					+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
					+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
					+ "left join division div on div.div_id = sub_div.division_id "
					+ "left join circle cir  on cir.circle_id =  div.circle_id "
					+ "left join region reg  on reg.region_id =  cir.region_id "
					+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
					+ "(:discomId =0 or disc.discom_id =:discomId ) AND "
					+ "(:regionId =0 or reg.region_id =:regionId ) "
					+ "and  (:circleId =0 or cir.circle_id =:circleId ) "
					+ "and  (:divisionId =0 or div.div_id =:divisionId ) "
					+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) "
					+ "and  (:dcId =0 or dc.dc_id =:dcId ) " + "and  (APPLICATION_STATUS > 5 ) "
					+ "and  (cad.is_active= 1 )" + "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
					+ "AND UPPER(cad.CONSUMER_APPLICATION_NUMBER) LIKE UPPER(CONCAT(CONCAT('%',:filterValue),'%') )"
					+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC " + " ", nativeQuery = true)
	public Page<ConsumerApplicationDetail> findByConsumerApplicationDetailsUserWiseApplicationNumberPaginate(

			@Param("discomId") Long discomId, @Param("regionId") Long regionId, @Param("circleId") Long circleId,
			@Param("divisionId") Long divisionId, @Param("subDivisionId") Long subDivisionId, @Param("dcId") Long dcId,
			@Param("consumerId") Long consumerId, @Param("filterValue") String filterValue, Pageable pageable);

//	bhanu sir 
	@Query(value = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
			+ "left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
			+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
			+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
			+ "left join division div on div.div_id = sub_div.division_id "
			+ "left join circle cir  on cir.circle_id =  div.circle_id "
			+ "left join region reg  on reg.region_id =  cir.region_id "
			+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
			+ "(:discomId =0 or disc.discom_id =:discomId ) AND " + "(:regionId =0 or reg.region_id =:regionId ) "
			+ "and  (:circleId =0 or cir.circle_id =:circleId ) " + "and  (:divisionId =0 or div.div_id =:divisionId ) "
			+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) " + "and  (:dcId =0 or dc.dc_id =:dcId ) "
			+ "and  (APPLICATION_STATUS > 5 ) " + "and  (cad.is_active= 1 ) "
			+ "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) " + "AND APPLICATION_STATUS = :filterValue "
			+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
			+ " ", countQuery = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
					+ "left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
					+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
					+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
					+ "left join division div on div.div_id = sub_div.division_id "
					+ "left join circle cir  on cir.circle_id =  div.circle_id "
					+ "left join region reg  on reg.region_id =  cir.region_id "
					+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
					+ "(:discomId =0 or disc.discom_id =:discomId ) AND "
					+ "(:regionId =0 or reg.region_id =:regionId ) "
					+ "and  (:circleId =0 or cir.circle_id =:circleId ) "
					+ "and  (:divisionId =0 or div.div_id =:divisionId ) "
					+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) "
					+ "and  (:dcId =0 or dc.dc_id =:dcId ) " + "and  (APPLICATION_STATUS > 5 ) "
					+ "and  (cad.is_active= 1 ) " + "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
					+ "AND APPLICATION_STATUS = :filterValue" + " ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
					+ " ", nativeQuery = true)
	public Page<ConsumerApplicationDetail> findByConsumerApplicationDetailsUserWiseApplicationStatusPaginate(
			@Param("discomId") Long discomId, @Param("regionId") Long regionId, @Param("circleId") Long circleId,
			@Param("divisionId") Long divisionId, @Param("subDivisionId") Long subDivisionId, @Param("dcId") Long dcId,
			@Param("consumerId") Long consumerId, @Param("filterValue") String filterValue, Pageable pageable);

	@Query(value = "  SELECT\r\n" + "    sub.transaction_date,\r\n" + "    sub.transaction_id,\r\n"
			+ "    sub.amount BILLDESK_AMOUNT,\r\n" + "    sub.ORDER_ID,\r\n" + "    erp.supervision_amount,\r\n"
			+ "    erp.cgst,\r\n" + "    erp.sgst,\r\n" + "     sub.BANK_REF_NO,\r\n" + "        sub.MERCID,\r\n"
			+ "        erp.DEPOSIT_AMOUNT,\r\n" + "        erp.KVALOAD SUPPLY_AFFORDING_CHARGE,\r\n"
			+ "        erp.KWLOAD SYSTEM_DEVELOPEMENT_CHARGE\r\n" + "FROM\r\n"
			+ "    erp_budget_workflow_amount erp\r\n" + "JOIN (\r\n" + "    SELECT\r\n"
			+ "        cad.consumer_application_number,\r\n" + "        cad.ERP_WORKFLOW_NUMBER,\r\n"
			+ "        bps.transaction_date,\r\n" + "        bps.transaction_id,\r\n" + "        bps.amount,\r\n"
			+ "        bps.ORDER_ID,\r\n" + "        bps.BANK_REF_NO,\r\n" + "        bps.MERCID\r\n" + "    FROM\r\n"
			+ "        billdesk_payment_res bps\r\n" + "    JOIN\r\n"
			+ "        consumer_application_detail cad ON bps.CONSUMER_APPLICATION_NO = cad.CONSUMER_APPLICATION_NUMBER\r\n"
			+ "    WHERE\r\n"
			+ "  --      TO_DATE(SUBSTR(bps.transaction_date, 0, INSTR(bps.transaction_date, 'T') - 1), 'YYYY-MM-DD') = TO_DATE(:input_date, 'YYYY-MM-DD')\r\n"
			+ "     TO_DATE(substr(bps.transaction_date,0,instr(bps.transaction_date,'T') - 1),'YYYY-MM-DD') \r\n"
			+ "    BETWEEN  TO_DATE(:toDate,'YYYY-MM-DD') AND TO_DATE(:fromDate,'YYYY-MM-DD')\r\n"
			+ "        AND bps.auth_status = '0300'\r\n"
			+ ") sub ON erp.ERP_NO = sub.ERP_WORKFLOW_NUMBER", nativeQuery = true)
	List<Map<String, ?>> getBillDeskMisByDate(@Param("toDate") String toDate, @Param("fromDate") String fromDate);

	@Query(value = "SELECT \r\n"
			+ "    NGB_DC_CD,\r\n"
			+ "     COUNT(*) AS transaction_count,\r\n"
			+ "   SUM(amount) AS total_sum,\r\n"
			+ "    TO_CHAR(TO_DATE(SUBSTR(transaction_date, 0, INSTR(transaction_date, 'T') - 1), 'YYYY-MM-DD'),'YYYY-MM-DD')  AS transaction_date \r\n"
			+ "FROM \r\n"
			+ "    BILLDESK_PAYMENT_RES bps\r\n"
			+ "JOIN \r\n"
			+ "    CONSUMER_APPLICATION_DETAIL cad ON bps.CONSUMER_APPLICATION_NO = cad.CONSUMER_APPLICATION_NUMBER\r\n"
			+ "JOIN\r\n"
			+ "    DISTRIBUTION_CENTER  dc on cad.dc_id = dc.dc_id  \r\n"
			+ "WHERE \r\n"
			+ "    AUTH_STATUS = '0300'\r\n"
			+ "    AND TO_DATE(SUBSTR(transaction_date, 0, INSTR(transaction_date, 'T') - 1), 'YYYY-MM-DD') \r\n"
			+ "        BETWEEN TO_DATE(:toDate, 'YYYY-MM-DD') AND TO_DATE(:fromDate, 'YYYY-MM-DD')\r\n"
			+ "GROUP BY \r\n"
			+ "    NGB_DC_CD, \r\n"
			+ "    transaction_date", nativeQuery = true)
	List<Map<String, ?>> getBillDesk(@Param("toDate") String toDate, @Param("fromDate") String fromDate);

	@Query(value = "select * from CONSUMER_APPLICATION_DETAIL where CONSUMER_APPLICATION_NUMBER =:consumerApplicationNo", nativeQuery = true)
	public CadProjection findByConsumerApplicationNO(@Param("consumerApplicationNo") String consumerApplicationNo);

//	@Query(value = "SELECT cad.CONSUMER_APPLICATION_ID FROM CONSUMER_APPLICATION_DETAIL cad "
//	        + "WHERE CONSUMER_APPLICATION_NUMBER = :consumerApplicationNo", nativeQuery = true)
//	public ConsumerApplicationDetail findByConsumerApplicationNO(@Param("consumerApplicationNo") String consumerApplicationNo);

//	@Query(value = "SELECT cad.CONSUMER_APPLICATION_ID from CONSUMER_APPLICATION_DETAIL cad "
//			+ "left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
//			+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
//			+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
//			+ "left join division div on div.div_id = sub_div.division_id "
//			+ "left join circle cir  on cir.circle_id =  div.circle_id "
//			+ "left join region reg  on reg.region_id =  cir.region_id "
//			+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "  (cad.is_active= 1 ) "
//			 + "and  (cad.CONSUMER_APPLICATION_NUMBER =:consumerApplicationNo ) ", nativeQuery = true)
//	public ConsumerApplicationDetail findByConsumerApplicationNO(@Param("consumerApplicationNo") String consumerApplicationNo);

	@Query(value = "SELECT \r\n"
			+ "    CIR.CIRCLE AS Name_OF_Circle,\r\n"
			+ "    DIV.DIVISION AS Name_OF_Division,\r\n"
			+ "    DC.DC_NAME AS Name_OF_DC,\r\n"
			+ "    ASA.APPLICATION_STATUS_NAME AS Application_Status,\r\n"
			+ "    CAD.CONSUMER_APPLICATION_NUMBER AS Application_No,\r\n"
			+ "    CAD.CONSUMER_NAME AS Name_of_Applicant,\r\n"
			+ "    ST.SCHEME_TYPE_NAME AS Name_OF_Scheme,\r\n"
			+ "    NOW.NATURE_OF_WORK_NAME AS Nature_OF_Work,\r\n"
			+ "    TO_CHAR(CAD.CREATED,'DD-MM-YY') AS Date_OF_Creation,\r\n"
			+ "    CAD.CREATED_BY AS Created_BY_Login,\r\n"
			+ "    BPR.TRANSACTION_ID registration_transaction_id,\r\n"
			+ "    BPR.AMOUNT AS Payment_Amount,\r\n"
			+ "    TO_DATE(SUBSTR(BPR.TRANSACTION_DATE,1,10),'YYYY-MM-DD') AS Date_OF_Registration,\r\n"
			+ "    BPR1.TRANSACTION_ID demand_transaction_id,\r\n"
			+ "    BPR1.AMOUNT AS Supervision_Payment_Amount,\r\n"
			+ "    TO_DATE(SUBSTR(BPR1.TRANSACTION_DATE,1,10),'YYYY-MM-DD') AS Date_OF_Supervision_Payment,\r\n"
			+ "    VWO.WORK_ORDER_NO AS Work_order_No,\r\n"
			+ "    VWO.WORK_ORDER_DATE AS Work_order_Date,\r\n"
			+ "    CAD.WORK_COMPLETION_DATE AS Work_Comp_Date_By_Contra,\r\n"
			+ "    ERP.CGST,\r\n"
			+ "    ERP.SGST,\r\n"
			+ "    ERP.ESTIMATE_SANCTION_NO,\r\n"
			+ "    ERP.ESTIMATE_NAME,\r\n"
			+ "    ERP.LOCATION,\r\n"
			+ "    ERP.SCHEMA,\r\n"
			+ "    ERP.ESTIMATE_AMOUNT,\r\n"
			+ "    ERP.APPROVED_BY,\r\n"
			+ "    ERP.ESTIMATE_STATUS,\r\n"
			+ "    ERP.ERP_NO AS KMY_ERP_ESTIMATE_NUMBER,\r\n"
			+ "    MKY.ABEDAN_SULK,\r\n"
			+ "    MKY.TOTAL_AMOUNT,\r\n"
			+ "    MKY.SECURITY_DEPOSIT,\r\n"
			+ "    MKY.SCHEME_CODE,\r\n"
			+ "    MKY.PAYALE_AMOUNT,\r\n"
			+ "    MKY.MPMK_MAF_BILL,\r\n"
			+ "    MKY.GOV_MAF_BILL,\r\n"
			+ "    MKY.CREATED,\r\n"
			+ "    MKY.CARRY_AMOUNT_BY_APPLICANT,\r\n"
			+ "    CFB.CONTRACTOR_NAME AS NAME_OF_CONTRACTOR,\r\n"
			+ "    CFB.CONTRACTOR_STATE,\r\n"
			+ "    CFB.CONTRACTOR_ID\r\n"
			+ "FROM \r\n"
			+ "    DEPOSITE_SCHEMA.CONSUMER_APPLICATION_DETAIL CAD\r\n"
			+ "    LEFT JOIN CONTRACTOR_FOR_BID CFB ON CFB.CONSUMER_APPLICATION_NUMBER=CAD.CONSUMER_APPLICATION_NUMBER\r\n"
			+ "    JOIN APPLICATION_STATUS ASA ON (ASA.APPLICATION_STATUS_ID=CAD.APPLICATION_STATUS)\r\n"
			+ "    JOIN NATURE_OF_WORK NOW ON (NOW.NATURE_OF_WORK_ID=CAD.NATURE_OF_WORK_ID)\r\n"
			+ "    JOIN SCHEME_TYPE ST ON (ST.SCHEME_TYPE_ID=CAD.SCHEME_TYPE_ID)\r\n"
			+ "    LEFT JOIN (\r\n"
			+ "        SELECT * \r\n"
			+ "        FROM BILLDESK_PAYMENT_RES \r\n"
			+ "        WHERE BILL_DESK_RES_ID IN (\r\n"
			+ "            SELECT MAX(BILL_DESK_RES_ID) \r\n"
			+ "            FROM BILLDESK_PAYMENT_RES \r\n"
			+ "            WHERE AMOUNT=1180 AND TRANSACTION_ERROR_DESC='Transaction Successful'  \r\n"
			+ "            GROUP BY CONSUMER_APPLICATION_NO\r\n"
			+ "        )\r\n"
			+ "    ) BPR ON (BPR.CONSUMER_APPLICATION_NO=CAD.CONSUMER_APPLICATION_NUMBER)\r\n"
			+ "    LEFT JOIN (\r\n"
			+ "        SELECT * \r\n"
			+ "        FROM BILLDESK_PAYMENT_RES \r\n"
			+ "        WHERE BILL_DESK_RES_ID IN (\r\n"
			+ "            SELECT MAX(BILL_DESK_RES_ID) \r\n"
			+ "            FROM BILLDESK_PAYMENT_RES \r\n"
			+ "            WHERE AMOUNT<>1180 AND TRANSACTION_ERROR_DESC='Transaction Successful'  \r\n"
			+ "            GROUP BY CONSUMER_APPLICATION_NO\r\n"
			+ "        )\r\n"
			+ "    ) BPR1 ON (BPR1.CONSUMER_APPLICATION_NO=CAD.CONSUMER_APPLICATION_NUMBER)\r\n"
			+ "    LEFT JOIN VENDOR_WORK_ORDER VWO ON (VWO.CONSUMER_APPLICATION_NO=CAD.CONSUMER_APPLICATION_NUMBER) \r\n"
			+ "    JOIN DISTRIBUTION_CENTER DC ON (DC.DC_ID=CAD.DC_ID)\r\n"
			+ "    JOIN SUB_DIVISION SD ON (SD.SUBDIV_ID=DC.SUBDIV_ID)\r\n"
			+ "    JOIN DIVISION DIV ON (DIV.DIV_ID=SD.DIVISION_ID)\r\n"
			+ "    JOIN CIRCLE CIR ON(CIR.CIRCLE_ID=DIV.CIRCLE_ID)\r\n"
			+ "    LEFT JOIN MKY_PAY_AMNT MKY ON CAD.consumer_application_number = MKY.CON_APP_NUM\r\n"
			+ "    LEFT JOIN ERP_BUDGET_WORKFLOW_AMOUNT ERP ON ERP.ERP_NO = CAD.ERP_WORKFLOW_NUMBER\r\n"
			+ "WHERE \r\n"
			+ "    cad.NATURE_OF_WORK_ID IN (1,2,3,4,5,6,7,8)\r\n"
			+ "    AND CAD.CONSUMER_APPLICATION_NUMBER = :consumerApplicationNo \r\n"
			+ "    AND cad.IS_ACTIVE = 1\r\n"
			+ "    AND cad.IS_DELETED = 0\r\n"
			+ "ORDER BY APPLICATION_STATUS", nativeQuery = true)
	public List<Map<String, Object>> cadDetailsReturn(@Param("consumerApplicationNo") String consumerApplicationNo);

	@Query(value = "select consumer_application_number from "
			+ "(select consumer_application_number,count(*) from consumer_application_detail group by consumer_application_number,application_status having count(*)>1)", nativeQuery = true)
	public List<Map<String, String>> settlementNautreOfWork();
	
	@Query(value = " SELECT * from CONSUMER_APPLICATION_DETAIL where CONSUMER_APPLICATION_NUMBER =:consumerAppNo", nativeQuery = true)
	public List<ConsumerApplicationDetail> UpadateConsumerApplicationDetailsByDuplicateAppNumber(
			@Param("consumerAppNo") String consumerAppNo) throws ConsumerNotFoundByApplicationIdException;

	@Query(value="SELECT\r\n"
			+ "    c.circle,\r\n"
			+ "    dc.dc_name,\r\n"
			+ "  TO_CHAR(TO_DATE(SUBSTR(transaction_date, 0, INSTR(transaction_date, 'T') - 1), 'YYYY-MM-DD'), 'YYYY-MM-DD') AS transaction_date,\r\n"
			+ "    SUM(erp.supervision_amount) AS supervision_amount,\r\n"
			+ "    SUM(erp.deposit_amount) AS deposit_amount,\r\n"
			+ "    SUM(erp.cgst) AS cgst,\r\n"
			+ "    SUM(erp.sgst) AS sgst,\r\n"
			+ "    erp.kwload AS system_development_charge,\r\n"
			+ "    erp.kvaload AS supply_affording_charge,\r\n"
			+ "    erp.colony_or_slum,\r\n"
			+ "    erp.je_return_amount,\r\n"
			+ "    mky.total_amount,\r\n"
			+ "    mky.gov_maf_bill,\r\n"
			+ "    mky.mpmk_maf_bill,\r\n"
			+ "    mky.payale_amount,\r\n"
			+ "    mky.abedan_sulk,\r\n"
			+ "    mky.security_deposit,\r\n"
			+ "    mky.carry_amount_by_applicant\r\n"
			+ "FROM billdesk_payment_res bps\r\n"
			+ "JOIN consumer_application_detail cad ON bps.consumer_application_no = cad.consumer_application_number\r\n"
			+ "JOIN distribution_center dc ON dc.dc_id = cad.dc_id\r\n"
			+ "JOIN sub_division sd ON sd.subdiv_id = dc.subdiv_id\r\n"
			+ "JOIN division d ON sd.division_id = d.div_id\r\n"
			+ "JOIN circle c ON c.circle_id = d.circle_id\r\n"
			+ "LEFT JOIN erp_budget_workflow_amount erp ON cad.erp_workflow_number = erp.erp_no\r\n"
			+ "LEFT JOIN mky_pay_amnt mky ON mky.erp_num = erp.erp_no\r\n"
			+ "WHERE TO_DATE(SUBSTR(bps.transaction_date, 0, INSTR(bps.transaction_date, 'T') - 1), 'YYYY-MM-DD') = TO_DATE('2023-11-14', 'YYYY-MM-DD')\r\n"
			+ "GROUP BY dc.dc_name, c.circle, TO_DATE(SUBSTR(bps.transaction_date, 0, INSTR(bps.transaction_date, 'T') - 1), 'YYYY-MM-DD'),\r\n"
			+ "    erp.supervision_amount, erp.deposit_amount, erp.cgst, erp.sgst, erp.kwload, erp.kvaload, erp.colony_or_slum, erp.je_return_amount,\r\n"
			+ "    mky.total_amount, mky.gov_maf_bill, mky.mpmk_maf_bill, mky.payale_amount, mky.abedan_sulk, mky.security_deposit, mky.carry_amount_by_applicant",  nativeQuery = true)
	public List<Map<String, Object>> getCraData(@Param("date") String date);

	@Query(value = "select consumer_application_number,count(*),nature_of_work_id,application_status from consumer_application_detail  where\r\n"
			+ "consumer_application_detail.nature_of_work_id<>8 group by consumer_application_number ,nature_of_work_id,application_status having count(*)>1", nativeQuery = true)
	public List<Map<String, String>> findListByConsumerApplicaation();
	
	@Query(value = "select *  from consumer_application_detail where CONSUMER_APPLICATION_NUMBER =:consumerAppNo", nativeQuery = true)
	public List<ConsumerApplicationDetail> findListByConsumerApplicatonNumber(@Param("consumerAppNo") String consumerAppNo);


	@Query(value = "select *  from consumer_application_detail where SCHEME_TYPE_ID=1 and APPLICATION_STATUS>=12", nativeQuery = true)
	public List<ConsumerApplicationDetail> findByScemetypeId();


	@Query(value="SELECT *\r\n"
			+ "FROM CONSUMER_APPLICATION_DETAIL\r\n"
			+ "WHERE nature_of_work_id = 5\r\n"
			+ "  AND application_status = 20\r\n"
			+ "  AND payment_date IS NULL", nativeQuery = true)
	public List<ConsumerApplicationDetail> findAllOytApplication();


	@Query(value = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
			+ "left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
			+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
			+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
			+ "left join division div on div.div_id = sub_div.division_id "
			+ "left join circle cir  on cir.circle_id =  div.circle_id "
			+ "left join region reg  on reg.region_id =  cir.region_id "
			+ "left join nature_of_work now  on now.nature_of_work_id =  cad.nature_of_work_id "
			+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
			+ "(:discomId =0 or disc.discom_id =:discomId ) AND " + "(:regionId =0 or reg.region_id =:regionId ) "
			+ "and  (:circleId =0 or cir.circle_id =:circleId ) " + "and  (:divisionId =0 or div.div_id =:divisionId ) "
			+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) " + "and  (:dcId =0 or dc.dc_id =:dcId ) "
			+ "and  (APPLICATION_STATUS > 5 ) " + "and  (cad.is_active= 1 ) "
			+ "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) " + " AND now.nature_of_work_id = :filterValue "
			+ "ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
			+ " ", countQuery = "SELECT cad.* from CONSUMER_APPLICATION_DETAIL cad "
					+ "left join consumer con on cad.CONSUMER_ID = con.CONSUMER_ID "
					+ "left join distribution_center  dc on dc.dc_id = cad.dc_id "
					+ "left join sub_division sub_div on sub_div.SUBDIV_ID  = dc.subdiv_id "
					+ "left join division div on div.div_id = sub_div.division_id "
					+ "left join circle cir  on cir.circle_id =  div.circle_id "
					+ "left join region reg  on reg.region_id =  cir.region_id "
					+ "left join nature_of_work now  on now.nature_of_work_id =  cad.nature_of_work_id "
					+ "left join discom disc  on disc.discom_id =  reg.discom_id " + "where "
					+ "(:discomId =0 or disc.discom_id =:discomId ) AND "
					+ "(:regionId =0 or reg.region_id =:regionId ) "
					+ "and  (:circleId =0 or cir.circle_id =:circleId ) "
					+ "and  (:divisionId =0 or div.div_id =:divisionId ) "
					+ "and  (:subDivisionId =0 or sub_div.SUBDIV_ID =:subDivisionId ) "
					+ "and  (:dcId =0 or dc.dc_id =:dcId ) " + "and  (APPLICATION_STATUS > 5 ) "
					+ "and  (cad.is_active= 1 ) " + "and  (:consumerId =0 or cad.CONSUMER_ID =:consumerId ) "
					+ "AND now.nature_of_work_id = :filterValue" + " ORDER BY cad.CONSUMER_APPLICATION_ID DESC "
					+ " ", nativeQuery = true)
	public Page<ConsumerApplicationDetail> findByConsumerApplicationDetailsUserWiseNatureOfWorkPaginate(
			Long discomId,
			Long regionId, Long circleId, Long divisionId, Long subDivisionId, Long dcId, Long consumerId,
			String filterValue, Pageable paging);

	 @Query(value="SELECT *\r\n"
     		+ "FROM CONSUMER_APPLICATION_DETAIL CAD\r\n"
     		+ "WHERE CAD.APPLICATION_STATUS in (6,7,12)\r\n"
     		+ "AND CAD.NATURE_OF_WORK_ID!=8\r\n"
     		+ "AND NOT EXISTS (\r\n"
     		+ "    SELECT 1\r\n"
     		+ "    FROM BILLDESK_PAYMENT_RES BPR\r\n"
     		+ "    WHERE CAD.CONSUMER_APPLICATION_NUMBER = BPR.CONSUMER_APPLICATION_NO\r\n"
     		+ "    AND BPR.ADDITIONAL_INFO1 LIKE '%Registration_Fees%' and BPR.AUTH_STATUS='0300'\r\n"
     		+ ")\r\n"
     		+ "AND NOT EXISTS (\r\n"
     		+ "    SELECT 1\r\n"
     		+ "    FROM MANUAL_PAYMENT MP\r\n"
     		+ "    WHERE CAD.CONSUMER_APPLICATION_NUMBER = MP.CON_APP_NO\r\n"
     		+ "    AND MP.PAYMENT_TYPE LIKE '%Registration_Fees%' and MP.AUTH_STATUS='0300'\r\n"
     		+ ")", nativeQuery=true)
	List<ConsumerApplicationDetail> RegistrationFeesNotDoneRevertApp();

	 @Query(value="select * from consumer_application_detail where erp_workflow_number = :erpNo",nativeQuery = true)
	public List<ConsumerApplicationDetail> findByErpNo(String erpNo);

	 
	 @Query(value="select * from \r\n"
	 		+ "consumer_application_detail cad \r\n"
	 		+ "left join  CONTRACTOR_FOR_BID_WORK_STATUS cfbs on \r\n"
	 		+ "cad.CONSUMER_APPLICATION_NUMBER = cfbs.CONSUMER_APPLICATION_NUMBER\r\n"
	 		+ "where application_status=22 and cfbs.CONSUMER_APPLICATION_NUMBER is null", nativeQuery = true)
	public List<ConsumerApplicationDetail> saveContractorExpectedDates();



	
}