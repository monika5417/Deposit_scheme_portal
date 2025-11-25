//package com.mpcz.deposit_scheme.testcontroller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//
//import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
//import com.mpcz.deposit_scheme.backend.api.constant.ResponseCode;
//import com.mpcz.deposit_scheme.backend.api.controller.NgbDataController;
//import com.mpcz.deposit_scheme.backend.api.domain.BillDeskPaymentResponse;
//import com.mpcz.deposit_scheme.backend.api.domain.Consumer;
//import com.mpcz.deposit_scheme.backend.api.domain.ConsumerApplicationDetail;
//import com.mpcz.deposit_scheme.backend.api.domain.DistributionCenter;
//import com.mpcz.deposit_scheme.backend.api.domain.District;
//import com.mpcz.deposit_scheme.backend.api.repository.BillPaymentResponseeeeeeeRepository;
//import com.mpcz.deposit_scheme.backend.api.repository.ConsumerApplictionDetailRepository;
//import com.mpcz.deposit_scheme.backend.api.response.Response;
//
//@ExtendWith(MockitoExtension.class)
//class GetOytDataForTemporaryConnectionTest {
//
//    @InjectMocks
//    private NgbDataController controller;
//
//    @Mock
//    private ConsumerApplictionDetailRepository consumerRepo;
//
//    @Mock
//    private BillPaymentResponseeeeeeeRepository billRepo;
//
//    @Test
//    void testConsumerNotFound() {
//        when(consumerRepo.findByConsumerApplicationNumber("SV1763450336774")).thenReturn(null);
//
//        ResponseEntity<Response> response = controller.getOytDataForTemporaryConnection("SV1763450336774");
//
//        assertEquals(HttpCode.NULL_OBJECT, response.getBody().getCode());
//        assertEquals("Consumer Application not found", response.getBody().getMessage());
//    }

//    @Test
//    void testBillDeskEmpty() {
//        ConsumerApplicationDetail cad = new ConsumerApplicationDetail();
//        cad.setCreated(new Date());
//        Consumer consumer = new Consumer();
//        consumer.setConsumerMobileNo("9876543210");
//
//        District district = new District();
//        district.setDistrictName("Indore");
//
//        DistributionCenter dc = new DistributionCenter();
//        dc.setDcName("Anand Nagar");
//        cad.setConsumers(consumer);
//        cad.setDistrict(district);
//        cad.setDistributionCenter(dc);
//
//        cad.setJeLoad("5");
//        cad.setJeLoadUnitKwYaKva("KW");
//
//        when(consumerRepo.findByConsumerApplicationNumber("APP123")).thenReturn(cad);
//        when(billRepo.getTotalPaymentDetails("APP123")).thenReturn(Collections.emptyList());
//
//        ResponseEntity<Response> response = controller.getOytDataForTemporaryConnection("APP123");
//
//        assertEquals(HttpCode.NULL_OBJECT, response.getBody().getCode());
//        assertEquals("Consumer Application not found in BillDesk Payment Response",
//                     response.getBody().getMessage());
//    }
//
//    @Test
//    void testSuccessScenario() {
//        ConsumerApplicationDetail cad = new ConsumerApplicationDetail();
//        cad.setCreated(new Date());
//        cad.setConsumerName("Amit");
//        cad.setGuardianName("Ram");
//        cad.setCastCategory("GEN");
//        cad.setAadharNo("1234");
//        cad.setAddress("Address 1");
//        cad.setPincode("452001");
//        cad.setDistrict(new District("Indore"));
//        cad.setDistributionCenter(new DistributionCenter("123", "456"));
//        cad.setConsumers(new Consumer("9876543210"));
//        cad.setKhasra("9090");
//        cad.setJeLoad("5");
//        cad.setJeLoadUnitKwYaKva("KW");
//
//        BillDeskPaymentResponse payment = new BillDeskPaymentResponse();
//        payment.setAdditionalInfo("Registration_Fees");
//        payment.setAuth_status("0300");
//        payment.setTranId("ABCDEFGHIJ1234567890");
//        payment.setOytTempAmount("500");
//
//        when(consumerRepo.findByConsumerApplicationNumber("APP123")).thenReturn(cad);
//        when(billRepo.getTotalPaymentDetails("APP123"))
//                .thenReturn(Arrays.asList(payment));
//
//        ResponseEntity<Response> response = controller.getOytDataForTemporaryConnection("APP123");
//
//        assertEquals(ResponseCode.OK, response.getBody().getCode());
//        assertEquals("SUCCESS", response.getBody().getMessage());
//        assertEquals(1, response.getBody().getList().size());
//    }
//
//    @Test
//    void testExceptionScenario() {
//        when(consumerRepo.findByConsumerApplicationNumber("APP123"))
//                .thenThrow(new RuntimeException("DB error"));
//
//        ResponseEntity<Response> response = controller.getOytDataForTemporaryConnection("APP123");
//
//        assertEquals("500", response.getBody().getCode());
//        assertEquals("Internal Server Error", response.getBody().getMessage());
//    }
//}

