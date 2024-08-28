package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mpcz.deposit_scheme.backend.api.domain.MmkyPayAmount;

@Repository
public interface MmkyPayAmountRespository extends JpaRepository<MmkyPayAmount, Long>{

	MmkyPayAmount findByConsumerApplicationNumber(String consumerAppNo);
	
	@Query(value ="select * from MKY_PAY_AMNT where MSG_SEND='Unsend'",nativeQuery = true)
	List<MmkyPayAmount> findAllUnsendMsg();

}
