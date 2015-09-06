package com.tjaide.wechat.core.shiro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * @author wangjun
 * @param      
 * @throws
 * @date 2014年12月2日下午3:18:49
 * @Description: 实体类
 */
public class ShiroUser implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String name;
	private String useraccountnumber;
	private String password;
	private String currentRole;
	private String dept;
	private Set<String> pops;
	private Set<String> roles;
	private Map<String, Object> attr = new HashMap<String, Object>();
	private Boolean locked = Boolean.FALSE;
	

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDept() {
		return dept;
	}
	

	public String getUseraccountnumber() {
		return useraccountnumber;
	}

	public void setUseraccountnumber(String useraccountnumber) {
		this.useraccountnumber = useraccountnumber;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Map<String, Object> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, Object> attr) {
		this.attr = attr;
	}

	public Set<String> getPops() {
		return pops;
	}

	public void setPops(Set<String> pops) {
		this.pops = pops;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public ShiroUser(String username, String name) {
		this.username = username;
		this.name = name;
	}

	public void addAttr(String key, Object value) {
		if (this.attr == null) {
			this.attr = new HashMap<String, Object>();
		}
		if (value == null) {
			if (this.attr.get(key) != null)
				this.attr.remove(key);
		} else
			this.attr.put(key, value);
	}

	public void getAttr(String key) {
		if (this.attr == null) {
			this.attr = new HashMap<String, Object>();
		}
		this.attr.get(key);
	}

	public String toString() {
		return this.username;
	}
}
