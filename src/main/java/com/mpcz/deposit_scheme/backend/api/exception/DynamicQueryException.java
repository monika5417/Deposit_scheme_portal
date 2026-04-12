/**
 * 
 */
package com.mpcz.deposit_scheme.backend.api.exception;

import com.mpcz.deposit_scheme.backend.api.response.Response;

/**
 * @author Monika Rajpoot
 * @since 17-Feb-2026
 * @description DynamicQueryException.java class description
 */

public class DynamicQueryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Response response;
	
	
	public DynamicQueryException(final Response response) {
		super(response.getMessage());
		this.response=response;
		
	}


	public Response getResponse() {
		return response;
	}


	public void setResponse(Response response) {
		this.response = response;
	}



	

}
