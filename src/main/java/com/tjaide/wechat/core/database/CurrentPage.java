package com.tjaide.wechat.core.database;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页方法
 */
public class CurrentPage<E> {
	private int pageSize = 20;// 每页记录数
	private int pageNum = 1;// 当前页
	private int beginRecordNum = 0;// 开始记录号
	private int endRecordNum = 0;// 结束记录号
	private int totalPages = 0;// 总页数
	private int totalRecords = 0;// 总记录数
	private String sortName = "";
	private String sortOrder = "DESC";
	private String qtype = "";
	private String qname = "";
	private String qvalue = "";
	private List<E> pageItems = new ArrayList<E>();
	private Class<?> clazz;

	public CurrentPage(Class<?> clazz) {
		this.clazz = clazz;
	}

	public CurrentPage(int pageNum, int pageSize, int totalRecords) {
		this.init(pageNum, pageSize, totalRecords);
	}
	public CurrentPage(int start,int end, int pageSize, int totalRecords) {
		this.init(start,end, pageSize, totalRecords);
	}
	public CurrentPage() {
	}
	public void init(int start,int end, int pageSize, int totalRecords) {
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
		this.setTotalRecords(totalRecords);
		this.beginRecordNum=start;
		this.endRecordNum=end;
	}
	public void init(int pageNum, int pageSize, int totalRecords) {
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
		this.setTotalRecords(totalRecords);
		this.comp();
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageItems(List<E> pageItems) {
		this.pageItems = pageItems;
	}

	public int getPageNum() {
		return pageNum;
	}

	public List<E> getPageItems() {
		return pageItems;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void comp() {
		// 如果有记录
		if (this.totalRecords > 0) {
			// 计算出总页数
			this.totalPages = (int) Math.ceil((double) this.totalRecords / (double) this.pageSize);
			// 得到总页数后修正错误的页号
			if (this.totalPages < this.pageNum) {
				this.pageNum = this.totalPages;
			}
		}
		// 修正错误的页号
		if (this.pageNum < 1) {
			this.pageNum = 1;
		}
		// 计算开始和结束记录号
		this.beginRecordNum = this.pageSize * (this.pageNum - 1);
		this.endRecordNum = this.pageSize * this.pageNum;
		// 修正末尾超出的记录号
		if (this.endRecordNum > this.totalRecords && this.totalRecords > 0) {
			this.endRecordNum = this.totalRecords;
		}
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getBeginRecordNum() {
		return beginRecordNum;
	}

	public int getEndRecordNum() {
		return endRecordNum;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getQtype() {
		return qtype;
	}

	public void setQtype(String qtype) {
		this.qtype = qtype;
	}

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	public String getQvalue() {
		return qvalue;
	}

	public void setQvalue(String qvalue) {
		this.qvalue = qvalue;
	}
}