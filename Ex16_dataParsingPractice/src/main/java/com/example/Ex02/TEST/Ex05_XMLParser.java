package com.example.Ex02.TEST;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Ex05_XMLParser {
    public static void main(String[] args) {

        try {
            File xmlFile = new File("file\\info.xml"); // ① XML 파일 객체 생성 (읽어올 파일 경로 지정)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // // ② DOM 파서를 생성하기 위한 팩토리 객체 생성
            DocumentBuilder builder = factory.newDocumentBuilder(); // ③ 실제 XML 파서를 생성
            Document document = builder.parse(xmlFile); //  ④  info.xml 파싱 (태그와 text분리) 후 document 변수에 담기

            NodeList nodeList = document.getElementsByTagName("person"); // people태그를 가져와서 nodeList에 담기
            System.out.println("nodeList:" + nodeList);
            System.out.println("nodeList.getLength():" + nodeList.getLength()); // person태그 개수 : 2

            for(int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);

                Element element = (Element)node;
                //System.out.println("element :" + element);
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                String age = element.getElementsByTagName("age").item(0).getTextContent();
                String city = element.getElementsByTagName("city").item(0).getTextContent();

                System.out.println(name + "/" + age + "/" + city);
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }


    }
}
