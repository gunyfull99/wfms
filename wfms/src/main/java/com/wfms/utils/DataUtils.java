package com.wfms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DataUtils {
    public static String generateTempPwd(int length){
        String number ="0123456789qwertyuiopasdfghjklzxcvbnm";
        char otp[]=new char[length];
        Random getOtpNum= new Random();
        for (int i = 0; i <length ; i++) {
        otp[i]=number.charAt(getOtpNum.nextInt(number.length()));
        }
            String otpCpde="";
        for (int i = 0; i <otp.length ; i++) {
            otpCpde+=otp[i];
        }
        return  otpCpde;
        }
        public static boolean notNullOrEmpty(String str){
            if(str != null &&( !str.isEmpty()|| !str.isBlank())){
                return true;
            }
            return false;
        }
    public static boolean listNotNullOrEmpty(List<?> listObject){
        if(listObject != null && (listObject.size() != 0)){
            return true;
        }
        return false;
    }

    public static boolean notNull(Object list) {
        if (list == null) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Date convertStringToDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
