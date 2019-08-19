package network.demo;

import network.compuer.ComputerInfo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



@SpringBootApplication
public class TestSenInfo {
  /*  public static void main(String[] args)
    {
        String url = "http://127.0.0.1:8960/services/zoomlShopSynImpIF";
//  http://localhost:8080
//用于创建JAX-WS代理的工厂类
      *//*  JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.getInInterceptors().add(new LoggingInInterceptor());
        factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
        factoryBean.setServiceClass(ZoomlShopSynIF.class);
        factoryBean.setAddress(url);*//*
       // ZoomlShopSynIF apiObj = (ZoomlShopSynIF) factoryBean.create();
    }*/




    public String workWatingTimeUpload(String shipdocID, String InProject, String OutProject, String WaitTime, String Distance) {
        String urlStr = "http://10.88.89.107/network/info/SetWaitTime?shipdocID=" + shipdocID + "&InProject=" + InProject + "&OutProject=" + OutProject + "&WaitTime=" + WaitTime + "&Distance=" + Distance + "".trim();
        System.out.println(urlStr);
        //链接URL  
        BufferedReader reader = null;
        StringBuffer document = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            //返回结果集
            //创建链接  
            URLConnection conn = url.openConnection();
            //读取返回结果集
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                document.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document.toString();
    }






}

