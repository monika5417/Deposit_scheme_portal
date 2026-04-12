package com.mpcz.deposit_scheme.feignClient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mpcz.deposit_scheme.backend.api.response.SSPResponse;

@FeignClient(name="ssp-api", url="${ssp.api.url}")
public interface SSPFeignClientAPI {
	
	
	@PostMapping(
		    value = "/department/post/application/work/status/ssp",
		    consumes = "application/json"
		)
	public ResponseEntity<SSPResponse> sendNDSchemeDataToSSP(@RequestBody Map<String, Object> sspData);
	
	
	

}
