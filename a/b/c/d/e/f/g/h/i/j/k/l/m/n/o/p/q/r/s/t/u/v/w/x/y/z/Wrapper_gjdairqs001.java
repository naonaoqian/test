
import com.qunar.qfwrapper.bean.booking.BookingResult;
import com.qunar.qfwrapper.bean.booking.BookingInfo;
import com.qunar.qfwrapper.bean.search.FlightSearchParam;
import com.qunar.qfwrapper.bean.search.ProcessResultInfo;
import com.qunar.qfwrapper.bean.search.OneWayFlightInfo;
import com.qunar.qfwrapper.bean.search.FlightDetail;
import com.qunar.qfwrapper.bean.search.FlightSegement;
import com.qunar.qfwrapper.interfaces.QunarCrawler;
import com.qunar.qfwrapper.util.QFGetMethod;
import com.qunar.qfwrapper.util.QFHttpClient;
import com.qunar.qfwrapper.util.QFPostMethod;
import com.qunar.qfwrapper.constants.Constants;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpParamsFactory;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;




import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Wrapper_gjdairqs001 implements QunarCrawler {
	private String cookie;
	public static void main(String[] args) {
		/**
		 * VLC-KBP 2014-07-03 ZTH-ARN 2014-07-04 ZTH-PRG 2014-07-02
		 */
		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("AYT");
		searchParam.setArr("BTS");
		searchParam.setDepDate("2014-07-30");
		searchParam.setTimeOut("60000");
		searchParam.setToken("");
		Wrapper_gjdairqs001 ssss =  new Wrapper_gjdairqs001();
		String html = ssss.getHtml(searchParam);
		ProcessResultInfo result = new ProcessResultInfo();
		result = ssss.process(html, searchParam);
//		ssss.getBookingInfo(searchParam);
		System.out.println(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect));;
//		if (result.isRet() && result.getStatus().equals(Constants.SUCCESS)) {
//			List<OneWayFlightInfo> flightList = (List<OneWayFlightInfo>) result
//					.getData();
//			for (OneWayFlightInfo in : flightList) {
//				System.out.println("************" + in.getInfo().toString());
//				System.out.println("++++++++++++" + in.getDetail().toString());
//			}
//		} else {
//			System.out.println(result.getStatus());
//		}
	}

	


	public void voidCertificate(QFHttpClient httpClient) {
		Protocol.registerProtocol("https", 
				new Protocol("https", new MySecureProtocolSocketFactory1(), 443));

	}
	private QFHttpClient getProxyHttpClient(QFHttpClient httpClient) {
		 return httpClient;
	}
	public String getHtml(FlightSearchParam searchParam) {
		QFGetMethod get = null;
		try {
			QFHttpClient httpClient = new QFHttpClient(searchParam, false);
			httpClient = getProxyHttpClient(httpClient);
			httpClient.getParams().setCookiePolicy(
					CookiePolicy.BROWSER_COMPATIBILITY);
			String[] depDate = searchParam.getDepDate().split("-");
			depDate[1]=Integer.parseInt(depDate[1])<10?"0"+Integer.parseInt(depDate[1]):Integer.parseInt(depDate[1])+"";
			depDate[2]=Integer.parseInt(depDate[2])<10?"0"+Integer.parseInt(depDate[2]):Integer.parseInt(depDate[2])+"";
			String postUrl = String.format("http://bookings.smartwings.com/en/index.php?AIRLINES=QS&PRICER_PREF=SCP2&next=1&searchTarget=_parent&DEP_0=%s&ARR_0=%s&JOURNEY_TYPE=OW&FULL_DATE_0=%s&FULL_DATE_1=&ADTCOUNT=1&CHDCOUNT=0&INFCOUNT=0&PROMOCODE=",searchParam.getDep(),searchParam.getArr(),depDate[2]+"-"+depDate[1]+"-"+depDate[0]);
			
			get = new QFGetMethod(postUrl);
			
			get.setFollowRedirects(false);
			get.getParams().setContentCharset("utf-8");
			int status =httpClient.executeMethod(get);
			String sid = httpClient.getState().getCookies()[0].toString();
			cookie = StringUtils.join(httpClient.getState().getCookies(),"; ");
			httpClient.getState().clearCookies();
			//
			get = new QFGetMethod("http://bookings.smartwings.com/en/ajaxSectorCalendarOffer.php?sector=0&fareOfferData=0&"+sid+"&userFareOfferData=0");
			get.addRequestHeader("Cookie",cookie);
			get.setFollowRedirects(false);
			get.getParams().setContentCharset("utf-8");
			status=httpClient.executeMethod(get);
			String key =getFareId(get.getResponseBodyAsString());
			cookie = StringUtils.join(httpClient.getState().getCookies(),"; ");
			httpClient.getState().clearCookies();
			//
			get = new QFGetMethod("http://bookings.smartwings.com/en/ajaxSectorItineraryOffer.php?sector=0&fareId0="+URLEncoder.encode(key)+"&"+sid);
			get.addRequestHeader("Cookie",cookie);
			get.setFollowRedirects(false);
			get.getParams().setContentCharset("utf-8");
			status = httpClient.executeMethod(get);
			return get.getResponseBodyAsString().replaceAll("\\s*", "");

		} catch (Exception e) {
			e.printStackTrace();
			return "Exception"+e.getLocalizedMessage();
		} finally {
			if (null != get) {
				get.releaseConnection();
			}
		}
	}
	private String getFareId(String info){
		String[] results = info.split("radio");
		String returnStr ="";
		for (int i = 0; i < results.length; i++) {
			String temp =results[i];
			if(temp.contains("checked")){
				returnStr =StringUtils.substringBetween(temp, "value=\"", "\"");
				break;
			}
		}
		return returnStr;
	}
	public ProcessResultInfo process(String arg0, FlightSearchParam searchParam) {
		String html = arg0;
		ProcessResultInfo result = new ProcessResultInfo();
		if ("Exception".equals(html)) {
			result.setRet(false);
			result.setStatus(Constants.CONNECTION_FAIL);
			return result;
		}
		// 需要有明显的提示语句，才能判断是否INVALID_DATE|INVALID_AIRLINE|NO_RESULT
		String flightSegmentStr = org.apache.commons.lang.StringUtils.substringBetween(
				html, "<description>","</description>");
		try {
			List<OneWayFlightInfo> flightList = new ArrayList<OneWayFlightInfo>();
			String[] array = flightSegmentStr.split("<trid=\"flight_i");
			for (int j = 1; j < array.length; j++) {
				FlightDetail flightDetail = initFlightDetail(searchParam);
				String[] flights =array[j].split("fdi_middle><h3>");
				OneWayFlightInfo flight = new OneWayFlightInfo();
				List<FlightSegement> segs = new ArrayList<FlightSegement>();
				List<String> flightNoList = new ArrayList<String>();
				if(flights.length==1){
					FlightSegement seg = getSegmentFromString(flights[0], searchParam);
					String flightno = StringUtils.substringBetween(flights[0], "<h3>","<div");
					seg.setFlightno(flightno.substring(0,2)+flightno.substring(3));
					segs.add(seg);
					flightNoList.add(seg.getFlightno());
				}else{
					for (int i = 1; i < flights.length; i++) {
						FlightSegement seg = getSegmentFromString(flights[i], searchParam);
						String flightno = StringUtils.substringBefore(flights[i], "<divclass=");
						System.err.println(flightno);
						seg.setFlightno(flightno.substring(0,2)+flightno.substring(3));
						segs.add(seg);
						flightNoList.add(seg.getFlightno());

					}
				}
				System.out.println(array[j]);
				String basepriceInfo = StringUtils.substringBetween(array[j], "promo", "</div></div></td>") ;
				if(basepriceInfo.contains("SoldOut")){
					basepriceInfo = StringUtils.substringBetween(array[j], "ww_checked","</div>");
				}
				String resultprice =StringUtils.substring(basepriceInfo, -10);
				String price = "0";
				String unit = "EUR";
				
				resultprice =resultprice.substring(resultprice.indexOf(">")+1);
				resultprice = resultprice.replace(",", "");
				System.err.println(resultprice);
				if(resultprice.contains("€")){
					unit ="EUR";
					price = StringUtils.substring(resultprice, 0, -1);
				}else{
					unit =StringUtils.substring(resultprice, -3);
					price = StringUtils.substring(resultprice, 0, -3);
				}
				System.out.println(unit);
				System.out.println(price);
				flightDetail.setMonetaryunit(unit);
				flightDetail.setPrice(Double.parseDouble(price));
				flightDetail.setTax(0d);
				flightDetail.setFlightno(flightNoList);
				System.out.println(JSON.toJSONString(flightDetail));
				flight.setDetail(flightDetail);
				flight.setInfo(segs);
				flightList.add(flight);
			}
			result.setRet(true);
			result.setStatus(Constants.SUCCESS);
			result.setData(flightList);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.setRet(false);
			result.setStatus(Constants.PARSING_FAIL);
			return result;
		}
	}
	private double getTaxByTotal(double total,double price){
		return Math.round((total-price)*100)/100.0;
	}
	private FlightSegement getSegmentFromString(String segment,FlightSearchParam searchParam){
		FlightSegement seg = new FlightSegement();
		String[] depInfo = getDepairport(segment);
		System.out.println(Arrays.toString(depInfo));
		seg.setDeptime(depInfo[0]);
		seg.setDepDate(depInfo[1]);
		seg.setDepairport(depInfo[2]);
		String[] arrInfo = getArrairport(segment);
		System.out.println(Arrays.toString(arrInfo));
		seg.setArrtime(arrInfo[0]);
		seg.setArrDate(arrInfo[1]);
		seg.setArrairport(arrInfo[2]);
//		seg.setFlightno(flightseg.getString("carrier")+flightseg.getString("flightNumber"));
		return seg;
		
	}
	private String[] getDepairport(String info){
		String depInfo = StringUtils.substringBetween(info, "Departureairport","Arrivalairport");
		String depDateInfo = StringUtils.substringBetween(depInfo, "Departuretime(local)</td><td>", "</td>");
		String depAirport = StringUtils.substringBetween(depInfo, "</strong>(", ")<");
		String[] depDates = depDateInfo.split(",");
		String depTime = depDates[0];
		String[] depDateS = depDates[2].split("/");
		String depDate = depDateS[2]+"-"+depDateS[1]+"-"+depDateS[0];
		return new String[]{depTime,depDate,depAirport};
	}
	private String[] getArrairport(String info){
		String depInfo = StringUtils.substringAfter(info,"Arrivalairport");
		String depDateInfo = StringUtils.substringBetween(depInfo, "Arrivaltime(local)</td><td>", "</td>");
		String depAirport = StringUtils.substringBetween(depInfo, "</strong>(", ")<");
		String[] depDates = depDateInfo.split(",");
		String depTime = depDates[0];
		String[] depDateS = depDates[2].split("/");
		String depDate = depDateS[2]+"-"+depDateS[1]+"-"+depDateS[0];
		return new String[]{depTime,depDate,depAirport};
	}
	private FlightDetail initFlightDetail(FlightSearchParam searchParam){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		FlightDetail flightDetail = new FlightDetail();
		
		flightDetail.setWrapperid(searchParam.getWrapperid());
		flightDetail.setArrcity(searchParam.getArr());
		flightDetail.setDepcity(searchParam.getDep());
		try {
			flightDetail.setDepdate(sf.parse(searchParam.getDepDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flightDetail;
	}
	public BookingResult getBookingInfo(FlightSearchParam searchParam) {
		String[] depDate = searchParam.getDepDate().split("-");
		depDate[1]=Integer.parseInt(depDate[1])<10?"0"+Integer.parseInt(depDate[1]):Integer.parseInt(depDate[1])+"";
		depDate[2]=Integer.parseInt(depDate[2])<10?"0"+Integer.parseInt(depDate[2]):Integer.parseInt(depDate[2])+"";
		String postUrl = String.format("http://bookings.smartwings.com/en/index.php?AIRLINES=QS&PRICER_PREF=SCP2&next=1&searchTarget=_parent&DEP_0=%s&ARR_0=%s&JOURNEY_TYPE=OW&FULL_DATE_0=%s&FULL_DATE_1=&ADTCOUNT=1&CHDCOUNT=0&INFCOUNT=0&PROMOCODE=",searchParam.getDep(),searchParam.getArr(),depDate[2]+"-"+depDate[1]+"-"+depDate[0]);
		
		BookingResult bookingResult = new BookingResult();
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(postUrl);
		bookingInfo.setMethod("get");
		Map<String, String> map = new LinkedHashMap<String, String>();
		bookingInfo.setInputs(map);
		bookingResult.setData(bookingInfo);
		bookingResult.setRet(true);
		return bookingResult;

	}
}
class MySecureProtocolSocketFactory1 implements SecureProtocolSocketFactory {
	private SSLContext sslcontext = null;
	
	private SSLContext createSSLContext() {
		SSLContext sslcontext=null;
		try {
			sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager1()}, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return sslcontext;
	}
	
	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}
	
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(
				socket,
				host,
				port,
				autoClose
				);
	}
	
	public Socket createSocket(String host, int port) throws IOException,
	UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(
				host,
				port
				);
	}
	
	
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}
	
	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress, localPort);
		} else {
			Socket socket = socketfactory.createSocket();
			SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}
	
	//自定义私有类
	private class TrustAnyTrustManager1 implements X509TrustManager {
		
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[]{};
		}
	}
	
	
}
