package ioc.test;

import ioc.annotations.Service;

@Service
public class TestServiceOne {


    public  TestServiceOne(){
        System.out.println("Createing Service one ");

    }
    public  TestServiceOne(int age,String name){
        System.out.println("Createing Service one ");
    }
}
