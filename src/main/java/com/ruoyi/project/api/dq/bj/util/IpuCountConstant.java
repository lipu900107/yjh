package com.ruoyi.project.api.dq.bj.util;

public class IpuCountConstant {
	
    public static final String COUNT_FILE_PATH = "IpuCount";
    public static String DEFAULT_SERVER_IP = "123.57.35.51";
    public static int DEFAULT_SERVER_PORT = 9999;
    public static final String LINE_SEPARATOR = System.getProperties().getProperty("line.separator");
    public static final String LOG_ITEM_SEPARATOR = "~~~";
    public static final String LOG_SEPERATOR = "###";
    public static String UTF_8 = "UTF-8";

    public static class Result {
        public static final String ERROR_CODE = "-1";
        public static final String SESSION_ERROR_CODE = "-100";
        public static final String SUCCESS_CODE = "0";
    }
}
