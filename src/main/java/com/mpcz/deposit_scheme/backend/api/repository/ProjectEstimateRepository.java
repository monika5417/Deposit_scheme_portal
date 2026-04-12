package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mpcz.deposit_scheme.backend.api.domain.ProjectEstimate;

public interface ProjectEstimateRepository extends JpaRepository<ProjectEstimate, Long> {

	ProjectEstimate findByProjectNumber(String erpNo);

}
