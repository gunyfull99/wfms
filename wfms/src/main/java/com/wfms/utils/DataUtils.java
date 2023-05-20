package com.wfms.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
 import java.time.LocalDateTime;
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
    public static String generateNumber(int length){
        String number ="0123456789";
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

    public static LocalDateTime convertStringToDate(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDateTime.parse(date, formatter);
        } catch (Exception e) {
            return null;
        }
    }
    public static LocalDateTime getPeriodDate(LocalDateTime start, LocalDateTime end, double priority){
        long minutes = Duration.between(start, end).toMinutes();
        System.out.println("aaaaaaaaaaaaa "+ minutes);
        System.out.println("cccccccccccccc "+ end.plusMinutes((long) (minutes * priority  *-(1))));
        return end.plusMinutes((long) (minutes * priority  *-(1)));
    }
}
