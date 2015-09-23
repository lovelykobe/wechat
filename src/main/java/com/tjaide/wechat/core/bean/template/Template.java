package com.tjaide.wechat.core.bean.template;

import java.util.List;

public class Template {
	
	private String toUser;
	
	private String templateId;
	
	private String url;
	
	private String topColor;
	
	private List<TemplateParam> templateParamList;

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopColor() {
		return topColor;
	}

	public void setTopColor(String topColor) {
		this.topColor = topColor;
	}

	public List<TemplateParam> getTemplateParamList() {
		return templateParamList;
	}

	public void setTemplateParamList(List<TemplateParam> templateParamList) {
		this.templateParamList = templateParamList;
	}
	
	public String toJSON(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append(String.format("\"touser\":\"%s\"", this.toUser)).append(",");
		buffer.append(String.format("\"template_id\":\"%s\"", this.templateId)).append(",");
		buffer.append(String.format("\"url\":\"%s\"", this.url)).append(",");
		buffer.append(String.format("\"touser\":\"%s\"", this.toUser)).append(",");
		buffer.append(String.format("\"topcolor\":\"%s\"", this.topColor)).append(",");
		buffer.append("\"data\":{");
		TemplateParam param = null;
		for(int i = 0;i < this.templateParamList.size();i++){
			param = templateParamList.get(i);
			// 判断是否追加逗号
			if(i < this.templateParamList.size()-1){
				buffer.append(String.format("\"%s\":{\"value\":\"%s\",\"color\":\"%s\"},",param.getName(),param.getValue(),param.getColor()));
			}else{
				buffer.append(String.format("\"%s\":{\"value\":\"%s\",\"color\":\"%s\"}",param.getName(),param.getValue(),param.getColor()));
			}
		}
		buffer.append("}");
		buffer.append("}");
		return buffer.toString();
	}

	public Template(String toUser, String templateId, String url, String topColor,
			List<TemplateParam> templateParamList) {
		super();
		this.toUser = toUser;
		this.templateId = templateId;
		this.url = url;
		this.topColor = topColor;
		this.templateParamList = templateParamList;
	}

	public Template() {
		super();
	}
	
	
}
