package com.tjaide.wechat.service;

import java.util.Map;

public interface CoreService {
	String process(Map<String,String> requestMap) throws Exception;
	void testDao();
}
