package com.ruoyi.project.common.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.ruoyi.project.api.znlj.util.ConstantConfig;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Component
public class YsBussinessUtil{
	
	private static final Logger LOGGER = LogManager.getLogger(YsBussinessUtil.class);
	
    public static void main(String[] args) throws Exception {

        YsBussinessUtil dataServiceTest = new YsBussinessUtil();

        //手机号码（11位长度的中国移动号码）
        String phone = "1600413714321987785";
        //注册时间（精确到小时。格式YYYYMMDDHH24）
        String regTime  = "2021012511";
        //获取令牌KEY
        //XStream xstream = new XStream(); 
        //dataServiceTest.getApplyKey(xstream,"736692","AD3691E089EA3367E0533FAE990AC30C","by-ys1-yjh");
        //根据标签修改所需参数
        //String requestXml = createXml(phone,regTime,"DZBQ-AA02");
        //System.out.println(requestXml);
        //发送请求，获取数据
        //String responseXml = doPost(ConstantConfig.opendata_url,requestXml);
        //打印服务接口响应报文
       // System.out.println(responseXml);
    }
    

    //@Test
    public  void mainTest() throws Exception {
        YsBussinessUtil dataServiceTest2 = new YsBussinessUtil();

        /**
         * 手机号码（11位长度的中国移动号码）
         */
        String phone = "13570959644";
        /**
         * 注册时间（精确到小时。格式YYYYMMDDHH24）
         */
        String regTime  = "2011081211";
        //获取令牌KEY
        //dataServiceTest2.getApplyKey(xStream);
        /*
         * 根据标签修改所需参数
         */
        //String requestXml = createXml(phone,regTime,"BAA11");
        //发送请求，获取数据
        //String responseXml = doPost(opendata_url,requestXml);
        //打印服务接口响应报文
        //System.out.println(responseXml);
    }

