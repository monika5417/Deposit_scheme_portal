package com.mpcz.deposit_scheme.backend.api.services.impl;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.mpcz.deposit_scheme.backend.api.constant.ConstantProperty;
import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.domain.Consumer;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
import com.mpcz.deposit_scheme.backend.api.domain.ConsumerLoginToken;
import com.mpcz.deposit_scheme.backend.api.domain.DistributionCenter;
import com.mpcz.deposit_scheme.backend.api.domain.NatureOfWork;
import com.mpcz.deposit_scheme.backend.api.domain.SchemeType;
import com.mpcz.deposit_scheme.backend.api.dto.SSPDto;
import com.mpcz.deposit_scheme.backend.api.enums.ApplicationStatusEnum;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerException;
import com.mpcz.deposit_scheme.backend.api.exception.DistributionCenterException;
import com.mpcz.deposit_scheme.backend.api.exception.DocumentTypeException;
import com.mpcz.deposit_scheme.backend.api.jwt.security.JwtConsumer;
import com.mpcz.deposit_scheme.backend.api.jwt.security.JwtTokenUtil;
import com.mpcz.deposit_scheme.backend.api.jwt.security.util.PasswordUtil;
import com.mpcz.deposit_scheme.backend.api.repository.ApplicationStatusRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
import com.mpcz.deposit_scheme.backend.api.repository.ConsumerRepository;
import com.mpcz.deposit_scheme.backend.api.repository.DistributionCenterRepository;
import com.mpcz.deposit_scheme.backend.api.repository.NatureOfWorkRepository;
import com.mpcz.deposit_scheme.backend.api.repository.SchemeTypeRepository;
import com.mpcz.deposit_scheme.backend.api.request.SMSRequest;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.response.SignUpResponse;
import com.mpcz.deposit_scheme.backend.api.services.SMSDirectService;
import com.mpcz.deposit_scheme.backend.api.services.SSPService;
import com.mpcz.deposit_scheme.backend.api.utility.MessageProperties;

@Service
public class SSPServiceImpl implements SSPService {

	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private MessageProperties messageProperties;

	@Autowired
	private SMSDirectService smsDirectService;

	@Autowired
	private ConsumerApplictionDetailRepository consumerApplictionDetailRepository;

	@Autowired
	private NatureOfWorkRepository natureOfWorkRepository;

	@Autowired
	private SchemeTypeRepository schemeTypeRepository;

	@Autowired
	private DistributionCenterRepository distributionCenterRepository;
	
	@Autowired
	private ApplicationStatusRepository applicationStatusRepository;

	@Override
	public SignUpResponse sspSignUp(SSPDto sspDto, HttpServletRequest request)
			throws ConsumerException, DocumentTypeException, DistributionCenterException {
		// TODO Auto-generated method stub
		ConsumerApplicationDetail consumerAppDetail = consumerApplictionDetailRepository
				.findByNscApplicationNo(sspDto.getNscApplicationNo());
		if (consumerAppDetail != null) {
			Response<Consumer> response = new Response<>();
			response.setCode(HttpCode.NOT_ACCEPTABLE);
			response.setMessage("NSC Application already exist");
			throw new ConsumerException(response);
		}
		Optional<Consumer> consumer = consumerRepository.findByConsumerLoginId(sspDto.getMobileNumber());
		validateDto(sspDto);
		if (consumer.isPresent()) {

			String consumerCredentials = consumer.get().getConsumerCredentials();

			ResponseEntity<Response> checkCredentials = checkCredentials(consumer.get(), request);
			System.out.println("tokkkkkkkkkkkkken   : " + checkCredentials.getBody().getToken());
			Optional<Consumer> consumer3 = consumerRepository.findByConsumerLoginId(sspDto.getMobileNumber());
			System.out.println("consumerCredentials   : " + consumerCredentials);

			Consumer consumer10 = consumer3.get();
			consumer10.setConsumerCredentials(consumerCredentials);
			Consumer updatedConsumer = consumerRepository.save(consumer10);
			sspDto.setConsumerName(updatedConsumer.getConsumerName());
			sspDto.setEmailId(updatedConsumer.getConsumerEmailId());
			sspDto.setAddress(updatedConsumer.getAddress());
			saveConsumerApplication(sspDto);

			return new SignUpResponse(updatedConsumer, checkCredentials.getBody().getToken());
		} else {

			SignUpResponse saveConsumerBySSP = saveConsumerBySSP(sspDto, request);
			saveConsumerApplication(sspDto);
			return saveConsumerBySSP;
		}

	}

