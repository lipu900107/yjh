package com.ruoyi.project.api.dq.bj.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHeaders;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

public class HttpTool {
	
    static final String TAG = HttpTool.class.getSimpleName();
    public static final int conTimeout = 5000;
    public static final String defaultEncode = "UTF-8";
    public static final int readTimeout = 120000;

    public static class DownStreamOper {
        public void startDown() {
        }

        public void downloading(InputStream in) throws Exception {
        }

        public void endDown(boolean flag, String response) {
        }
    }

    public static class DownloadOper {
        public void startDownload(int fileSize) {
        }

        public void downloading(int fileSize, int count) {
        }

        public void endDownload(boolean flag, String response) {
        }
    }

    static class FakeX509TrustManager implements X509TrustManager {
        FakeX509TrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] certificate, String type) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    }

    public static String httpRequest(String url, String data, String type) throws Exception {
        return httpRequest(url, data, type, "UTF-8", readTimeout, 5000);
    }

    public static String httpRequest(String url, String data, String type, String encode) throws Exception {
        return httpRequest(url, data, type, encode, readTimeout, 5000);
    }

    public static String httpRequest(String requestUrl, String data, String type,
                                     String encode, int readTimeout,
                                     int conTimeout) throws Exception {
        Throwable e;
        Throwable th;
        if (encode == null || "".equals(encode)) {
            encode = "UTF-8";
        }
        if (readTimeout <= readTimeout) {
            readTimeout = readTimeout;
        }
        if (conTimeout <= 5000) {
            conTimeout = 5000;
        }
        InputStream is = null;
        OutputStreamWriter writer = null;
        URL url = new URL(requestUrl);
        HttpURLConnection conn = null;
        try {
            if (requestUrl.startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
                TrustManager[] tm = new TrustManager[]{new FakeX509TrustManager()};
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, tm, null);
                ((HttpsURLConnection) conn).setSSLSocketFactory(ctx.getSocketFactory());
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(conTimeout);
            conn.setReadTimeout(readTimeout);
            if ("POST".equals(type)) {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
            } else if ("GET".equals(type)) {
                conn.setRequestMethod("GET");
            }
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", URLEncodedUtils.CONTENT_TYPE);
            conn.connect();
            if (data != null) {
                OutputStreamWriter writer2 = new OutputStreamWriter(conn.getOutputStream(), encode);
                try {
                    writer2.write(data);
                    writer2.flush();
                    writer = writer2;
                } catch (Exception e2) {
                    e = e2;
                    writer = writer2;
                    try {
                        e.printStackTrace();
                        throw e;
                    } catch (Throwable th2) {
                        th = th2;
                        if (is != null) {
                            is.close();
                        }
                        if (writer != null) {
                            writer.close();
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    writer = writer2;
                    if (is != null) {
                        is.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    throw th;
                }
            }
            System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("响应错误");
            }
            is = conn.getInputStream();
            String parseInputStream = parseInputStream(is, encode);
            if (is != null) {
                is.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
            return parseInputStream;
        } catch (Throwable e3) {
            e = e3;
            e.printStackTrace();
            try {
                throw e;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return null;
    }

    private static String parseInputStream(InputStream is, String encode) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
        StringBuffer buff = new StringBuffer();
        String nl = IpuBasicConstant.LINE_SEPARATOR;
        while (true) {
            String result = reader.readLine();
            if (result == null) {
                break;
            }
            buff.append(result).append(nl);
        }
        if (buff.length() > 0) {
            buff.setLength(buff.length() - 1);
        }
        return buff.toString();
    }

    public static String toQueryString(Map<String, String> data) {
        StringBuilder buff = new StringBuilder();
        for (String key : data.keySet()) {
            buff.append(key).append("=").append((String) data.get(key)).append("&");
        }
        buff.setLength(buff.length() - 1);
        return buff.toString();
    }

    public static String toQueryStringWithEncode(Map<String, String> data) {
        StringBuilder buff = new StringBuilder();
        for (String key : data.keySet()) {
            buff.append(postDataEncode(key)).append("=").append(postDataEncode((String) data.get(key))).append("&");
        }
        buff.setLength(buff.length() - 1);
        return buff.toString();
    }

    public static String postDataEncode(String data) {
        if (data == null || "".equals(data)) {
            return "";
        }
        if (data.indexOf(37) == -1 && data.indexOf(43) == -1 && data.indexOf(38) == -1) {
            return data;
        }
        int length = data.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = data.charAt(i);
            switch (c) {
                case '%':
                    sb.append("%25");
                    break;
                case '&':
                    sb.append("%26");
                    break;
                case '+':
                    sb.append("%2B");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static String urlEncode(String url, String encode) {
        if (url == null || "".equals(url)) {
            return "";
        }
        if (encode == null || "".equals(encode)) {
            encode = "UTF-8";
        }
        Matcher m = Pattern.compile("[㐀-䶵一-龥龦-龻豈-鶴侮-頻並-龎＀-￯⺀-⻿　-〿㇀-㇯\\s]+").matcher(url);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            try {
                m.appendReplacement(sb, URLEncoder.encode(url.substring(m.start(), m.end()), encode));
            } catch (Exception e) {
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String urlEscape(String url) throws URISyntaxException, MalformedURLException {
        URL _url = new URL(url);
        return new URI(_url.getProtocol(), _url.getUserInfo(), _url.getHost(), _url.getPort(), _url.getPath(), _url.getQuery(), null).toString();
    }

    public static String httpDownload(String requestUrl, String filePath) throws Exception {
        return httpDownload(requestUrl, filePath, 5000, new DownloadOper(), false);
    }

    public static String httpDownload(String requestUrl, String filePath, int timeout, DownloadOper oper) throws Exception {
        return httpDownload(requestUrl, filePath, timeout, oper, false);
    }

    public static String httpDownload(String requestUrl, String filePath, int timeout, DownloadOper oper, boolean isResume) throws Exception {
        Throwable e;
        Throwable th;
        InputStream in = null;
        OutputStream out = null;
        boolean flag = true;
        URL url = new URL(requestUrl);
        HttpURLConnection conn = null;
        String response;
        try {
            if (requestUrl.startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
                TrustManager[] tm = new TrustManager[]{new FakeX509TrustManager()};
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, tm, null);
                ((HttpsURLConnection) conn).setSSLSocketFactory(ctx.getSocketFactory());
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(timeout);
            conn.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, HTTP.IDENTITY_CODING);
            if (isResume) {
                File file = new File(filePath);
                if (file.exists()) {
                    conn.setRequestProperty("RANGE", "bytes=" + file.length() + "-");
                }
            }
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206) {
                in = conn.getInputStream();
                int fileSize = conn.getContentLength();
                if (fileSize < 0) {
                    throw new RuntimeException("请求异常");
                } else if (in == null) {
                    throw new RuntimeException("请求异常");
                } else {
                    oper.startDownload(fileSize);
                    OutputStream fileOutputStream = new FileOutputStream(filePath);
                    try {
                        BufferedInputStream buffIn = new BufferedInputStream(in);
                        BufferedOutputStream buffOut = new BufferedOutputStream(fileOutputStream);
                        byte[] bytes = new byte[8192];
                        while (true) {
                            int c = buffIn.read(bytes);
                            if (c == -1) {
                                break;
                            }
                            buffOut.write(bytes, 0, c);
                            oper.downloading(fileSize, c);
                        }
                        buffOut.flush();
                        response = Messages.DOWNLOAD_SUCCESS;
                        if (in != null) {
                            in.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        } else {
                            out = fileOutputStream;
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                        oper.endDownload(true, response);
                        return response;
                    } catch (Exception e2) {
                        e = e2;
                        out = fileOutputStream;
                        flag = false;
                        try {
                            response = e.getMessage() + ":" + requestUrl;
                            throw e;
                        } catch (Throwable th2) {
                            th = th2;
                            if (in != null) {
                                in.close();
                            }
                            if (out != null) {
                                out.close();
                            }
                            if (conn != null) {
                                conn.disconnect();
                            }
                            oper.endDownload(flag, null);
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        out = fileOutputStream;
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                        oper.endDownload(flag, null);
                        throw th;
                    }
                }
            }
            throw new RuntimeException(Messages.EXCEPTION_CONN);
        } catch (Throwable e3) {
            e = e3;
            flag = false;
            response = e.getMessage() + ":" + requestUrl;
            try {
                throw e;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return null;
    }

    public static String httpDownload(String requestUrl, DownStreamOper oper) throws Exception {
        InputStream in = null;
        oper.startDown();
        URL url = new URL(requestUrl);
        HttpURLConnection conn = null;
        String response;
        try {
            if (requestUrl.startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
                TrustManager[] tm = new TrustManager[]{new FakeX509TrustManager()};
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, tm, null);
                ((HttpsURLConnection) conn).setSSLSocketFactory(ctx.getSocketFactory());
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(5000);
            conn.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, HTTP.IDENTITY_CODING);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("请求异常");
            }
            in = conn.getInputStream();
            if (conn.getContentLength() <= 0) {
                throw new RuntimeException("请求异常");
            } else if (in == null) {
                throw new RuntimeException(Messages.FILE_STREAM_NULL);
            } else {
                oper.downloading(in);
                response = Messages.DOWNLOAD_SUCCESS;
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
                oper.endDown(true, response);
                return response;
            }
        } catch (Exception e) {
            response = e.getMessage() + ":" + requestUrl;
            throw e;
        } catch (Throwable th) {
            if (in != null) {
                in.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
            oper.endDown(false, null);
        }

        return null;
    }

    public static Map<String, String> parseResponse(String response) {
        Map<String, String> map = new HashMap();
        for (String agr : response.trim().substring(1, response.length() - 1).replaceAll("\"", "").split(BjConstant.PARAMS_SQE)) {
            String[] strs = agr.split(":");
            if (strs.length == 2) {
                map.put(strs[0], strs[1]);
            }
        }
        return map;
    }

    public static boolean isExistRemoteFile(String requestUrl) {
        URL url;
        Throwable th;
        InputStream inStream = null;
        try {
            URL url2 = new URL(requestUrl);
            try {
                inStream = url2.openConnection().getInputStream();
                if (inStream != null) {
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                        }
                    }
                    url = url2;
                    return true;
                }
                if (inStream != null) {
                    try {
                        inStream.close();
                        url = url2;
                    } catch (IOException e2) {
                        url = url2;
                    }
                }
                return false;
            } catch (Exception e3) {
                url = url2;
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e4) {
                    }
                }
                return false;
            } catch (Throwable th2) {
                th = th2;
                url = url2;
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e5) {
                    }
                }
                throw th;
            }
        } catch (Exception e6) {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } catch (Throwable th3) {
            try {
                th = th3;
                if (inStream != null) {
                    inStream.close();
                }
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return false;
    }
}
