package com.tibco.cep.pattern.impl.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/*
* Author: Ashwin Jayaprakash Date: Oct 15, 2009 Time: 2:43:28 PM
*/
public class HashGenerator {
    public static String toMD5Hash(Object object) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            String string = object.toString();
            byte buf[] = string.getBytes();
            byte[] md5 = md.digest(buf);

            BigInteger number = new BigInteger(1, md5);
            String s = number.toString(16);

            return s;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
