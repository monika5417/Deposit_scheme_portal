package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationSurvey;

@Repository
public interface ConsumerApplicationSurveyRepository extends JpaRepository<ConsumerApplicationSurvey, Long> {

	
	Optional<ConsumerApplicationSurvey>  findByConsumersApplicationDetailConsumerApplicationId(Long id);
	
 
	
}
