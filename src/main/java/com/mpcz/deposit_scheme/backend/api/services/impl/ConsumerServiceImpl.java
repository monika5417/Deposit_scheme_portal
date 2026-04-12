package com.mpcz.deposit_scheme.backend.api.services.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.domain.CheckConsumer;
import com.mpcz.deposit_scheme.backend.api.domain.Consumer;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerException;
import com.mpcz.deposit_scheme.backend.api.exception.DataNotFoundException;
import com.mpcz.deposit_scheme.backend.api.exception.DocumentTypeException;
import com.mpcz.deposit_scheme.backend.api.exception.InvalidAuthenticationException;
import com.mpcz.deposit_scheme.backend.api.jwt.security.util.PasswordUtil;
import com.mpcz.deposit_scheme.backend.api.repository.CheckConsumerRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerRepository;
import com.mpcz.deposit_scheme.backend.api.request.ConsumerSignUpForm;
import com.mpcz.deposit_scheme.backend.api.request.DataStatusDTO;
import com.mpcz.deposit_scheme.backend.api.request.SMSRequest;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerService;
import com.mpcz.deposit_scheme.backend.api.services.ConsumerTypeService;
import com.mpcz.deposit_scheme.backend.api.services.SMSDirectService;
import com.mpcz.deposit_scheme.backend.api.services.UploadService;
import com.mpcz.deposit_scheme.backend.api.util.MasterDataStatus;
import com.mpcz.deposit_scheme.backend.api.utility.MessageProperties;

@Service
public class ConsumerServiceImpl implements ConsumerService {

	Logger logger = LoggerFactory.getLogger(ConsumerServiceImpl.class);

	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private MessageProperties messageProperties;

	@Autowired
	private SMSDirectService smsDirectService;

	@Autowired
	private ConsumerTypeService consumerTypeService;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private CheckConsumerRepository CheckConsumerRepository;

	@Override
	public Consumer saveConsumer(ConsumerSignUpForm consumerSignUpForm)
			throws ConsumerException, DocumentTypeException {

		final String method = "ConsumerServiceImpl : saveConsumer()";
		logger.info(method);

		Response<Consumer> response = new Response<>();

		Consumer consumerResponse = null;

		if (Objects.isNull(consumerSignUpForm)) {
			logger.error("Consumer object is null");
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.NULL_OBJECT_MESSAGE);
			throw new ConsumerException(response);
		} else {

			/*********** validations start ***********************/

			if (consumerSignUpForm.getConsumerName() == null || consumerSignUpForm.getConsumerName().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Consumer Name");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getConsumerEmailId() == null || consumerSignUpForm.getConsumerEmailId().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Email Id");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getConsumerMobileNo() == null
					|| consumerSignUpForm.getConsumerMobileNo().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Mobile No");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getAddress() == null || consumerSignUpForm.getAddress().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Address");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getConsumerPassword() == null
					|| consumerSignUpForm.getConsumerPassword().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Password");
				throw new ConsumerException(response);
			}

			Consumer consumer = new Consumer();
			CheckConsumer checkConsumer = new CheckConsumer();

			final SMSRequest smsRequest = new SMSRequest();

			String consumerPassword = consumerSignUpForm.getConsumerPassword();

			checkConsumer.setConsumerNumber(consumerSignUpForm.getConsumerMobileNo());
			checkConsumer.setCheckConsumer(consumerPassword);

