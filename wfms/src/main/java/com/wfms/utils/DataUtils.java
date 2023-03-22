package com.wfms.utils;

import org.apache.poi.ss.formula.functions.T;

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
            if(!str.isEmpty()|| !str.isBlank()){
                return true;
            }else if(str != null){
                return true;
            }
            return false;
        }
    public static boolean listNotNullOrEmpty(List<?> listObject){
        if(!listObject.isEmpty()|| listObject.size()>0){
            return true;
        }else if(listObject != null){
            return true;
        }
        return false;
    }
        public static boolean notNull(Object list){
            if (list == null){
                return false;
            }
            return true;
        }
}
