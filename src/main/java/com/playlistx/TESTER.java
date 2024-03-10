package com.playlistx;

import com.playlistx.model.login.UserName;

import java.util.HashMap;

public class TESTER {
    public static void main(String[] args) {
        HashMap<String, Integer> integers = new HashMap<>();
        integers.put("SEX", 1);
        integers.put("SEX", 3);
        System.out.println(integers.get("SEX"));
    }
}
