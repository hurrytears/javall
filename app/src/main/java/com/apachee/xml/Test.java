package com.apachee.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

public class Test {

    public static void main(String[] args) throws DocumentException {
        Test test = new Test();
        SAXReader reader = new SAXReader();
        //2.加载xml
        File f = new File("C:\\Users\\sosog\\Desktop\\StationTransform\\SourceDir\\dump.xml");
        System.out.println(f.getPath());
        Document document = reader.read(f);
        Element rootElement = document.getRootElement();
//        for(Object o: rootElement.elements()){
//            Element element = (Element) o;
//            System.out.println("::::" + element.attributeValue("rollno"));
//            for(Object level2 :element.elements()){
//                Element e = (Element) level2;
//                System.out.println(e.getStringValue());
//            }
//        }
    }
}
