package com.ff;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.util.DigestUtils;

/**
 * @author FF
 * @date 2021/11/21
 * @TIME:17:27
 */
public class aa {
    public static void main(String[] args) {
        String s = DigestUtils.md5DigestAsHex("12345".getBytes());
        System.out.println(s);

    }
}
