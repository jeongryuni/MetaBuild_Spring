package com.example.Ex02.TEST;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Ex07_JSON_Parsing_Practice {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ParseException {

        // http://openapi.seoul.go.kr:8088/(인증키)/json/tbCycleStationUseMonthInfo/1/5/202208
        // (RENTCNT)대여건수 => 3000 의 (STATIONNAME)대여소명 조회

        StringBuilder urlBuilder = new StringBuilder(" http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/"+ URLEncoder.encode("766b505a67776a643836676d78725a", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("tbCycleStationUseMonthInfo", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("200", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("202208", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //연결다리
        String line;
        StringBuilder jsonLine = new StringBuilder();

        while( (line = reader.readLine()) != null){
            jsonLine.append(line);
            System.out.println(line);
        }
        reader.close();

        // JSON 파싱
        JSONParser parser  = new JSONParser();
        JSONObject root = (JSONObject)parser.parse(jsonLine.toString());

        // 최상위 계층 파싱
        JSONObject info = (JSONObject) root.get("stationUseMonthInfo");

        // 하위 "row" 접근
        JSONArray rowArr = (JSONArray) info.get("row");


        for (int i = 0; i < rowArr.size(); i++) {
            JSONObject data = (JSONObject) rowArr.get(i);

            // 1) 변수 설정 : rentCnt int설정
            int rentCnt = Integer.parseInt((String) data.get("rentCnt"));
            // 2) 변수 설정
            String stationName =  (String)data.get("stationName");

            if(rentCnt>= 3000){
                System.out.println("대여소명: " + stationName + '\t' +"대여건수: " + rentCnt);
            }
        }
    }
}
