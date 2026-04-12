package com.mpcz.deposit_scheme.backend.api.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mpcz.deposit_scheme.backend.api.domain.ProjectEstimate;

public class ProjectEstimateApiResponse {

    @JsonProperty("data")
    private List<ProjectEstimate> data;

    @JsonProperty("list")
    private Object list;

    @JsonProperty("statusCode")
    private String statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error")
    private String error;

	public List<ProjectEstimate> getData() {
		return data;
	}

	public void setData(List<ProjectEstimate> data) {
		this.data = data;
	}

	public Object getList() {
		return list;
	}

	public void setList(Object list) {
		this.list = list;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
 
    
    
    
}