package com.android.ysq.utils.volley;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.ysq.utils.YJsonUtil;
import com.android.ysq.utils.logger.Logger;

import android.app.Application;

final public class VolleyService {

	private static final int DEFAULT_TIMEOUT_MILLISECOND = 30 * 1000;

	private static VolleyService mSelf;
	private RequestQueue mQueue;

	public interface VolleyServiceCallBack {
		public void onRequestSuccess(String urlAndRequestKey, Object response);

		public void onRequestFailed(String urlAndRequestKey, YResponseError error);
	}

	private VolleyService() {
	}

	public static synchronized VolleyService getInstance(Application context) {
		if (mSelf == null) {
			mSelf = new VolleyService();
			mSelf.mQueue = Volley.newRequestQueue(context);
		}
		return mSelf;
	}

	/**
	 * 调用
	 * {@link #requestObject(int, String, String, Map, VolleyServiceCallBack, Class)}
	 * , 其中第一个参数为{@link com.android.volley.Request.Method#POST}
	 * 
	 * @param requestKey
	 *            请求关键字
	 * @param url
	 *            请求地址
	 * @param map
	 *            参数
	 * @param callBack
	 *            请求结果回调方法
	 * @param clazz
	 *            请求返回数据的结果类型
	 */
	public void requestObject(final String requestKey, String url, final Map<String, String> map,
			final VolleyServiceCallBack callBack, final Class<? extends YResponseDTO> clazz) {
		requestObject(Method.POST, requestKey, url, map, callBack, clazz, true);
	}

	/**
	 * 调用
	 * {@link #requestObject(Method.POST, String, String, Map, VolleyServiceCallBack, Class)}
	 * , 其中第一个参数为{@link com.android.volley.Request.Method#POST}，第二个参数为null
	 * 
	 * @param url
	 *            请求地址
	 * @param map
	 *            参数
	 * @param callBack
	 *            请求结果回调方法
	 * @param clazz
	 *            请求返回数据的结果类型
	 */
	public void requestObject(String url, final Map<String, String> map, final VolleyServiceCallBack callBack,
			final Class<? extends YResponseDTO> clazz) {
		requestObject(Method.POST, null, url, map, callBack, clazz, true);
	}

	/**
	 * 调用
	 * {@link #requestObject(int, String, String, Map, VolleyServiceCallBack, Class)}
	 * ，其中第二个参数为null
	 * 
	 * @param method
	 *            {@link Method} 请求方法类型
	 * @param url
	 *            请求地址
	 * @param map
	 *            参数
	 * @param callBack
	 *            请求结果回调方法
	 * @param clazz
	 *            请求返回数据的结果类型
	 */
	public void requestObject(int method, final String url, final Map<String, String> map,
			final VolleyServiceCallBack callBack, final Class<? extends YResponseDTO> clazz) {
		requestObject(method, null, url, map, callBack, clazz, true);
	}

	/**
	 * 调用 {@link #requestObject(String, String, Map, VolleyServiceCallBack)}
	 * 其中第一个参数为null
	 * 
	 * @param url
	 *            请求地址
	 * @param map
	 *            参数
	 * @param callBack
	 *            请求结果回调方法
	 */
	public void requestObject(final String url, final Map<String, String> map, final VolleyServiceCallBack callBack) {
		requestObject(null, url, map, callBack);
	}

	/**
	 * 调用
	 * {@link #requestObject(String, String, Map, VolleyServiceCallBack, VolleyServiceCallBack)}
	 * ，其中最后一个参数为null
	 * 
	 * @param requestKey
	 *            请求关键字
	 * @param url
	 *            请求地址
	 * @param map
	 *            参数
	 * @param callBack
	 *            请求结果回调方法
	 */
	public void requestObject(final String requestKey, final String url, final Map<String, String> map,
			final VolleyServiceCallBack callBack) {
		requestObject(requestKey, url, map, callBack, null);
	}

	/**
	 * @param method
	 *            {@link Method} 请求方法类型
	 * @param requestKey
	 *            请求关键字
	 * @param url
	 *            请求地址
	 * @param map
	 *            参数
	 * @param callBack
	 *            请求结果回调方法
	 * @param clazz
	 *            请求返回数据的结果类型，可以重写{@link YResponseDTO#isSuccess()}
	 *            ，返回成功时若此参数不为空则返回{@link YResponseDTO#getData()}, 若为空则返回
	 *            {@link YResponseDTO}
	 * @param cancelSame
	 *            是否取消之前的具有相同的requestKey和url的请求
	 */
	public void requestObject(int method, final String requestKey, final String url, final Map<String, String> map,
			final VolleyServiceCallBack callBack, final Class<? extends YResponseDTO> clazz, boolean cancelSame) {
		if (cancelSame) {
			cancelAll(requestKey, url);
		}
		final StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Logger.i(response);
				if (callBack == null) {
					return;
				}
				YResponseDTO ret = null;
				try {
					ret = YJsonUtil.parseObject(response, clazz == null ? YResponseDTO.class : clazz);
				} catch (Exception e) {
					Logger.e(e, "Volley请求结果：" + response);
					YResponseError error = new YResponseError(YResponseError.Type.DECODEERROR);
					callBack.onRequestFailed(url + requestKey, error);
					e.printStackTrace();
					return;
				}
				if (!ret.isSuccess()) {
					YResponseError error = new YResponseError(ret.getStatus_code(), ret.getMessage());
					callBack.onRequestFailed(url + requestKey, error);
				} else {
					callBack.onRequestSuccess(url + requestKey, clazz == null ? ret : ret.getData());
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				YResponseError yResponseError = new YResponseError(YResponseError.Type.UNKNOWERROR);
				if (error instanceof TimeoutError) {
					yResponseError = new YResponseError(YResponseError.Type.TIMEOUT);
				} else if (error instanceof ServerError) {
					yResponseError = new YResponseError(YResponseError.Type.SERVERERROR);
				} else if (error instanceof NoConnectionError) {
					yResponseError = new YResponseError(YResponseError.Type.NOCONNECTIONERROR);
				} else {
					Logger.e(error, "Volley请求失败：" + YResponseError.Type.UNKNOWERROR.name());
					error.printStackTrace();
				}
				if (callBack != null) {
					callBack.onRequestFailed(url + requestKey, yResponseError);
				}
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return map;
			}

		};
		Logger.i("Url:%s \n params:%s", stringRequest.getUrl(), map != null ? map.toString() : "null");
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MILLISECOND,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		stringRequest.setTag(url + requestKey);
		mSelf.mQueue.add(stringRequest);
	}

	/**
	 * 取消所有指定的请求
	 * 
	 * @param requestKey
	 *            请求关键字
	 * @param url
	 *            请求地址
	 */
	public void cancelAll(String url) {
		cancelAll(null, url);
	}

	/**
	 * 取消所有指定的请求
	 * 
	 * @param requestKey
	 *            请求关键字
	 * @param url
	 *            请求地址
	 */
	public void cancelAll(final String requestKey, final String url) {
		mSelf.mQueue.cancelAll(new RequestFilter() {

			@Override
			public boolean apply(Request<?> request) {
				return request.getTag().toString().equals(url + requestKey);
			}
		});
	}
}
