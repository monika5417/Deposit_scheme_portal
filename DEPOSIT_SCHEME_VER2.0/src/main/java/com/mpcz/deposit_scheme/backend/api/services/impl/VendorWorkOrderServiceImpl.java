package com.mpcz.deposit_scheme.backend.api.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.VendorWorkOrder;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.VendorWorkOrderRepository;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationStatusService;
import com.mpcz.deposit_scheme.backend.api.services.VendorWorkOrderService;

@Service
public class VendorWorkOrderServiceImpl implements VendorWorkOrderService {

	@Autowired
	private VendorWorkOrderRepository vendorWorkOrderRepository;

	@Autowired
	private ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@Autowired
	private ApplicationStatusService applicationStatusService;

	@Override
	public VendorWorkOrder save(VendorWorkOrder vendorWorkOrder) {
		// TODO Auto-generated method stub
		ConsumerApplicationDetail consumerApplicationDetail = consumerApplictionDetailRepository
				.findByConsumerApplicationNumber(vendorWorkOrder.getConsumerApplicationNo());
		ApplicationStatus appStatus =null;
		VendorWorkOrder vendorWorkOrders = new VendorWorkOrder();

		int year = LocalDate.now().getYear();
		String valueOf = String.valueOf(year);
		int previousYear = year - 1;
		vendorWorkOrders.setCurrentYear(valueOf);
		vendorWorkOrders.setPreviousYear(String.valueOf(previousYear));
		vendorWorkOrders.setConsumerApplicationNo(vendorWorkOrder.getConsumerApplicationNo());
		vendorWorkOrders.setDgmStcId(vendorWorkOrder.getDgmStcId());
		vendorWorkOrders.setWorkOrderDate(vendorWorkOrder.getWorkOrderDate());
		vendorWorkOrders.setWorkOrderNo(vendorWorkOrder.getWorkOrderNo());
		vendorWorkOrders.setWorkOrderGeneratedRoleCode(vendorWorkOrder.getWorkOrderGeneratedRoleCode());

		
		
		ApplicationStatus applicationStatus = applicationStatusService
				.findById(ApplicationStatusEnum.PENDING_FOR_WORK_COMPLETION.getId());
		consumerApplicationDetail.setApplicationStatus(applicationStatus);
		if(consumerApplicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId().equals(8L) 
				|| consumerApplicationDetail.getSchemeType().getSchemeTypeId().equals(2L)) {
			 appStatus = applicationStatusService.findById(ApplicationStatusEnum.PENDING_FOR_CONTRACTOR_SELECTION_AFTER_TENDER_BY_DGM_STC.getId());
			consumerApplicationDetail.setApplicationStatus(appStatus);
		}

		else {
			appStatus = applicationStatusService
					.findById(ApplicationStatusEnum.PENDING_FOR_WORK_COMPLETION.getId());
		
		}

		consumerApplicationDetail.setApplicationStatus(appStatus);
		
		
		VendorWorkOrder findByApplicationNo = vendorWorkOrderRepository.findByApplicationNo(vendorWorkOrder.getConsumerApplicationNo());
		if(findByApplicationNo!=null) {
			vendorWorkOrders.setWoVersion("V1");
			appStatus = applicationStatusService
					.findById(ApplicationStatusEnum.PENDING_FOR_WORK_COMPLETION.getId());
			consumerApplicationDetail.setApplicationStatus(appStatus);
			consumerApplictionDetailRepository.save(consumerApplicationDetail);
			vendorWorkOrderRepository.save(vendorWorkOrders);
		}
		consumerApplictionDetailRepository.save(consumerApplicationDetail);
		return vendorWorkOrderRepository.save(vendorWorkOrders);

	}

	@Override
	public VendorWorkOrder update(VendorWorkOrder vendorWorkOrder) {
		// TODO Auto-generated method stub
		return vendorWorkOrderRepository.save(vendorWorkOrder);
	}

	@Override
	public Boolean delete(Long workOrderId) {
		// TODO Auto-generated method stub
		vendorWorkOrderRepository.deleteById(workOrderId);
		return Boolean.TRUE;
	}

	@Override
	public VendorWorkOrder findById(Long workOrderId) {
		// TODO Auto-generated method stub
		return vendorWorkOrderRepository.findById(workOrderId).orElseThrow(null);
	}

	@Override
	public List<VendorWorkOrder> findAll() {
		// TODO Auto-generated method stub
		return vendorWorkOrderRepository.findAll();
	}

	@Override
	public VendorWorkOrder findByApplicationNo(String applicationNo) {
		// TODO Auto-generated method stub
		return vendorWorkOrderRepository.findByApplicationNo(applicationNo);
	}

}
