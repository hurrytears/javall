package com.apachee.xml;


import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.awt.print.Book;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JDomDemo {
    public static void main(String[] args) throws Exception {
        //1.创建SAXBuilder对象
        SAXBuilder saxBuilder = new SAXBuilder();
        //2.创建输入流
        InputStream is = new FileInputStream(new File("src/main/resources/demo.xml"));
        //3.将输入流加载到build中
        Document document = saxBuilder.build(is);
        //4.获取根节点
        Element rootElement = document.getRootElement();
        //5.获取子节点
        List<Element> children = rootElement.getChildren();
        for (Element child : children) {
            System.out.println("通过rollno获取属性值:" + child.getAttribute("rollno"));
            List<Attribute> attributes = child.getAttributes();
            //打印属性
            for (Attribute attr : attributes) {
                System.out.println(attr.getName() + ":" + attr.getValue());
            }
            List<Element> childrenList = child.getChildren();
            System.out.println("======获取子节点-start======");
            for (Element o : childrenList) {
                System.out.println("节点名:" + o.getName() + "---" + "节点值:" + o.getValue());
            }
            System.out.println("======获取子节点-end======");
        }
    }

    public static void createXml() {
        try {
            // 1、生成一个根节点
            Element rss = new Element("rss");
            // 2、为节点添加属性
            rss.setAttribute("version", "2.0");
            // 3、生成一个document对象
            Document document = new Document(rss);

            Element channel = new Element("channel");
            rss.addContent(channel);
            Element title = new Element("title");
            title.setText("国内最新新闻");
            channel.addContent(title);

            Format format = Format.getCompactFormat();
            // 设置换行Tab或空格
            format.setIndent("	");
            format.setEncoding("UTF-8");

            // 4、创建XMLOutputter的对象
            XMLOutputter outputer = new XMLOutputter(format);
            // 5、利用outputer将document转换成xml文档
            File file = new File("rssNew.xml");
            outputer.output(document, new FileOutputStream(file));

            System.out.println("生成rssNew.xml成功");
        } catch (Exception e) {
            System.out.println("生成rssNew.xml失败");
        }
    }

    @Test
    public void test() {
        List<Book> bList = new ArrayList<Book>();
        Book b = new Book();
        b.setName("冰与火之歌");
        b.setAuthor("乔治马丁");
        b.setId("1");
        b.setLanguage("English");
        b.setPrice("86");
        b.setYear("2014");
        bList.add(b);
        Long start = System.currentTimeMillis();
        createXml(bList);
        System.out.println("运行时间：" + (System.currentTimeMillis() - start));
    }

    // 生成xml
    public static void createXml(List<Book> bList) {
        // 1、创建一个SAXTransformerFactory类的对象
        SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        try {
            // 2、通过SAXTransformerFactory创建一个TransformerHandler的对象
            TransformerHandler handler = tff.newTransformerHandler();
            // 3、通过handler创建一个Transformer对象
            Transformer tr = handler.getTransformer();
            // 4、通过Transformer对象对生成的xml文件进行设置
            // 设置编码方式
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // 设置是否换行
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            // 5、创建一个Result对象
            File f = new File("src/newbooks.xml");
            // 判断文件是否存在
            if (!f.exists()) {
                f.createNewFile();
            }
            Result result = new StreamResult(new FileOutputStream(f));
            // 6、使RESULT与handler关联
            handler.setResult(result);

            // 打开document
            handler.startDocument();
            AttributesImpl attr = new AttributesImpl();
            handler.startElement("", "", "bookstore", attr);
            attr.clear();

            for (Book book : bList) {
                attr.clear();
                attr.addAttribute("", "", "id", "", book.getId());
                handler.startElement("", "", "book", attr);

                // 创建name
                attr.clear();
                handler.startElement("", "", "name", attr);
                handler.characters(book.getName().toCharArray(), 0, book.getName().length());
                handler.endElement("", "", "name");

                // 创建year
                attr.clear();
                handler.startElement("", "", "year", attr);
                handler.characters(book.getYear().toCharArray(), 0, book.getYear().length());
                handler.endElement("", "", "year");

                // 创建author
                if (book.getAuthor() != null && !"".equals(book.getAuthor().trim())) {
                    attr.clear();
                    handler.startElement("", "", "author", attr);
                    handler.characters(book.getAuthor().toCharArray(), 0, book.getAuthor().length());
                    handler.endElement("", "", "author");
                }

                // 创建price
                if (book.getPrice() != null && !"".equals(book.getPrice().trim())) {
                    attr.clear();
                    handler.startElement("", "", "price", attr);
                    handler.characters(book.getPrice().toCharArray(), 0, book.getPrice().length());
                    handler.endElement("", "", "price");
                }

                // 创建language
                if (book.getLanguage() != null && !"".equals(book.getLanguage().trim())) {
                    attr.clear();
                    handler.startElement("", "", "language", attr);
                    handler.characters(book.getLanguage().toCharArray(), 0, book.getLanguage().length());
                    handler.endElement("", "", "language");
                }

                handler.endElement("", "", "book");
            }

            handler.endElement("", "", "bookstore");
            // 关闭document
            handler.endDocument();
            System.out.println("生成newbooks.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成newbooks.xml失败");
        }
    }


    class Book {
        private String id, name, price, language, year, author;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

}
