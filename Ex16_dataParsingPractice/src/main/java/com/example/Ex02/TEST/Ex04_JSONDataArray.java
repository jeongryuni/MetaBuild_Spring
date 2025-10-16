package com.example.Ex02.TEST;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ex04_JSONDataArray {
    public static void main(String[] args) throws IOException, ParseException {

        Path currentPath = Paths.get("."); // 현재 위치
        String path = currentPath.toAbsolutePath().toString();
        System.out.println("path : " + path); // path : C:\Spring_study\Ex16\.

        // code_value : 강남구 , lbrry_name : 도서관 이름
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader("file\\서울시 공공도서관 현황정보3_ANSI.json");
        JSONObject jsonObject = (JSONObject)parser.parse(reader);

        String searchKey = "code_value";
        String getKey = "lbrry_name";

        JSONArray data = (JSONArray) jsonObject.get("DATA");
        for (int index = 0; index < data.size(); index++) {
            JSONObject object = (JSONObject) data.get(index);

            if (object.get(searchKey).equals("강남구")) {
                System.out.println(object.get(getKey));
            }
        }

    }
}
