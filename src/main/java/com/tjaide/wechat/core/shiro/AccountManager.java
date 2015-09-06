package com.tjaide.wechat.core.shiro;

import java.util.Set;

/**
 * @author wangjun
 * @param      
 * @throws
 * @date 2014年12月2日下午3:19:36
 * @Description: 提供接口实现
 */
public abstract interface AccountManager
{
  public abstract ShiroUser loadUser(String username);

  public abstract Set<String> loadRoles(String username, String department);

  public abstract Set<String> loadPermissions(String role,String department);
}