//			final String defaultConsumerPassword = Utility.getPassword(8);

			smsRequest.setText(MessageFormat.format(messageProperties.getDefaultPasswordMessage(),
					new Object[] { consumerSignUpForm.getConsumerName(), consumerPassword }));
			smsRequest.setMobileNo(consumerSignUpForm.getConsumerMobileNo());
			smsRequest.setTemplateId(messageProperties.getPasswordTemplateId());

			final String password = PasswordUtil.getPasswordHash(consumerPassword);

			consumer.setConsumerName(consumerSignUpForm.getConsumerName().trim());
			consumer.setConsumerLoginId(consumerSignUpForm.getConsumerMobileNo().trim());
			consumer.setConsumerMobileNo(consumerSignUpForm.getConsumerMobileNo().trim());
			consumer.setConsumerEmailId(consumerSignUpForm.getConsumerEmailId().trim());
			consumer.setActive(Boolean.TRUE);
			consumer.setAccountNonExpired(Boolean.FALSE);
			consumer.setAccountNonLocked(Boolean.FALSE);
			consumer.setIsFirstLogin(Boolean.TRUE);
			consumer.setCredentialsNonExpired(null);
			consumer.setConsumerCredentials(password);
			consumer.setLoginAttemp(0L);
			consumer.setCreated(new Date());
			consumer.setCreatedBy(1L);
			consumer.setUpdated(new Date());
			consumer.setUpdatedBy(1L);
			consumer.setLoginStatus("NEW REGISTRATION");
			consumer.setCredentialsNonExpired(Boolean.FALSE);

			consumer.setAddress(consumerSignUpForm.getAddress().trim());
			consumer.setConsumerTypeData(consumerSignUpForm.getConsumerType());

			consumerResponse = consumerRepository.save(consumer);
			CheckConsumerRepository.save(checkConsumer);

			try {
				smsDirectService.sendMessage(smsRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (Objects.isNull(consumerResponse)) {
				logger.error("repository.save(consumer) is returning Null");
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage(ResponseMessage.RECORD_INSERTION_FAILED_MESSAGE);
				throw new ConsumerException(response);
			} else {
				return consumerResponse;
			}

		}
	}

	@Override
	public Consumer findByConsumerLoginId(String consumerLoginId) throws DataNotFoundException {
		System.out.println(consumerLoginId);
		return this.consumerRepository.findByConsumerLoginId(consumerLoginId)
				.orElseThrow(() -> new DataNotFoundException(
						new Response<String>(ResponseCode.DATA_NOT_FOUND, ResponseMessage.CONSUMER_DETAILS_NOT_FOUND)));
	}

	@Override
	public Consumer findByConsumerLoginIdAndAccountNonLocked(String consumerLoginId, Boolean accountNonLocked)
			throws InvalidAuthenticationException {
		final String method = "ConsumerServiceImpl : findByConsumerLoginIdAndAccountNonLocked()";
		logger.info(method);
		return this.consumerRepository.findByConsumerLoginIdAndAccountNonLocked(consumerLoginId, accountNonLocked)
				.orElseThrow(() -> new InvalidAuthenticationException(
						new Response<String>(ResponseCode.ACCOUNT_LOCKED, ResponseMessage.ACCOUNT_LOCKED)));
	}

	@Override
	public Consumer findByConsumerLoginIdAndAccountNonExpired(String consumerLoginId, Boolean accountNonExpired)
			throws InvalidAuthenticationException {
		// TODO Auto-generated method stub
		return this.consumerRepository.findByConsumerLoginIdAndAccountNonExpired(consumerLoginId, accountNonExpired)
				.orElseThrow(() -> new InvalidAuthenticationException(
						new Response<String>(ResponseCode.ACCOUNT_EXPIRED, ResponseMessage.ACCOUNT_EXPIRED)));
	}

	@Override
	public Response<Consumer> update(Consumer consumer) throws ConsumerException {
		final String method = "ConsumerServiceImpl : update()";
		logger.info(method);

		final Response<Consumer> response = new Response<>();
		final Consumer consumers = consumerRepository.save(consumer);
		if (Objects.isNull(consumers)) {
			logger.error("consumerRepository.update(consumer) is returning Null");
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.RECORD_UPDATION_FAILED_MESSAGE);
			throw new ConsumerException(response);
		} else {
			logger.info("Response is returning successfully");
			response.setCode(ResponseCode.OK);
			response.setMessage(ResponseMessage.RECORD_UPDATED_MESSAGE);
			final List<Consumer> list = new ArrayList<>();
			list.add(consumers);
			response.setList(list);
			return response;
		}

	}

	@Override
	public Response<Consumer> save(Consumer consumer) throws ConsumerException {

		final String method = "ConsumerServiceImpl : save()";
		logger.info(method);

		boolean status = true;

		final Response<Consumer> response = new Response<>();
		Consumer consumerResponse = null;

		if (Objects.isNull(consumer)) {
			logger.error("Consumer object is null");
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.NULL_OBJECT_MESSAGE);
			throw new ConsumerException(response);
		} else {
			if (!consumer.isActive()) {
				status = false;
			}
			consumer.setAccountNonExpired(false);
			consumer.setAccountNonLocked(false);
			consumer.setCredentialsNonExpired(false);
			consumer.setIsFirstLogin(true);
			consumer.setLastLoggedInDate(new Date());
			consumer.setLoginAttemp(null);
			consumer.setLoginStatus("NA");
			consumer.setActive(status);
			consumerResponse = consumerRepository.save(consumer);
		}

		if (Objects.isNull(consumerResponse)) {
			logger.error("consumerRepository.save(consumer) is returning Null");
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.CREATE_USER_RECORD_ERROR_MESSAGE);
			throw new ConsumerException(response);
		} else {
			logger.info("Response is returning successfully");
			response.setCode(HttpCode.CREATED);
			response.setMessage(ResponseMessage.RECORD_INSERTED_MESSAGE);
			final List<Consumer> list = new ArrayList<>();
			list.add(consumerResponse);
			response.setList(list);
			return response;
		}

	}

	@Override
	public Consumer findByMobileNo(String consumerMobileNo) throws DataNotFoundException {
		return this.consumerRepository.findByConsumerMobileNo(consumerMobileNo)
				.orElseThrow(() -> new DataNotFoundException(
						new Response<String>(ResponseCode.DATA_NOT_FOUND, ResponseMessage.MOBILE_NO_NOT_FOUND)));
	}

	@Override
	public Consumer getCurrentLoginConsumerByLoginId() throws DataNotFoundException {

		String consumerLoginId = SecurityContextHolder.getContext().getAuthentication().getName();

//		String consumerLoginId = "9340302532";
		System.out.println("consumerLoginId-------->" + consumerLoginId);
		return this.consumerRepository.findByConsumerLoginId(consumerLoginId)
				.orElseThrow(() -> new DataNotFoundException(
						new Response<String>(ResponseCode.DATA_NOT_FOUND, ResponseMessage.CONSUMER_DETAILS_NOT_FOUND)));
	}

	@Override
	public String isConsumerRecordExist(DataStatusDTO dataStatusDTO) throws ConsumerException {

		final String method = "ConsumerServiceImpl : isConsumerRecordExist()";

		logger.info(method);

		List<Consumer> consumers = null;

		if (dataStatusDTO.getCrudType() == 1) {
			if (dataStatusDTO.getConsumerEmailId() != null) {
				consumers = consumerRepository.checkConsumerEmailId(dataStatusDTO.getConsumerEmailId());
			} else if (dataStatusDTO.getConsumerMobileNo() != null) {
				consumers = consumerRepository.checkConsumerMobileNo(dataStatusDTO.getConsumerMobileNo());
			} else if (dataStatusDTO.getAadharNo() != null) {
				consumers = consumerRepository.checkAadharNo(dataStatusDTO.getAadharNo());
			} else if (dataStatusDTO.getPanNo() != null) {
				consumers = consumerRepository.checkPanNo(dataStatusDTO.getPanNo());
			}
		}
//		else if (dataStatusDTO.getCrudType() == 2) {
//			if (dataStatusDTO.getConsumerEmailId() != null) {
//				consumers = consumerRepository.checkEmailForUpdate(dataStatusDTO.getConsumerEmailId(),
//						dataStatusDTO.getRecordId());
//			} else if (dataStatusDTO.getConsumerMobileNo() != null) {
//				consumers = consumerRepository.checkMobileNumberForUpdate(dataStatusDTO.getConsumerMobileNo(),
//						dataStatusDTO.getRecordId());
//			} else if (dataStatusDTO.getAadharNo() != null) {
//				consumers = consumerRepository.checkAadharNumberForUpdate(dataStatusDTO.getAadharNo(),
//						dataStatusDTO.getRecordId());
//			} else if (dataStatusDTO.getPanNo() != null) {
//				consumers = consumerRepository.checkUserIdForUpdate(dataStatusDTO.getPanNo(),
//						dataStatusDTO.getRecordId());
//			}
//		}
		if (Objects.isNull(consumers) || consumers.isEmpty()) {
			logger.error(
					"repository.checkEmail(dataStatusDTO.getConsumerEmailId()) or getAadharNo or getConsumerMobileNo or getPanNo is returning Null when isConsumerRecordExist call");
			return MasterDataStatus.NOT_EXISTS;
		} else {
			logger.info("Response is returning successfully");
			return MasterDataStatus.EXISTS;
		}
	}

