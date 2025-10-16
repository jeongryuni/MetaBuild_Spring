package com.example.Ex02.TEST;

import java.io.*;

public class Ex01_CSVReader {
    public static void main(String[] args) {
        File file =new File("file\\기상청27_관광코스별_관광지_지점정보.csv");
        BufferedReader br;
        String line;
        int count =0;
        try {
            br = new BufferedReader(new FileReader(file)); //연결 다리
            while((line = br.readLine()) != null) {
                count++;
                String [] arr = line.split(",");

                // 남해, 힐링 관광지 출력
                if (arr[4].contains("(남해)") && arr[10].contains("힐링")){
                    System.out.println(line);
                }
            }
            System.out.println("Count :" + count);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
