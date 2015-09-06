package com.tjaide.wechat.core.utils;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangjun
 * @param
 * @throws
 * @date 2014年12月3日下午1:47:32
 * @Description: 限定登陆次数
 */
public class RetryLimitHashedCredentialsMatcher extends
		HashedCredentialsMatcher {

	private Cache<String, Map<String, Object>> passwordRetryCache;

	private List<Integer> LimitCount;

	private List<Long> LimitTime;

	public List<Integer> getLimitCount() {
		return LimitCount;
	}

	public void setLimitCount(List<Integer> limitCount) {
		LimitCount = limitCount;
	}

	public List<Long> getLimitTime() {
		return LimitTime;
	}

	public void setLimitTime(List<Long> limitTime) {
		LimitTime = limitTime;
	}

	public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
		passwordRetryCache = cacheManager.getCache("passwordRetryCache");
	}
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken,
			AuthenticationInfo info) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		Map<String, Object> retryMap = passwordRetryCache.get(username);
		if (retryMap == null) {
			retryMap = new HashMap<String, Object>();
			retryMap.put("retryCount", new AtomicInteger(0));
			passwordRetryCache.put(username, retryMap);
		}
		AtomicInteger retryCount = (AtomicInteger) retryMap.get("retryCount");
		Integer count = retryCount.incrementAndGet();
		if (retryMap.containsKey("loginTime")) {
			Long loginTime = Long.parseLong(retryMap.get("loginTime")
					.toString());
			Integer currentCount = Integer.parseInt(retryMap
					.get("currentCount").toString());
			Long differTime = System.currentTimeMillis() - loginTime;
			for (int i = 0; i < LimitCount.size()-1; i++) {
				if (currentCount > LimitCount.get(i)&&currentCount <LimitCount.get(i +1) ) {
					if (differTime > LimitTime.get(i)) {
						retryMap.remove("loginTime");
						retryMap.remove("currentCount");
						retryCount = new AtomicInteger(currentCount);
						retryMap.put("retryCount", retryCount);
						boolean matches = super.doCredentialsMatch(token, info);
						if (matches) {
							passwordRetryCache.remove(username);
							return matches;
						}else{
							throw new ExcessiveAttemptsException(LimitTime.get(i) + "");
						}
					} else {
						throw new ExcessiveAttemptsException(LimitTime.get(i)-differTime + "");
					}
				}
			}
			if(currentCount > LimitCount.get(LimitCount.size()-1)){
				if (differTime > LimitTime.get(LimitCount.size()-1)) {
					retryMap.remove("loginTime");
					retryMap.remove("currentCount");
					retryCount = new AtomicInteger(currentCount);
					retryMap.put("retryCount", retryCount);
					boolean matches = super.doCredentialsMatch(token, info);
					if (matches) {
						passwordRetryCache.remove(username);
						return matches;
					}else{
						throw new ExcessiveAttemptsException(LimitTime.get(LimitCount.size()-1) + "");
					}
				} else {
					throw new ExcessiveAttemptsException(LimitTime.get(LimitCount.size()-1)-differTime + "");
				}
			}
			
		} else {
			for (int i = 0; i < LimitCount.size()-1; i++) {
				if (count > LimitCount.get(i)&&count <LimitCount.get(i +1) ) {
					retryMap.put("loginTime", System.currentTimeMillis());
					retryMap.put("currentCount", count);
					throw new ExcessiveAttemptsException(LimitTime.get(i)
							+ "");
				} 
			}
			if(count > LimitCount.get(LimitCount.size()-1)){
				retryMap.put("loginTime", System.currentTimeMillis());
				retryMap.put("currentCount", count);
				throw new ExcessiveAttemptsException(LimitTime.get(LimitCount.size()-1)+"");
			}
		}
		
		boolean matches = super.doCredentialsMatch(token, info);
		if (matches) {
			passwordRetryCache.remove(username);
		}
		return matches;
	}
}
