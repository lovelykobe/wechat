package com.tjaide.wechat.core.filter;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.tjaide.wechat.core.shiro.ShiroUser;

/**
 * @author wangjun
 * @param
 * @throws
 * @date 2014年12月3日上午9:52:03
 * @Description: 提出用户过滤器
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

	private String kickoutUrl;
	private boolean kickoutAfter = false;
	private int maxSession = 1;

	private SessionManager sessionManager;
	private Cache<String, Deque<Serializable>> cache;

	public void setKickoutUrl(String kickoutUrl) {
		this.kickoutUrl = kickoutUrl;
	}

	public void setKickoutAfter(boolean kickoutAfter) {
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			return true;
		}

		Session session = subject.getSession();
		ShiroUser user = (ShiroUser) subject.getPrincipal();
		String username = user.getName();
		Serializable sessionId = session.getId();

		Deque<Serializable> deque = cache.get(username);
		if (deque == null) {
			deque = new LinkedList<Serializable>();
			cache.put(username, deque);
		}

		if (!deque.contains(sessionId)
				&& session.getAttribute("kickout") == null) {
			deque.push(sessionId);
		}

		while (deque.size() > maxSession) {
			Serializable kickoutSessionId = null;
			if (kickoutAfter) { 
				kickoutSessionId = deque.removeFirst();
			} else { 
				kickoutSessionId = deque.removeLast();
			}
			try {
				Session kickoutSession = sessionManager
						.getSession(new DefaultSessionKey(kickoutSessionId));
				if (kickoutSession != null) {
					kickoutSession.setAttribute("kickout", true);
				}
			} catch (Exception e) {// ignore exception
			}
		}

		if (session.getAttribute("kickout") != null) {
			try {
				SecurityUtils.getSubject().logout(); 
			} catch (Exception e) { // ignore
			}
			saveRequest(request);
			WebUtils.issueRedirect(request, response, kickoutUrl);

			return false;
		}

		return true;
	}
}
