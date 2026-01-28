package com.mpcz.deposit_scheme.backend.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mpcz.deposit_scheme.backend.api.domain.Kva63Dtr;

@Repository
public interface Kva63DtrRepository extends JpaRepository<Kva63Dtr, Integer> {

	@Query(value="select * from KVA_63_DTR",nativeQuery=true)
	List<Kva63Dtr> findAllData();

}