    /**
     * 以POST请求标签服务接口
     * @param url 标签接口请求地址
     * @param xml 标签接口请求报文
     * @return 标签接口响应报文
     */
    public static String doPost(String url, String xml, Map<String, String> map) {
    	String ap_id = (String)map.get("apId");
    	String app_id = (String)map.get("appId");
    	String client_code = (String)map.get("clientCode");
    	String app_key = (String)map.get("appKey");
    	String key = (String)map.get("key");
    	String seq = (String)map.get("seq");
    	
        StringBuffer respXml = new StringBuffer();
        try {
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(10000);
            con.setReadTimeout(1000000);

            /**
             * 调用系统名（格式：apid#appid）
             */
            String accessName = "accessname=\"" + ap_id + "#" + app_id + "\",";
            /**
             * 客户端编码(必须和key接口请求参数 clientCode一致)
             */
            String clientCode = "clientCode=\"" + client_code + "\",";
            /**
             * AES加密串
             */
            String content = ap_id + "#" + app_id + "#" + app_key + "#" + seq;
            String code ="code=\""+AES.encrypt(content, key)+"\",";
            /**
             * 帐号,可为空（非空，必须Base64转换）
             */
            String accout = "account=\"\"";
            System.out.println(accessName + clientCode + code + accout);
            con.setRequestProperty("Authorization", accessName + clientCode + code + accout);

            con.setRequestProperty("Content-Type", "application/xml;charset=utf-8");
            con.connect();

            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(xml);
            osw.flush();
            osw.close();

            //发送请求，并读取返回内容
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                respXml.append(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return respXml.toString();
    }

    /**
     * 必须保证seq在单个KEY的有效时间内为唯一值
     * @return
     */
    /*private synchronized static int getSeq(){
        return seq++;
    }*/

    /**
     * 创建服务接口请求报文
     * @param phone 手机号码
     * @param regTime 注册时间
     * @param servCode 服务编码
     * @return XML请求报文
     */
    public static String createXml(String phone, String regTime, String servCode,Map<String, String> map) {
    	String ap_id = (String)map.get("apId");
    	String app_id = (String)map.get("appId");
    	String seq = (String)map.get("seq");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xml = db.newDocument();

            Element OpenDataRequest = xml.createElement("OpenDataRequest");
            Element RequestSEQ = xml.createElement("RequestSEQ");
            Element RequestTime = xml.createElement("RequestTime");
            Element APID = xml.createElement("APID");
            Element APPID = xml.createElement("APPID");
            Element ServCode = xml.createElement("ServCode");
            Element ServArgs = xml.createElement("ServArgs");
            Element Phone = xml.createElement("Phone");
            Element RegTime = xml.createElement("RegTime");
            Element AuthCode = xml.createElement("AuthCode");
            Element CookieID = xml.createElement("CookieID");

            //给元素赋值
            String nowTime = getNowTime("yyyyMMddHHmmssSSS");
            RequestSEQ.setTextContent(app_id + nowTime + seq);
            RequestTime.setTextContent(nowTime);
            APID.setTextContent(ap_id);
            APPID.setTextContent(app_id);
            ServCode.setTextContent(servCode);
            CookieID.setTextContent(phone);
            //Phone.setTextContent(phone);
            //RegTime.setTextContent(regTime);
            //AuthCode.setTextContent("afdasfdsaffa");

            //构建XML
            OpenDataRequest.appendChild(RequestSEQ);
            OpenDataRequest.appendChild(RequestTime);
            OpenDataRequest.appendChild(APID);
            OpenDataRequest.appendChild(APPID);
            OpenDataRequest.appendChild(ServCode);
            OpenDataRequest.appendChild(ServArgs);
            ServArgs.appendChild(CookieID);
            /*ServArgs.appendChild(Phone);
            ServArgs.appendChild(RegTime);
            ServArgs.appendChild(AuthCode);*/

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer trans = transFactory.newTransformer();
            DOMSource source = new DOMSource(OpenDataRequest);
            trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            trans.transform(source, result);

            return sw.toString();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取标签服务接口头部Authorization鉴权属性key
     */
    public synchronized Map<String, Object> getApplyKey(XStream xStream,Map<String, String> map) throws Exception {
    	String ap_id = (String)map.get("apId");
    	String ap_key = (String)map.get("apKey");
    	String client_code = (String)map.get("clientCode");
    	
    	String date_format = "yyyyMMddHHmmss";
        long nowTime = Long.valueOf(getNowTime(date_format));
        //封装发送的数据
        RequestApplyKey bean = new RequestApplyKey();
        bean.setApId(ap_id);
        bean.setApKey(ap_key);
        bean.setClientCode(client_code);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(RequestApplyKey.class);
        xStream.processAnnotations(ResponseApplyKey.class);
        String xml = xStream.toXML(bean);
        System.out.println(xml);
        //数据加密
        String data = encryptByPublicKey(xml, ConstantConfig.publicKey_1);
        LOGGER.warn("有数获取令牌KEY请求参数:" + data);
        //若超过失效时间则重新获取令牌KEY
        String applyKeyXml = doPost(ConstantConfig.applykey_url ,data, map);
        LOGGER.warn("有数获取令牌KEY返回结果result:" + applyKeyXml);
        //解密
        String responseXML = decryptByPublicKey(applyKeyXml, ConstantConfig.publicKey_1);
        LOGGER.warn("有数获取令牌XML解析结果responseXML:" + responseXML);
        ResponseApplyKey responseApplyKey  = (ResponseApplyKey) xStream.fromXML(responseXML);
        String resultkey = StringUtils.isBlank(responseApplyKey.getKey())? "":responseApplyKey.getKey(); //更新令牌KEY
        int resultSeq = StringUtils.isBlank(responseApplyKey.getSeq())? 0:Integer.valueOf(responseApplyKey.getSeq());  //当KEY更新时，seq重置
        long resultExpiryDate = Long.valueOf(addDay(String.valueOf(nowTime), date_format, 1));  //更新失效时间，24小时内有效
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("key", resultkey);
        resultMap.put("seq", resultSeq);
        resultMap.put("expiryDate", resultExpiryDate);
        return resultMap;
    }

    private static String addDay(String date, String format, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date d = sdf.parse(date);
            return addDay(d,format,days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param date
     * @param format
     * @param days
     * @return
     */
    private static String addDay(Date date, String format, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return sdf.format(c.getTime());
    }

    /**
     * 将key接口响应报文转化为HashMap存储
     * @param applyKeyXml key接口响应报文
     * @return HashMap存储
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     */
    private static HashMap<String,String> parseApplyKey(String applyKeyXml) throws ParserConfigurationException, SAXException, IOException, ParseException{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(applyKeyXml.getBytes("UTF-8"));
        Document responseDoc = docBuilder.parse(is);

        if (is != null) is.close();

        HashMap<String,String> applyKeyMap = new HashMap<String,String>();
        String key = getFirstNodeValue(responseDoc, "key");
        String seq = getFirstNodeValue(responseDoc, "seq");
        applyKeyMap.put("key", key);
        applyKeyMap.put("seq", seq);

        return applyKeyMap;
    }


    /**
     * 解析XML方法（返回第一个找到的节点值）
     * @param xml XML文档
     * @param nodeName 节点名
     * @return 如果找不到节点则返回null
     */
    private static String getFirstNodeValue(Document xml, String nodeName) {
        String temp = null;
        if (xml != null) {
            NodeList nodeList = xml.getElementsByTagName(nodeName);
            if (nodeList.getLength() > 0 ) {
                temp = nodeList.item(0).getTextContent();
            }
        }

        return temp;
    }

    /**
     * 以GET的方式请求key接口
     * @param url 测试环境key接口请求地址
     * @return 响应报文
     */
    private static String doGet(String url) {
        StringBuffer respXml = new StringBuffer();
        try {
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String lines;
            StringBuffer response = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                response.append(lines);
            }

            reader.close();
            con.disconnect();
            respXml.append( response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respXml.toString();
    }

    /**
     * 格式化时间方法
     * @param format 时间根式化表达式
     * @return 格式化时间
     */
    private static String getNowTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * AES加密类
     * @author lWX499239
     *
     */
    static class AES {
        public static final String VIPARA = "0102030405060708";
        public static final String bm = "GBK";

        /**
         * 加密方法
         * @param cleartext 加密内容
         * @param privateKey 加密秘钥
         * @return
         */
        public static String encrypt(String cleartext, String privateKey) {
            try {
                IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());

                SecretKeySpec key = new SecretKeySpec(privateKey.getBytes(), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
                byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));

                return Base64.encode(encryptedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 解密方法
         * @param encrypted 解密内容
         * @param privateKey 解密秘钥
         * @return
         */
        public static String decrypt(String encrypted, String privateKey) {
            try {
                byte[] byteMi = Base64.decode(encrypted);
                IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
                SecretKeySpec key = new SecretKeySpec(privateKey.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
                byte[] decryptedData = cipher.doFinal(byteMi);

                return new String(decryptedData, bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static class Base64 {
        private static char Base64Code[] = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
                'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', '+', '/'
        };

        private static byte Base64Decode[] = {
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, 62, -1, 63, -1, 63, 52, 53,
                54, 55, 56, 57, 58, 59, 60, 61, -1, -1,
                -1, 0, -1, -1, -1, 0, 1, 2, 3, 4,
                5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
                25, -1, -1, -1, -1, -1, -1, 26, 27, 28,
                29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
                39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
                49, 50, 51, -1, -1, -1, -1, -1
        };

        private Base64(){}

        public static String encode(byte b[]) {
            int code = 0;
            StringBuffer sb = new StringBuffer((b.length - 1) / 3 << 6);
            for(int i = 0; i < b.length; i++) {
                code |= b[i] << 16 - (i % 3) * 8 & 255 << 16 - (i % 3) * 8;
                if(i % 3 == 2 || i == b.length - 1) {
                    sb.append(Base64Code[(code & 0xfc0000) >>> 18]);
                    sb.append(Base64Code[(code & 0x3f000) >>> 12]);
                    sb.append(Base64Code[(code & 0xfc0) >>> 6]);
                    sb.append(Base64Code[code & 0x3f]);
                    code = 0;
                }
            }

            if(b.length % 3 > 0)
                sb.setCharAt(sb.length() - 1, '=');
            if(b.length % 3 == 1)
                sb.setCharAt(sb.length() - 2, '=');

            return sb.toString();
        }

        public static byte[] decode(String code) {
            if(code == null)
                return null;

            int len = code.length();
            if(len == 0)
                return new byte[0];
            if(len % 4 != 0)
                throw new IllegalArgumentException("Base64 string length must be 4*n");

            int pad = 0;
            if(code.charAt(len - 1) == '=')
                pad++;
            if(code.charAt(len - 2) == '=')
                pad++;
            int retLen = (len / 4) * 3 - pad;
            byte ret[] = new byte[retLen];
            for(int i = 0; i < len; i += 4) {
                int j = (i / 4) * 3;
                char ch1 = code.charAt(i);
                char ch2 = code.charAt(i + 1);
                char ch3 = code.charAt(i + 2);
                char ch4 = code.charAt(i + 3);
                int tmp = Base64Decode[ch1] << 18 | Base64Decode[ch2]
                        << 12 | Base64Decode[ch3] << 6 | Base64Decode[ch4];
                ret[j] = (byte)((tmp & 0xff0000) >> 16);
                if(i < len - 4) {
                    ret[j + 1] = (byte)((tmp & 0xff00) >> 8);
                    ret[j + 2] = (byte)(tmp & 0xff);
                    continue;
                }
                if(j + 1 < retLen)
                    ret[j + 1] = (byte)((tmp & 0xff00) >> 8);
                if(j + 2 < retLen)
                    ret[j + 2] = (byte)(tmp & 0xff);
            }

            return ret;
        }
    }

    //公钥加密
    public static String encryptByPublicKey(String param, String publicKey)
            throws Exception {

        byte[] data = param.getBytes("UTF-8");
        if( data == null || publicKey == null )
        {
            throw new NullPointerException("encryptByPublicKey data||publicKey is null");
        }

        byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKey);
        // byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;

        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0)
        {
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 117;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return new BASE64Encoder().encode(encryptedData);
    }

    //公钥解密
    public static String decryptByPublicKey(String param, String publicKey)
            throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bates = decoder.decodeBuffer(param);
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKey);
        //  byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = bates.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(bates, offSet, 128);
            } else {
                cache = cipher.doFinal(bates, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        String pubDecode = new String(decryptedData, "UTF-8");
        return pubDecode;
    }
}


@XStreamAlias("applyKey")
@Data
class RequestApplyKey{

    @XStreamAlias("apId")
    private String apId;
    @XStreamAlias("apKey")
    private String apKey;
    @XStreamAlias("clientCode")
    private String clientCode;
    @XStreamAlias("encryptKey")
    private String encryptKey;

}

@XStreamAlias("applyKey")
@Data
class ResponseApplyKey{

    @XStreamAlias("responseID")
    String responseID = "";
    //返回的结果编码
    @XStreamAlias("resultCode")
    String resultCode = "";
    //返回的编码描述
    @XStreamAlias("codeDesc")
    String codeDesc = "";
    @XStreamAlias("clientCode")
    String clientCode = "";
    @XStreamAlias("key")
    String key = "";
    @XStreamAlias("seq")
    String seq = "";
    @XStreamAlias("createDate")
    String createDate = "";
    @XStreamAlias("expiryDate")
    String expiryDate = "";
    @XStreamAlias("publicKey")
    String publicKey;

}


@XStreamAlias("applyDataService")
@Data
class ResponseApplyDataService{

    //返回的结果编码
    @XStreamAlias("resultCode")
    String resultCode = "";
    @XStreamAlias("responseSEQ")
    String responseSEQ = "";
    @XStreamAlias("responseTime")
    String responseTime = "";
    @XStreamAlias("requestSEQ")
    String requestSEQ = "";
    @XStreamAlias("aPID")
    String aPID = "";
    @XStreamAlias("aPPID")
    String aPPID = "";
    @XStreamAlias("servCode")
    String servCode = "";
    @XStreamAlias("servResult")
    String servResult = "";
    String row = "";
    @XStreamAlias("row")
    String name = "";
    @XStreamAlias("name")
    String attrList = "";
    @XStreamAlias("value")
    String value = "";
    @XStreamAlias("isBilling")
    String isBilling = "";
    @XStreamAlias("isValid")
    String isValid = "";
    @XStreamAlias("channel")
    String channel = "";
    @XStreamAlias("cookieID")
    String cookieID = "";

}