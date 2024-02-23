package ioc.test;

import ioc.annotations.AutoWired;
import ioc.annotations.Bean;
import ioc.annotations.PostConstruct;
import ioc.annotations.Service;

@Service
public class TestServiceOne {

    public  TestServiceOne(){
    }
    public  TestServiceOne(int age,String name){

        System.out.println("Createing Service one ");
    }
}
