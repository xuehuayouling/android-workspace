package com.android.ysq.utils;

import java.util.List;

public interface IJson {
	public String toJSONString(Object obj);
	public <T> List<T> parseArray(String text, Class<T> clazz);
	public <T> List<T> parseSpecialArray(String text, String specialKey, Class<T> clazz);
	public <T> T parseObject(String text, Class<T> clazz);
	public <T> T parseSpecialObject(String text, String specialKey, Class<T> clazz);
	public List<?> toList(String json);
}
