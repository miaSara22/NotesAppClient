package com.miaekebom.mynotesapp.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Test {

    public static void main(String[] args) {

        try {
            URL url = new URL("http://192.168.1.103:9000/loginUser");

            URLConnection con = url.openConnection();
            con.setRequestProperty("email","hi@gmail.com");
            con.setRequestProperty("pwd","333");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String  s ="";
            while((s = reader.readLine())!= null)
                System.out.println(s);
            reader.close();
        } catch(Exception e) {}
    }

}
