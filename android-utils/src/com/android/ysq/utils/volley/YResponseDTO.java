package com.android.ysq.utils.volley;

public abstract class YResponseDTO {

	/**
	 * 状态码，用于服务器返回操作结果判断
	 */
	private int status_code;
	/**
	 * 结果信息，用户服务器返回操作结果信息，如成功或者失败原因
	 */
	private String message;

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return 根据装状态码判断操作是否成功，默认当status_code == 1时返回true，各个子类可以自己重写此方法
	 */
	public boolean isSuccess() {
		return 1 == status_code;
	}

	abstract public Object getData();
	
}
