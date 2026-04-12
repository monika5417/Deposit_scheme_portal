/**
 * 
 */
package com.mpcz.deposit_scheme.backend.api.dto;

import java.util.Map;

/**
 * @author Monika Rajpoot
 * @since 12-Mar-2026
 * @description DynamicExcelRequest.java class description
 */

public class DynamicExcelRequest {
	
	
	private String queryName;
	private Map<String,Object> params;
	private String sheetName;
	private String fileName;
	
	
	
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	
	
}
