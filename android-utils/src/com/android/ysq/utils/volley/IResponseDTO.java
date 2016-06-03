package com.android.ysq.utils.volley;

public interface IResponseDTO {

	public boolean isSuccess();
	public String getErrorResult();
	public Object getSuccessResult();
}
