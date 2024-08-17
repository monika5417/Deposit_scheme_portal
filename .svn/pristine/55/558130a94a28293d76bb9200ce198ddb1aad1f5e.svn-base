package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mpcz.deposit_scheme.backend.api.domain.ApplicationDocument;

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
	
}