//	25-02-2026 mutiple consumer signup
	@Override
	public Consumer consumerSignUpForMutipleConsumer(ConsumerSignUpForm consumerSignUpForm)
			throws ConsumerException, DocumentTypeException {

		final String method = "ConsumerServiceImpl : saveConsumer()";
		logger.info(method);

		Response<Consumer> response = new Response<>();

		Consumer consumerResponse = null;

		if (Objects.isNull(consumerSignUpForm)) {
			logger.error("Consumer object is null");
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.NULL_OBJECT_MESSAGE);
			throw new ConsumerException(response);
		} else {

			/*********** validations start ***********************/

			if (consumerSignUpForm.getConsumerName() == null || consumerSignUpForm.getConsumerName().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Consumer Name");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getConsumerEmailId() == null || consumerSignUpForm.getConsumerEmailId().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Email Id");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getConsumerMobileNo() == null
					|| consumerSignUpForm.getConsumerMobileNo().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Mobile No");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getAddress() == null || consumerSignUpForm.getAddress().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Address");
				throw new ConsumerException(response);
			}

			if (consumerSignUpForm.getConsumerPassword() == null
					|| consumerSignUpForm.getConsumerPassword().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Password");
				throw new ConsumerException(response);
			}
			if (consumerSignUpForm.getParentMobileNo() == null
					|| consumerSignUpForm.getParentMobileNo().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Parent Mobile No");
				throw new ConsumerException(response);
			}


			Consumer consumerExist = consumerRepository.findByConsumerLoginId(consumerSignUpForm.getConsumerMobileNo())
					.orElse(null);
			if (consumerExist != null) {
				throw new ConsumerException(
						new Response<>(HttpCode.NOT_ACCEPTABLE, "Consumer Already Exist With This Mobile No."));
			}

			Consumer consumer = new Consumer();
			CheckConsumer checkConsumer = new CheckConsumer();

			final SMSRequest smsRequest = new SMSRequest();

			String consumerPassword = consumerSignUpForm.getConsumerPassword();

			checkConsumer.setConsumerNumber(consumerSignUpForm.getConsumerMobileNo());
			checkConsumer.setCheckConsumer(consumerPassword);

			smsRequest.setText(MessageFormat.format(messageProperties.getDefaultPasswordMessage(),
					new Object[] { consumerSignUpForm.getConsumerName(), consumerPassword }));
			smsRequest.setMobileNo(consumerSignUpForm.getConsumerMobileNo());
			smsRequest.setTemplateId(messageProperties.getPasswordTemplateId());

			final String password = PasswordUtil.getPasswordHash(consumerPassword);

			consumer.setConsumerName(consumerSignUpForm.getConsumerName().trim());
			consumer.setConsumerLoginId(consumerSignUpForm.getConsumerMobileNo().trim());
			consumer.setConsumerMobileNo(consumerSignUpForm.getConsumerMobileNo().trim());
			consumer.setConsumerEmailId(consumerSignUpForm.getConsumerEmailId().trim());
			consumer.setActive(Boolean.TRUE);
			consumer.setAccountNonExpired(Boolean.FALSE);
			consumer.setAccountNonLocked(Boolean.FALSE);
			consumer.setIsFirstLogin(Boolean.TRUE);
			consumer.setCredentialsNonExpired(null);
			consumer.setConsumerCredentials(password);
			consumer.setLoginAttemp(0L);
			consumer.setCreated(new Date());
			consumer.setCreatedBy(1L);
			consumer.setUpdated(new Date());
			consumer.setUpdatedBy(1L);
			consumer.setLoginStatus("NEW REGISTRATION");
			consumer.setCredentialsNonExpired(Boolean.FALSE);
			consumer.setAddress(consumerSignUpForm.getAddress().trim());
			consumer.setConsumerTypeData(consumerSignUpForm.getConsumerType());

			if (consumerSignUpForm.getParentMobileNo() != null) {

				// Parent dhundo
				Consumer parentConsumer = consumerRepository
						.findByConsumerLoginId(consumerSignUpForm.getParentMobileNo())
						.orElseThrow(() -> new ConsumerException(new Response<>(HttpCode.NULL_OBJECT,
								"Parent Mobile No. not exist: " + consumerSignUpForm.getParentMobileNo())));

				// ✅ Fix 1 — Chain prevention (Parent khud Child nahi hona chahiye)
				if (parentConsumer.getChildOf() != null && parentConsumer.getChildOf()>0) {
					throw new ConsumerException(new Response<>(HttpCode.NOT_ACCEPTABLE,
							"This consumer is already a Child, cannot be used as Parent :" +parentConsumer.getConsumerMobileNo() ));
				}

				// ✅ Fix 2 — Long → Integer cast (agar childOf Integer hai entity mein)
				consumer.setChildOf(parentConsumer.getConsumerId());

				// ✅ Fix 3 — Redundant null check hataya
				logger.info("Child consumer linked to Parent Mobile: {}", consumerSignUpForm.getParentMobileNo());
			}

			consumerResponse = consumerRepository.save(consumer);
			CheckConsumerRepository.save(checkConsumer);

			try {
				smsDirectService.sendMessage(smsRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (Objects.isNull(consumerResponse)) {
				logger.error("repository.save(consumer) is returning Null");
				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage(ResponseMessage.RECORD_INSERTION_FAILED_MESSAGE);
				throw new ConsumerException(response);
			} else {
				System.err.println("aaaaaaaaa : " + new Gson().toJson(consumerResponse));
				return consumerResponse;
			}

		}
	}

}
