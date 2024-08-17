package com.mpcz.deposit_scheme.backend.api.services;

import java.util.List;

import com.mpcz.deposit_scheme.backend.api.domain.VendorWorkOrder;
import com.mpcz.deposit_scheme.backend.api.response.Response;

public interface VendorWorkOrderService {
	
	public VendorWorkOrder save(VendorWorkOrder vendorWorkOrder) ;

	public VendorWorkOrder update(VendorWorkOrder vendorWorkOrder);
	
	public Boolean delete(Long workOrderId);// throws RegionException;

	public VendorWorkOrder findById(Long workOrderId);// throws RegionException;
	
	public List<VendorWorkOrder> findAll();// throws RegionException;
	
	public VendorWorkOrder findByApplicationNo(String applicationNo);


} 
