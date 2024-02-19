package ioc.utils;

import ioc.models.ServiceDetails;

import java.util.Comparator;

public class ServiceDetailsConstructComparator implements Comparator<ServiceDetails> {
    public  int compare(ServiceDetails serviceDetails1,ServiceDetails serviceDetails2){
        return  Integer.compare(
                serviceDetails1.getTargetConstructor().getParameterCount(),
                serviceDetails2.getTargetConstructor().getParameterCount()
        );
    }
}
