package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mpcz.deposit_scheme.backend.api.domain.ApplicationDocument;
import com.mpcz.deposit_scheme.backend.api.domain.Consumer;

@Repository
public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {

	@Query(value = "select * from application_document  where consumer_application_id = :consumerApplicationDetailId and is_active= 1", nativeQuery = true)
	public ApplicationDocument findByConsumerApplicationDetailId(
			@Param("consumerApplicationDetailId") Long consumerApplicationDetailId);

	
	@Query(value = "select * from application_document  where consumer_application_id = :consumerApplicationDetailId and is_active= 1", nativeQuery = true)
	public ApplicationDocument findByconsumerApplicationDetail_consumerApplicationId(
			@Param("consumerApplicationDetailId") Long consumerApplicationDetailId);


	@Query(value="select * from application_document where consumer_application_id = :consumerApplicationId", nativeQuery = true)
	public List<ApplicationDocument> findAllDataByConsumerApplicationId(Long consumerApplicationId);
	
	
	@Query(value = "select * from application_document  where CONSUMER_ID = :consumerID", nativeQuery = true)
	public ApplicationDocument findByconsumerApplicationDetail_consumer(
			@Param("consumerID") Long consumerID);


	@Query(value = "SELECT DOCUMENT_PATH \r\n"
			+ "FROM (\r\n"
			+ "    select up.DOCUMENT_PATH \r\n"
			+ "    from application_document ad \r\n"
			+ "    left join upload up \r\n"
			+ "    on ad.DOC_DEMAND_NOTE_FILE = up.UPLOAD_ID \r\n"
			+ "    where consumer_application_id = :consumerApplicationId \r\n"
			+ "    order by APPLICATION_DOCUMENT_ID desc\r\n"
			+ ") \r\n"
			+ "WHERE ROWNUM = 1", nativeQuery = true)
	public String findLatestDocument(Long consumerApplicationId);

	


}
