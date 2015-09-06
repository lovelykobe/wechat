package com.tjaide.wechat.core.shiro;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author wangjun
 * @param
 * @throws
 * @date 2014年12月2日下午3:19:02
 * @Description: DbRealm
 */
public class ShiroDbRealm extends AuthorizingRealm {

	private AccountManager accountManager;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// TODO 判断用户角色权限
		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName())
				.iterator().next();

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> roles = null;
		Set<String> pops = null;
		if ((shiroUser.getRoles() == null)
				|| (shiroUser.getRoles().size() == 0)) {
			roles = this.accountManager.loadRoles(shiroUser.getUsername(),shiroUser.getDept());
		} else {
			roles = shiroUser.getRoles();
		}
		info.setRoles(roles);
		if ((shiroUser.getPops() == null)
				|| (shiroUser.getPops().size() == 0)) {
				pops = this.accountManager.loadPermissions(shiroUser.getUsername(), shiroUser.getDept());
		} else {
			pops = shiroUser.getPops();
		}
		info.setStringPermissions(pops);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		// TODO 用户登录
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		SimpleAuthenticationInfo simpleAuthenticationInfo = null;
		// System.out.println((char[])token.getPassword());
		ShiroUser user = this.accountManager.loadUser(token.getUsername());
		if (user == null) {
			throw new UnknownAccountException();
		}
		if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); 
        }
		String password = user.getPassword();
		user.setPassword("");
		simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,
				password, getName());
		return simpleAuthenticationInfo;
	}

	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

}
