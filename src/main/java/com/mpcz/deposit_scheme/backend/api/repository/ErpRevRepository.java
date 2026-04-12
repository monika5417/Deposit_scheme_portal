package com.mpcz.deposit_scheme.backend.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mpcz.deposit_scheme.backend.api.domain.ErpRev;

@Repository
public interface ErpRevRepository extends JpaRepository<ErpRev, Long> {

	ErpRev findByConsAppNo(String consumerApp);

	@Query(value="select * from erp_rev where cons_app_no=:consumerApp",nativeQuery=true)
	List<ErpRev> findByConsumerAppNo(String consumerApp);

	@Query(value="select * from erp_rev where cons_app_no=:consumerAppNo and rem_return_amt is not null",nativeQuery = true)
	ErpRev findReturnAmountData(String consumerAppNo);

	ErpRev findByConsAppNoAndPayAmt(String consumerApplicationNo, BigDecimal amount);

	ErpRev findByNewErpNo(String erpNo);
	
	ErpRev findByConsAppNoAndNewErpNo(String consumerApplicationNumber,String erpNo);
	
	ErpRev findByConsAppNoAndNewErpNoAndErpVersion(String consumerApplicationNumber,String erpNo,String erpVersion);
	
	ErpRev findByConsAppNoAndNewErpNoAndVersionNumber(String consumerApplicationNumber,String erpNo,Long erpVersion);
	
	ErpRev findByConsAppNoAndVersionNumber(String consumerApplicationNumber,Long erpVersion);

	@Query(value="SELECT *\r\n"
			+ "FROM (\r\n"
			+ "    SELECT *\r\n"
			+ "    FROM erp_rev\r\n"
			+ "    WHERE cons_app_no =:consumerApp \r\n"
			+ "    ORDER BY id DESC\r\n"
			+ ")\r\n"
			+ "WHERE ROWNUM = 1",nativeQuery=true)
	List<ErpRev> findByConsumerAppNo_1(String consumerApp);
	
}