package com.graduate.musicback.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ValidateCode {

    public String getValidateCode(){
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb=new StringBuilder(6);
        for(int i=0;i<6;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString().toLowerCase();
    }
}
