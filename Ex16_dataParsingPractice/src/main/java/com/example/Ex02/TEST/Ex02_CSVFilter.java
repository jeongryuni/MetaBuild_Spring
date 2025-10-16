package com.example.Ex02.TEST;

import java.io.*;

public class Ex02_CSVFilter {
    public static void main(String[] args) {

        // 강남구 도서관 출력
        File file =new File("file\\서울시 공공도서관 현황정보.csv");
        BufferedReader br;
        String line;

        try {
            br = new BufferedReader(new FileReader(file));
            while((line = br.readLine()) != null){
                String[]arr = line.split(",");
                if(arr[3].equals("\"강남구\"")){
                    System.out.println(line);
                }
            }

            br.close();
            br = new BufferedReader(new FileReader(file));

            // 어린이가 포함된 도서관 출력
            int count=0;
            while((line = br.readLine()) != null){
                String[]arr = line.split(",");
                if (arr[1].contains("어린이")) {
                    count++;
                    System.out.println(line);
                }
            }
            System.out.println("총 어린이 도서관 개수: " + count);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
