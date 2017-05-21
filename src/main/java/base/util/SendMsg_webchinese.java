package base.util;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weixin.base.html.HttpsUtil;

import java.io.IOException;

public class SendMsg_webchinese {

	private boolean flag = false;

	private static Log logger = LogFactory.getLog(SendMsg_webchinese.class);

//	// 普通方法，主要是为了spring注入，区分开发环境和生产环境使用,测试使用
//	public String sendMsgGBKBean(String phone, String code)
//			throws HttpException, IOException {
//		if (flag) {
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
//			post.addRequestHeader("Content-Type",
//					"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
//			NameValuePair[] data = { new NameValuePair("Uid", "lfgxlyg"),
//					new NameValuePair("Key", "15b66951ecdccd4f74f0"),
//					new NameValuePair("smsMob", phone),
//					new NameValuePair("smsText", code) };
//			post.setRequestBody(data);
//			client.executeMethod(post);
//			String result = new String(post.getResponseBodyAsString().getBytes(
//					"gbk"));
//			logger.debug("[result=" + result + "] [phone=" + phone + "] [code=" + code + "]");
//			post.releaseConnection();
//			return result;
//		} else {
//			String result = "1";
//			logger.info("[进入测试阶段，短信功能屏蔽]");
//			logger.info("[" + result + ":" + phone + ":" + code + "]");
//			return result;
//		}
//
//	}
//
//	public String sendMsgUTF8Bean(String phone, String code)
//			throws HttpException, IOException {
//		if (flag) {
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");
//			post.addRequestHeader("Content-Type",
//					"application/x-www-form-urlencoded;charset=UTF-8");// 在头文件中设置转码
//			NameValuePair[] data = { new NameValuePair("Uid", "lfgxlyg"),
//					new NameValuePair("Key", "15b66951ecdccd4f74f0"),
//					new NameValuePair("smsMob", phone),
//					new NameValuePair("smsText", code) };
//			post.setRequestBody(data);
//			client.executeMethod(post);
//			String result = new String(post.getResponseBodyAsString().getBytes(
//					"UTF-8"));
//			logger.debug("[" + result + ":" + phone + ":" + code + "]");
//			post.releaseConnection();
//			return result;
//		} else {
//			String result = "1";
//			logger.info("[进入测试阶段，短信功能屏蔽]");
//			logger.info("[" + result + ":" + phone + ":" + code + "]");
//			return result;
//		}
//	}

//	public static int sendMsgGBK(String phone, String code) {
//		com.alibaba.fastjson.JSONObject jsonPost = new com.alibaba.fastjson.JSONObject();
//
//		String url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="
//				+ componentAccessToken;
//
//		log.debug("before post," + content + "," + url);
//		String authorization_info_Json = HttpsUtil.post(url, content);
//		log.debug("after post," + content + "," + url + "," + authorization_info_Json);
//		JSONObject jsonObject = JSONObject.fromObject(authorization_info_Json);
//		if (jsonObject == null) {
//			log.error("jsonObject null,"+url+","+authorization_info_Json);
//			return null;
//		}
//	}
	public static int sendMsgGBK(String phone, String code)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
		NameValuePair[] data = { new NameValuePair("Uid", "lfgxlyg"),
				new NameValuePair("Key", "15b66951ecdccd4f74f0"),
				new NameValuePair("smsMob", phone),
				new NameValuePair("smsText", code) };
		post.setRequestBody(data);
		client.executeMethod(post);
		String result = new String(post.getResponseBodyAsString().getBytes(
				"gbk"));
		logger.debug("[" + result + ":" + phone + ":" + code + "]");
		post.releaseConnection();
		return Integer.parseInt(result);
	}

//	public static String sendMsgUTF8(String phone, String code)
//			throws HttpException, IOException {
//		HttpClient client = new HttpClient();
//		PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");
//		post.addRequestHeader("Content-Type",
//				"application/x-www-form-urlencoded;charset=UTF-8");// 在头文件中设置转码
//		NameValuePair[] data = { new NameValuePair("Uid", "lfgxlyg"),
//				new NameValuePair("Key", "15b66951ecdccd4f74f0"),
//				new NameValuePair("smsMob", phone),
//				new NameValuePair("smsText", code) };
//		post.setRequestBody(data);
//		client.executeMethod(post);
//		String result = new String(post.getResponseBodyAsString().getBytes(
//				"UTF-8"));
//		logger.debug("[" + result + ":" + phone + ":" + code + "]");
//		post.releaseConnection();
//		return result;
//	}

//	public static void main(String[] args) throws HttpException, IOException {
//		System.out.println(new SendMsg_webchinese().sendMsgGBK("18101330507",
//				"验证码：123456"));
//		// try {
//		// sendMsg("13146648992", "717478-1397355486791");
//		// } catch (HttpException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// } catch (IOException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//	}

//	public static void main1(String[] args) throws Exception {
//
//		HttpClient client = new HttpClient();
//		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
//		post.addRequestHeader("Content-Type",
//				"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
//		NameValuePair[] data = { new NameValuePair("Uid", "lfgxlyg"),
//				new NameValuePair("Key", "15b66951ecdccd4f74f0"),
//				new NameValuePair("smsMob", "13146648992,18500193590"),
//				new NameValuePair("smsText", "短信验证码为：2697") };
//		post.setRequestBody(data);
//
//		client.executeMethod(post);
//		Header[] headers = post.getResponseHeaders();
//		int statusCode = post.getStatusCode();
//		System.out.println("statusCode:" + statusCode);
//		for (Header h : headers) {
//			System.out.println(h.toString());
//		}
//		String result = new String(post.getResponseBodyAsString().getBytes(
//				"gbk"));
//		System.out.println(result);
//
//		post.releaseConnection();
//
//	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}