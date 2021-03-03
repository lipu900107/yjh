package com.ruoyi.project.api.dq.bj.util;

import com.alibaba.fastjson.JSONException;

import java.io.*;

public class JSONTokener {
	
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;

    public JSONTokener(Reader reader) {
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader);
        }
        this.reader = reader;
        this.eof = false;
        this.usePrevious = false;
        this.previous = '\u0000';
        this.index = 0;
        this.character = 1;
        this.line = 1;
    }

    public JSONTokener(InputStream inputStream) throws JSONException {
        this(new InputStreamReader(inputStream));
    }

    public JSONTokener(String s) {
        this(new StringReader(s));
    }

    public void back() throws JSONException {
        if (this.usePrevious || this.index <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.index--;
        this.character--;
        this.usePrevious = true;
        this.eof = false;
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 55;
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 87;
    }

    public boolean end() {
        return this.eof && !this.usePrevious;
    }

    public boolean more() throws JSONException {
        next();
        if (end()) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        int c;
        long j = 0;
        if (this.usePrevious) {
            this.usePrevious = false;
            c = this.previous;
        } else {
            try {
                c = this.reader.read();
                if (c <= 0) {
                    this.eof = true;
                    c = 0;
                }
            } catch (Throwable exception) {
                throw new JSONException(exception.getMessage());
            }
        }
        this.index++;
        if (this.previous == '\r') {
            this.line++;
            if (c != 10) {
                j = 1;
            }
            this.character = j;
        } else if (c == 10) {
            this.line = 1 + this.line;
            this.character = 0;
        } else {
            this.character++;
        }
        this.previous = (char) c;
        return this.previous;
    }

    public char next(char c) throws JSONException {
        char n = next();
        if (n == c) {
            return n;
        }
        throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
    }

    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        char[] chars = new char[n];
        for (int pos = 0; pos < n; pos++) {
            chars[pos] = next();
            if (end()) {
                throw syntaxError("Substring bounds error");
            }
        }
        return new String(chars);
    }

    public char nextClean() throws JSONException {
        char c;
        do {
            c = next();
            if (c == '\u0000') {
                break;
            }
        } while (c <= ' ');
        return c;
    }

    public String nextString(char quote) throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            switch (c) {
                case '\u0000':
                case '\n':
                case '\r':
                    throw syntaxError("Unterminated string");
                case 92 /*92*/:
                    c = next();
                    switch (c) {
                        case '\"':
                        case '\'':
                        case '/':
                        case 92 /*92*/:
                            sb.append(c);
                            break;
                        case 98 /*98*/:
                            sb.append('\b');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 110 /*110*/:
                            sb.append('\n');
                            break;
                        case 114 /*114*/:
                            sb.append('\r');
                            break;
                        case 116 /*116*/:
                            sb.append('\t');
                            break;
                        case 117 /*117*/:
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        default:
                            throw syntaxError("Illegal escape.");
                    }
                default:
                    if (c != quote) {
                        sb.append(c);
                        break;
                    }
                    return sb.toString();
            }
        }
    }

//    public String nextTo(char delimiter) throws JSONException {
//        StringBuilder sb = new StringBuilder();
//        char c = next();
//        while (true) {
//            if (c != delimiter && c != '\u0000' && c != '\n' && c != '\r') {
//                sb.append(c);
//            } else if (c != '\u0000') {
//                back();
//            }
//        }
//        if (c != '\u0000') {
//            back();
//        }
//        return sb.toString().trim();
//    }

//    public String nextTo(String delimiters) throws JSONException {
//        StringBuilder sb = new StringBuilder();
//        while (true) {
//            char c = next();
//            if (delimiters.indexOf(c) < 0 && c != '\u0000' && c != '\n' && c != '\r') {
//                sb.append(c);
//            } else if (c != '\u0000') {
//                back();
//            }
//        }
//        if (c != '\u0000') {
//            back();
//        }
//        return sb.toString().trim();
//    }

    public Object nextValue() {
        try {
            char c = nextClean();
            switch (c) {
                case '\"':
                case '\'':
                    return nextString(c);
                case 91 /*91*/:
                    back();
                    return new JSONArray(this);
                case 123 /*123*/:
                    back();
                    return new JSONObject(this);
                default:
                    StringBuilder sb = new StringBuilder();
                    while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                        sb.append(c);
                        c = next();
                    }
                    back();
                    String string = sb.toString().trim();
                    if (!"".equals(string)) {
                        return JSONObject.stringToValue(string);
                    }
                    throw syntaxError("Missing value");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public char skipTo(char to) throws JSONException {
        try {
            char c;
            long startIndex = this.index;
            long startCharacter = this.character;
            long startLine = this.line;
            this.reader.mark(1000000);
            do {
                c = next();
                if (c == '\u0000') {
                    this.reader.reset();
                    this.index = startIndex;
                    this.character = startCharacter;
                    this.line = startLine;
                    break;
                }
            } while (c != to);
            back();
            return c;
        } catch (Throwable exception) {
            throw new JSONException(exception.getMessage());
        }
    }

    public JSONException syntaxError(String message) {
        return new JSONException(message + toString());
    }

    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}
