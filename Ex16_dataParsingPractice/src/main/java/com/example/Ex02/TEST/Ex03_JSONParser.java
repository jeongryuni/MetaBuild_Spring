package com.example.Ex02.TEST;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class Ex03_JSONParser {
    public static void main(String[] args) throws IOException, ParseException, JSONException {

        Path currentPath = Paths.get("."); // 현재 위치
        String path = currentPath.toAbsolutePath().toString();
        System.out.println("path : " + path); // path : C:\Spring_study\Ex16\.

        // ------------------- Ex01 : 단일 JSON 객체 -------------------
        System.out.println("=========Ex01=========");
        JSONParser parser = new JSONParser(); // Json (parser)파서기를 이용해 속성과 값을 분리
        Reader reader = new FileReader("file\\test1.json");
        JSONObject jsonObject = (JSONObject)parser.parse(reader);

        String name = (String) jsonObject.get("name");
        long id = (Long) jsonObject.get("id");
        long price = (Long) jsonObject.get("price");

        System.out.println("name :" + name);
        System.out.println("id :" + id);
        System.out.println("price :" + price);

        Set keySet = jsonObject.keySet();
        for (Object key : keySet) {
            System.out.println("key: " + key + ", value: " + jsonObject.get(key));
        }

        // ------------------- Ex02 : JSON 배열 -------------------
        System.out.println("=========Ex02=========");
        JSONParser parser2 = new JSONParser();
        Reader reader2 = new FileReader("file\\test2.json");
        Object jsonObject2 = parser2.parse(reader2);

        JSONArray jsonArr2 = (JSONArray) jsonObject2;
        System.out.println(jsonArr2.size());

        // 배열 요소 반복 출력
        for (int i = 0; i < jsonArr2.size(); i++) {
            JSONObject jObject = (JSONObject)jsonArr2.get(i);
            Set keySet2 = jObject.keySet();
            for (Object key : keySet2) {
                System.out.println(key + " : " + jObject.get(key));
            }
        }

        // ------------------- Ex03 : 객체 안에 배열 -------------------
        System.out.println("=========Ex03=========");
        JSONParser parser3 = new JSONParser();
        Reader reader3 = new FileReader("file\\test3.json");
        JSONObject jsonObject3 = (JSONObject)parser3.parse(reader3);

        // "employee" 키에 있는 배열 가져오기
        JSONArray jsonArr3  = (JSONArray)jsonObject3.get("employee");

        for (int i = 0; i < jsonArr3.size(); i++){
            JSONObject jObject3 = (JSONObject)jsonArr3.get(i);

            // 배열 내부 객체의 key-value 출력
            Set keySet3 = jObject3.keySet(); // key값이 들어감
            for (Object key : keySet3){
                System.out.println(key +":"+jObject3.get(key));
            }
        }
    }
}
