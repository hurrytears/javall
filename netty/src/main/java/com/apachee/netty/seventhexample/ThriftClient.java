package com.apachee.netty.seventhexample;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClient {
    public static void main(String[] args) {
        TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        TProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);

        try{
            transport.open();
            Person person = client.getPersonByUsername("张三");
            System.out.println(person.username);
            System.out.println(person.age);
            System.out.println(person.married);

            System.out.println("________________________________");

            Person person2 = new Person();
            person2.setUsername("李四");
            person2.setAge(14);
            person2.setMarried(true);

            client.savePerson(person2);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            transport.close();
        }
    }
}