	public SignUpResponse saveConsumerBySSP(SSPDto sspDto, HttpServletRequest request)
			throws ConsumerException, DocumentTypeException {

		Response<Consumer> response = new Response<>();

		Consumer consumer = new Consumer();

		String consumerPassword = generatePassword();
		consumer.setConsumerCredentials(PasswordUtil.getPasswordHash(consumerPassword));

		final String password = PasswordUtil.getPasswordHash(consumerPassword);
		consumer.setConsumerName(sspDto.getConsumerName().trim());
		consumer.setConsumerLoginId(sspDto.getMobileNumber().trim());
		consumer.setConsumerMobileNo(sspDto.getMobileNumber().trim());
		consumer.setConsumerEmailId(sspDto.getEmailId().trim());
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

		consumer.setAddress(sspDto.getAddress().trim());

		Consumer savedConsumer = consumerRepository.save(consumer);

		String token = getToken(savedConsumer, consumerPassword, request);

		sendPasswordSMS(sspDto, consumerPassword);

		return new SignUpResponse(savedConsumer, token);

	}

	public ResponseEntity<Response> checkCredentials(Consumer consumer1, HttpServletRequest request) {
		Response response = new Response();

		System.out.println("consumerVerificationCaptcha method calling!!!");

		String orginalpassword = consumer1.getConsumerCredentials();

		consumer1.setConsumerCredentials("$2a$10$GvpWoJdgmP1YziOVXUUF5ewrtCTFR16XM2/vT09u6vh8fR2eqyt5i");
		String consumerLoginId = consumer1.getConsumerLoginId();
		String consumerLoginPwd = "Pass@1234";

		Consumer save = consumerRepository.save(consumer1);
		try {
			if (consumerLoginId == null || consumerLoginId.isEmpty()) {
				response.setCode("400");
				response.setMessage("Id should not be null");
				return ResponseEntity.ok().body(response);
			}
			if (consumerLoginPwd == null || consumerLoginPwd.isEmpty()) {
				response.setCode("400");
				response.setMessage("Password should not be null");
				return ResponseEntity.ok().body(response);
			}

			Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
			Consumer consumer = null;
			Optional<Consumer> consumerDetail = consumerRepository.findByConsumerLoginId(consumerLoginId);

			if (!consumerDetail.isPresent()) {
				response.setCode("404");
				response.setMessage("Consumer not found");
				return ResponseEntity.ok().body(response);
			}

			if (BCrypt.checkpw(consumerLoginPwd, consumerDetail.get().getConsumerCredentials())) {
				ConsumerLoginToken consumerLoginToken = new ConsumerLoginToken();

				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(consumerLoginId + ":CONSUMER", consumerLoginPwd));
				final JwtConsumer jwtConsumer = (JwtConsumer) authentication.getPrincipal();

				SecurityContextHolder.getContext().setAuthentication(authentication);
				final String token = jwtTokenUtil.generateToken(jwtConsumer, null, request);
				System.out.println("bbbbbbbbbbbbbbbb : " + token);
				final HttpHeaders headers = new HttpHeaders();
				headers.set(ConstantProperty.CONTENT_TYPE, ConstantProperty.APPLICATION_JSON);
				headers.set("authorization", token);
				headers.set("Access-Control-Expose-Headers", "authorization");

				response.setCode("200");
				response.setMessage("Login Successfully");
				response.setToken(token);
				response.setList(Arrays.asList(consumerDetail));

				return ResponseEntity.ok().headers(headers).body(response);
			} else {
				response.setCode("404");
				response.setMessage("Invalid credentials");
				return ResponseEntity.ok().body(response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setCode("500");
			response.setMessage(e.getMessage());
			return ResponseEntity.ok().body(response);
		}

	}

	public String getToken(Consumer consumer, String consumerPassword, HttpServletRequest request) {
		System.out.println("Provided Password: " + consumerPassword);

		// Check if the provided password matches the stored hashed password
		if (BCrypt.checkpw(consumerPassword, consumer.getConsumerCredentials())) {
			// Authenticate the user
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					consumer.getConsumerLoginId() + ":CONSUMER", consumerPassword));

			JwtConsumer jwtConsumer = (JwtConsumer) authentication.getPrincipal();
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Generate JWT token
			String token = jwtTokenUtil.generateToken(jwtConsumer, null, request);
			System.out.println("Generated Token: " + token);

			return token;
		}

