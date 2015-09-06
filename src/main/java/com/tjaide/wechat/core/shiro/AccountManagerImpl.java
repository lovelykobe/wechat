package com.tjaide.wechat.core.shiro;

import java.util.Set;

import org.springframework.stereotype.Service;


/**
 * @author wangjun
 * @param
 * @throws
 * @date 2014年12月8日上午8:58:37
 * @Description: 登陆用户内容的实现
 */
@Service
public class AccountManagerImpl implements AccountManager {

	@Override
	public ShiroUser loadUser(String username) {
		
		return null;
	}

	@Override
	public Set<String> loadRoles(String username, String department) {
		return null;
	}

	@Override
	public Set<String> loadPermissions(String username, String department) {
		return null;
	}

}
