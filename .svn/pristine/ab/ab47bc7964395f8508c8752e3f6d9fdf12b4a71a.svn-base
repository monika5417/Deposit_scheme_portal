package com.mpcz.deposit_scheme.backend.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mpcz.deposit_scheme.backend.api.domain.ApplicationStatus;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.WorkCompletionChangStautsByDgmOAndM;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.repository.WorkCompletionChangStautsByDgmOAndMRepository;
import com.mpcz.deposit_scheme.backend.api.services.ApplicationStatusService;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerApplicationDetailService;
import com.mpcz.deposit_scheme.backend.api.services.WorkCompletionChangStautsByDgmOAndMService;
@Service
public class WorkCompletionChangStautsByDgmOAndMServiceIMp implements WorkCompletionChangStautsByDgmOAndMService {
	
	@Autowired
	private WorkCompletionChangStautsByDgmOAndMRepository workCompletionChangStautsByDgmOAndMService;
	
	@Autowired
	private ConsumerApplicationDetailService consumerApplicationDetailService;
	
	@Autowired
	private ApplicationStatusService applicationStatusService;

	@Override
	public WorkCompletionChangStautsByDgmOAndM saveAndChange(
			WorkCompletionChangStautsByDgmOAndM workCompletionChangStautsByDgmOAndM) throws ConsumerApplicationDetailException {
		
		ApplicationStatus applicationStatus = null;
		try {
			if(workCompletionChangStautsByDgmOAndM.getWorkComplationChangedByDgmOrendum().equalsIgnoreCase("false")) {
				ConsumerApplicationDetail applicationDetail=consumerApplicationDetailService.findByConsumerApplicationId(workCompletionChangStautsByDgmOAndM.getConsumerApplicationId());
				if(applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId()==2L 
						|| applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId()==5L
						||applicationDetail.getNatureOfWorkType().getNatureOfWorkTypeId()==8L) {
					 applicationStatus=applicationStatusService
							.findById(ApplicationStatusEnum.WORK_FINISHED_NOW_PENDING_FOR_CONNECTION_PUNCHING.getId());
				}else {
				 applicationStatus=applicationStatusService
						.findById(ApplicationStatusEnum.WORK_FINISED.getId());
				}
				applicationDetail.setDateOfDgmOandM(workCompletionChangStautsByDgmOAndM.getDateOfDgmOandM());
				applicationDetail.setApplicationStatus(applicationStatus);
				consumerApplicationDetailService.saveConsumerApplication(applicationDetail);
			}else {
				ConsumerApplicationDetail applicationDetail=consumerApplicationDetailService.findByConsumerApplicationId(workCompletionChangStautsByDgmOAndM.getConsumerApplicationId());
				 applicationStatus=applicationStatusService
						.findById(ApplicationStatusEnum.PENDING_FOR_CONFIRMATION_OF_WORK_COMPLETION_BY_DGM_STC.getId());
				applicationDetail.setDateOfDgmOandM(workCompletionChangStautsByDgmOAndM.getDateOfDgmOandM());
				applicationDetail.setApplicationStatus(applicationStatus);
				consumerApplicationDetailService.saveConsumerApplication(applicationDetail);
			}
		} catch (ConsumerApplicationDetailException e) {
			
			e.printStackTrace();
		}
		
		return workCompletionChangStautsByDgmOAndMService.save(workCompletionChangStautsByDgmOAndM);
	}

}
