package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mpcz.deposit_scheme.backend.api.domain.BillDeskPaymentResponse;

@Repository
public interface BillPaymentResponseeeeeeeRepository extends JpaRepository<BillDeskPaymentResponse, Long> {

	BillDeskPaymentResponse findByOrderid(String orderid);

	BillDeskPaymentResponse findByOrderidAndTranId(String orderid, String transactionid);

	List<BillDeskPaymentResponse> findByConsumerApplicationNo(String consumerApplicationNumber);

	@Query(value = "SELECT\r\n"
			+ "    MAX(CASE WHEN amount = 1180.00 THEN TRANSACTION_DATE ELSE NULL END) AS registration_Date,\r\n"
			+ "    MAX(CASE WHEN amount <> 1180.00 THEN TRANSACTION_DATE ELSE NULL END) AS demand_Date\r\n"
			+ "FROM BILLDESK_PAYMENT_RES\r\n" + "WHERE consumer_application_no =:appNo\r\n" + "", nativeQuery = true)
	List<Map<String, String>> getTransactionDateByConsumerApplicationNo(
			@Param("appNo") String consumerApplicationNumber);

	BillDeskPaymentResponse findByTranId(String tranId);

	@Query(value = "SELECT COUNT(*) FROM BILLDESK_PAYMENT_RES WHERE TRANSACTION_ID=?1", nativeQuery = true)
	Integer getByTransactionId(String tranId);

	@Query(value = "select * from BILLDESK_PAYMENT_RES  WHERE consumer_application_no =:consumerApplicationNumber and ADDITIONAL_INFO1 ='Demand_fees' and  AUTH_STATUS='0300'", nativeQuery = true)
	BillDeskPaymentResponse getApplicationByDemandFees(
			@Param("consumerApplicationNumber") String consumerApplicationNumber);

	@Query(value = "select * from billdesk_payment_res where additional_info1 is null and amount > 1190", nativeQuery = true)
	List<BillDeskPaymentResponse> AdditionalInfoSetDemandFees();

	@Query(value = "select * from billdesk_payment_res where additional_info1 is null and amount = 1180", nativeQuery = true)
	List<BillDeskPaymentResponse> AdditionalInfoSetRegistrationFees();

	@Query(value = "SELECT\r\n" + "    *\r\n" + "FROM\r\n" + "    billdesk_payment_res\r\n" + "WHERE\r\n"
			+ "    TO_DATE(substr(transaction_date,0,instr(transaction_date,'T') - 1),'YYYY-MM-DD') = TO_DATE(:s,'YYYY-MM-DD')\r\n"
			+ "    AND auth_status = '0300'\r\n" + "    and amount > 1180", nativeQuery = true)
	List<BillDeskPaymentResponse> RavindraSendDataForContractorSelectinon(@Param("s") String s);

	@Query(value = "select * from billdesk_payment_res where auth_status='0300' and additional_info1='Demand_fees' \r\nand consumer_application_no = :consumerApplicationNo", nativeQuery = true)
	BillDeskPaymentResponse getDemandDataFromBilldesk(String consumerApplicationNo);

	@Query(value = "select * from billdesk_payment_res where auth_status='0300' and additional_info1='Revised_Demand_fees' \r\nand consumer_application_no =:consumerApplicationNo", nativeQuery = true)
	BillDeskPaymentResponse getReviseDemandDataFromBilldesk(String consumerApplicationNo);

	@Query(value = "SELECT * \r\n"
			+ "FROM (\r\n"
			+ "    SELECT * \r\n"
			+ "    FROM billdesk_payment_res \r\n"
			+ "    WHERE additional_info1 = 'Demand_fees' \r\n"
			+ "      AND auth_status = '0300' \r\n"
			+ "      AND merc_refund_ref_no IS NULL \r\n"
			+ "      AND consumer_application_no = :consumerApplicationNo \r\n"
			+ "    ORDER BY bill_desk_res_id DESC\r\n"
			+ ") \r\n"
			+ "WHERE ROWNUM = 1", nativeQuery = true)
	BillDeskPaymentResponse getBillDeskLatestDemand(String consumerApplicationNo);
	
	@Query(value ="select * from billdesk_payment_res where consumer_application_no=:consumerApplicationNo and auth_status='0300'",nativeQuery = true)
	List<BillDeskPaymentResponse> getAllPaymentDetails(String consumerApplicationNo);
}