		return "Credentials not matched";
	}

	private String generatePassword() {
		String uuid = UUID.randomUUID().toString();
		String numberPart = uuid.replaceAll("[^0-9]", "").substring(0, 4);
		return "Pass#" + numberPart;
	}

	private void sendPasswordSMS(SSPDto sspDto, String consumerPassword) {
		try {
			SMSRequest smsRequest = new SMSRequest();
			smsRequest.setText(MessageFormat.format(messageProperties.getDefaultPasswordMessage(),
					sspDto.getConsumerName(), consumerPassword));
			smsRequest.setMobileNo(sspDto.getMobileNumber());
			smsRequest.setTemplateId(messageProperties.getPasswordTemplateId());
			smsRequest.setHinglish(1L);
			// Send SMS (uncomment the service call if necessary)
			smsDirectService.sendMessage(smsRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveConsumerApplication(SSPDto sspDto) throws DistributionCenterException {
		NatureOfWork natureOfWork = null;
		Optional<Consumer> consumer = consumerRepository.findByConsumerLoginId(sspDto.getMobileNumber());
		Consumer consumerData = consumer.get();

		SchemeType schemeType = schemeTypeRepository.findById(sspDto.getSchemeType()).get();

		if (sspDto.getNatureOfWork().equals(5L)) {
			natureOfWork = natureOfWorkRepository.findById(sspDto.getNatureOfWork()).get();
		} else {
			natureOfWork = natureOfWorkRepository.findById(2L).get();
		}

		long uniqueCurrentTimeMS = uniqueCurrentTimeMS();
		String applicationNumber = String.valueOf(uniqueCurrentTimeMS);

		System.out.println("applictionfullname  : " + applicationNumber);
		String prefix = schemeType.getSchemeTypeId().equals(1L) ? "SV" : "DS";
		String consumerAppNo = prefix + applicationNumber;

		DistributionCenter distributionCenter = distributionCenterRepository
				.findDistributionCenterByNgbDcCd(sspDto.getDcCode());
		if(distributionCenter==null) {
			Response<Consumer> response = new Response<>();
			response.setCode(HttpCode.NOT_ACCEPTABLE);
			response.setMessage("Given Dc not found in database");
			throw new DistributionCenterException(response);
		}

		ConsumerApplicationDetail cad = new ConsumerApplicationDetail();
		cad.setConsumers(consumerData);
		cad.setConsumerApplicationNo(consumerAppNo);
		cad.setConsumerName(sspDto.getConsumerName());
		cad.setAddress(sspDto.getAddress());
		cad.setConsumerphonNumber(sspDto.getMobileNumber());
		cad.setNscApplicationNo(sspDto.getNscApplicationNo());
		cad.setSspPortalName("SSP Portal");
		cad.setNatureOfWorkType(natureOfWork);
		cad.setSchemeType(schemeType);
		cad.setDistributionCenter(distributionCenter);
		cad.setApplicationStatus(applicationStatusRepository
					.findById(ApplicationStatusEnum.PENDING_FOR_GIS_LOCATION.getId()).get());
		consumerApplictionDetailRepository.save(cad);

	}

	private static final AtomicLong LAST_TIME_MS = new AtomicLong();

	public static long uniqueCurrentTimeMS() {
		long now = System.currentTimeMillis();
		while (true) {
			long lastTime = LAST_TIME_MS.get();
			if (lastTime >= now)
				now = lastTime + 1;
			if (LAST_TIME_MS.compareAndSet(lastTime, now))
				return now;
		}
	}

	public void validateDto(SSPDto sspDto) throws ConsumerException {
		Response<Consumer> response = new Response<>();

		if (Objects.isNull(sspDto)) {
			response.setCode(HttpCode.NULL_OBJECT);
			response.setMessage(ResponseMessage.NULL_OBJECT_MESSAGE);
			throw new ConsumerException(response);
		} else {
			/*********** validations start ***********************/

			if (sspDto.getConsumerName() == null || sspDto.getConsumerName().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Consumer Name");
				throw new ConsumerException(response);
			}

			if (sspDto.getEmailId() == null || sspDto.getEmailId().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Email Id");
				throw new ConsumerException(response);
			}

			if (sspDto.getMobileNumber() == null || sspDto.getMobileNumber().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Mobile No");
				throw new ConsumerException(response);
			}

			if (sspDto.getAddress() == null || sspDto.getAddress().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Please fill Address");
				throw new ConsumerException(response);
			}

			if (sspDto.getNscApplicationNo() == null || sspDto.getNscApplicationNo().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("SSP Application No. Can not be null");
				throw new ConsumerException(response);
			}
			
			if (sspDto.getNatureOfWork() == null) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Nature Of Work Can not be null");
				throw new ConsumerException(response);
			}
			
			if (sspDto.getSchemeType() == null) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Scheme Type Can not be null");
				throw new ConsumerException(response);
			}
			
			if (sspDto.getDcCode() == null || sspDto.getDcCode().equals("")) {

				response.setCode(HttpCode.NULL_OBJECT);
				response.setMessage("Dc Code Can not be null");
				throw new ConsumerException(response);
			}
		}
	}

}
