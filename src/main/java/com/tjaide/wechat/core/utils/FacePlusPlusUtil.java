package com.tjaide.wechat.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tjaide.wechat.core.bean.third.Face;

/**
 * @author wangjun
 * @param
 * @throws
 * @date 2015年7月15日上午9:20:12
 * @Description: Face++接口工具类
 */
public class FacePlusPlusUtil {
	/**
	 * @author wangjun
	 * @param @param imageUrl 带识别的图片地址
	 * @param @return
	 * @return String
	 * @throws
	 * @date 2015年7月15日上午9:21:23
	 * @Description: 人脸识别提供给外部调用
	 */
	public static String detectFace(String imageUrl) {
		// 请求地址
		String requestUrl = "http://apicn.faceplusplus.com/v2/detection/detect?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&url=URL&attribute=glass,pose,gender,age,race,smiling";
		requestUrl = requestUrl.replace("YOUR_API_KEY", "840561c0cb59737f7121c240b71340e4");
		requestUrl = requestUrl.replace("YOUR_API_SECRET", "qqkCTL-XhALnPnOBs1ByXUF9rKNByeAF");
		requestUrl = requestUrl.replace("URL", imageUrl);
		// 发起请求
		String respJSON = PublicUtil.httpRequest(requestUrl, "GET", null);

		JSONArray faceArray = (JSONArray) JSONObject.fromObject(respJSON).get("face");
		List<Face> faceList = new ArrayList<Face>();
		for (int i = 0; i < faceArray.size(); i++) {
			JSONObject faceObject = (JSONObject) faceArray.get(i);
			JSONObject attrObject = faceObject.getJSONObject("attribute");
			JSONObject posObject = faceObject.getJSONObject("position");

			System.out.println(faceObject+"============");
			Face face = new Face();
			face.setFaceId(faceObject.getString("face_id"));
			face.setAgeValue(attrObject.getJSONObject("age").getInt("value"));
			face.setAgeRange(attrObject.getJSONObject("age").getInt("range"));
			face.setGenderValue(genderConvert(attrObject.getJSONObject("gender").getString("value")));
			face.setGenderConfidence(attrObject.getJSONObject("gender").getDouble("confidence"));
			face.setRaceValue(raceConvert(attrObject.getJSONObject("race").getString("value")));
			face.setRaceConfidence(attrObject.getJSONObject("race").getDouble("confidence"));
			face.setSmilingValue(attrObject.getJSONObject("smiling").getDouble("value"));
			face.setCenterX(posObject.getJSONObject("center").getDouble("x"));
			face.setCenterY(posObject.getJSONObject("center").getDouble("y"));
			faceList.add(face);

		}

		Collections.sort(faceList);
		StringBuffer buffer = new StringBuffer();
		// 检测到1张人脸
		if (1 == faceList.size()) {
			buffer.append("共检测到").append(faceList.size()).append("张人脸").append("\n\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("人种，");
				buffer.append(face.getGenderValue()).append("，");
				buffer.append(face.getAgeValue()).append("岁左右");
			}
		} else if (faceList.size() > 1) {
			buffer.append("共检测到").append(faceList.size()).append("张人脸,按脸部中心位置从左到右").append("\n\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("人种，");
				buffer.append(face.getGenderValue()).append("，");
				buffer.append(face.getAgeValue()).append("岁左右").append("\n");
			}
		} else {
			buffer.append("未检测到人脸");
		}
		return buffer.toString();
	}

	/**
	 * @author wangjun
	 * @param @param string
	 * @param @return
	 * @return String
	 * @throws
	 * @date 2015年7月15日上午10:15:46
	 * @Description: 人种转换 英文 -- 中文
	 */
	private static String raceConvert(String race) {
		// TODO Auto-generated method stub
		String result = "黄色";
		if (race.equals("Black")) {
			result = "黑色";
		} else if (race.equals("White")) {
			result = "白色";
		}
		return result;
	}

	/**
	 * @author wangjun
	 * @param @param string
	 * @param @return
	 * @return String
	 * @throws
	 * @date 2015年7月15日上午10:15:46
	 * @Description: 性别转换 英文 -- 中文
	 */
	private static String genderConvert(String gender) {
		// TODO Auto-generated method stub
		String result = "男性";
		if (gender.equalsIgnoreCase("female")) {
			result = "女性";
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(detectFace("http://lovelykobe.aliapp.com/image/test.jpg"));;
	}
}
