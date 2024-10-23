package com.mpcz.deposit_scheme.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mpcz.deposit_scheme.backend.api.domain.RefundAmount;

public interface RefundAmountRepository extends JpaRepository<RefundAmount, Long> {

	RefundAmount findByConsumerApplicationNo(String consumerApplicationNo);

}
