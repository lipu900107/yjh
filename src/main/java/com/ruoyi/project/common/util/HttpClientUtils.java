package com.ruoyi.project.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
	
	public static String get(String url, String param, String charsetName) {  
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			URLConnection conn = realUrl.openConnection();
            conn.connect();
			
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charsetName));
			String line = "";
			while ((line = in.readLine()) != null) {
				result += "/n" + line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result; 
	}

	public static String get(String url, List<NameValuePair> params, List<NameValuePair> headerParams) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String body = null;
		
		try {
			HttpGet httpget = new HttpGet(url);

			if(params != null && params.size() > 0) {
				String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
				httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			}
			
			HttpResponse httpresponse = httpClient.execute(httpget);
			httpresponse.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
			
			if(headerParams != null) {
				for (NameValuePair param : headerParams) {
					if (param != null && param.getName() != null && param.getValue() != null) {
						httpresponse.setHeader(param.getName(), param.getValue());
					}
				}
			}
			
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			try {
				if(httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return body;
	}

	public static String post(String url, String sendBody, String contentType) {
		return post(url, sendBody, contentType, null);
	}

	public static String post(String strUrl, String sendBody, String contentType, List<NameValuePair> headerParams) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String body = null;
		try {
			URL url = new URL(strUrl);
			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			
			HttpPost post = new HttpPost(uri);
			
			StringEntity se = new StringEntity(sendBody, "UTF-8");
			if(StringUtils.isBlank(contentType)) {
	        	contentType = "application/x-www-form-urlencoded; charset=UTF-8";
	        }
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
			
			post.setEntity(se);
			
			if (headerParams != null) {
				for (NameValuePair param : headerParams) {
					if (param != null && param.getName() != null && param.getValue() != null) {
						post.setHeader(param.getName(), param.getValue());
					}
				}
			}
			
			HttpResponse httpresponse = httpClient.execute(post);
			if(httpresponse.getStatusLine().getStatusCode()!=200){
				return null;
			}
			
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			EntityUtils.consume(entity);

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return body;
	}
	
	public static String sendPost(String url, String param, String contentType) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        
        if(StringUtils.isBlank(contentType)) {
        	contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        }
        
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty(HTTP.CONTENT_TYPE, contentType);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally{
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
}
