package com.example.Ex02.TEST;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Ex06_OpenAPIRequest {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        // 샘플URL : http://openapi.seoul.go.kr:8088/(인증키)/xml/SeoulPublicLibraryInfo/1/5/
        StringBuilder urlBuilder = new StringBuilder(" http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/"+ URLEncoder.encode("766b505a67776a643836676d78725a", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("xml", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("SeoulPublicLibraryInfo", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode("5", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //연결다리
        StringBuilder xmlResponse = new StringBuilder();
        String line;
        StringBuilder xmlLine = new StringBuilder();

        while( (line = reader.readLine()) != null){
            System.out.println("line" + line);
            xmlLine.append(line);
        }
        reader.close();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(xmlLine.toString().getBytes("UTF-8")));

        int rowLength = document.getElementsByTagName("row").getLength();
        System.out.println("rowLength : " + rowLength);

        NodeList LBRRY_NAME_nodeList = document.getElementsByTagName("LBRRY_NAME");
        NodeList CODE_VALUE_nodeList = document.getElementsByTagName("CODE_VALUE");

        for(int i=0; i<rowLength; i++){
            Node LBRRY_NAME_node = LBRRY_NAME_nodeList.item(i);
            Node CODE_VALUEE_node = CODE_VALUE_nodeList.item(i);
            System.out.println(LBRRY_NAME_node.getTextContent()+"/"+CODE_VALUEE_node.getTextContent());



        }

    }
}
