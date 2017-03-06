package com.stock.source.data.sina;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Test {
	public final static void main(String[] args) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// HttpGet httpget = new HttpGet("http://sina.com/");
			HttpGet httpget = new HttpGet(
					"http://market.finance.sina.com.cn/transHis.php?symbol=sh601006&date=2016-08-01&page=1");
			httpget.addHeader("Content-Type", "text/html;charset=UTF-8");

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity,
								"GBK") : null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			};

			String responseBody = httpclient.execute(httpget, responseHandler);
//			System.out.println("----------------------------------------");
//			 System.out.println(responseBody);
			Pattern p = Pattern
					.compile("<th>([\\d]{2}:[\\d]{2}:[\\d]{2})</th><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><th><h(\\d{1})>(.*)</h\\d{1}></th>");
			Matcher macher = p.matcher(responseBody);
			while (macher.find()) {
				System.out.println(
						"成交时间:" + macher.group(1).trim()+
						"\t成交价:" + macher.group(2).trim()
						+"\t价格变动:"+macher.group(3).trim()
						+"\t成交量(手):"+macher.group(4).trim()
						+"\t成交额(元):"+macher.group(5).trim()
						+"\t性质:"+macher.group(7).trim()
						);
			}
		} finally {
			httpclient.close();
		}
	}
}
