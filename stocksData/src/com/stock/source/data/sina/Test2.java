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

public class Test2 {
	public final static void main(String[] args) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// HttpGet httpget = new HttpGet("http://sina.com/");
			HttpGet httpget = new HttpGet(
					"http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/601006.phtml?year=2015&jidu=2");
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
			System.out.println("----------------------------------------");
//			 System.out.println(responseBody);
//			 System.out.println("----------------------------------------");
			 Pattern p0 = Pattern
						.compile("<table id=\"FundHoldSharesTable\">(.*)</table>" ,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			 String table = "";
			 Matcher macher0 = p0.matcher(responseBody);
				while (macher0.find()) {
					table = macher0.group(0).trim();
					/*System.out.println(
							"日期111:" + macher0.group(0).trim()
							+"\t日期222:" + macher0.group(1).trim()
							);*/
				}
				table = table.replaceAll("[\\s*\\t\\n\\r]", "").replaceAll("class=\"tr_2\"", "")
						.replaceAll("<tdclass=\"tdr\">", "<td>")
						.replaceAll("<divalign=\"center\">", "")
				.replaceAll("</div>", "");
//				System.err.println(table);
		Pattern p = Pattern
					.compile(
"<tr><td><atarget='_blank'href='http://vip\\.stock\\.finance\\.sina\\.com\\.cn/quotes_service/view/vMS_tradehistory.php\\?symbol=(.{8})&date=(.{10})'>(.{10})</a></td>"
+ "<td>([0-9]*\\.[0-9]*)</td>"
+ "<td>([0-9]*\\.[0-9]*)</td>"
+ "<td>([0-9]*\\.[0-9]*)</td>"
+ "<td>([0-9]*\\.[0-9]*)</td>"
+ "<td>([0-9]*)</td>"
+ "<td>([0-9]*)</td></tr>"
							);
			Matcher macher = p.matcher(table);
			while (macher.find()) {
				System.out.println(
//						"all0:" + macher.group(0).trim()+
//						"all1:" + macher.group(1).trim()+
//						"all2:" + macher.group(2).trim()+
						" 日期:" + macher.group(3).trim()
						+" \t开盘价:" + macher.group(4).trim()
						+" \t最高价:"+macher.group(5).trim()
						+" \t收盘价:"+macher.group(6).trim()
						+" \t最低价:"+macher.group(7).trim()
						+" \t交易量(股):"+macher.group(8).trim()
						+" \t交易金额(元):"+macher.group(9).trim()
						);
			}
		} finally {
			httpclient.close();
		}
	}
}
