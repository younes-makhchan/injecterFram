package ioc.config;

import ioc.config.configurations.CustomAnnotationsConfiguration;

public class InjectorConfiguration {
    private final CustomAnnotationsConfiguration annotations;
    public  InjectorConfiguration(){
        this.annotations=new CustomAnnotationsConfiguration(this);
    }
    public CustomAnnotationsConfiguration annotations(){
        return  this.annotations;
    }
    public  InjectorConfiguration build(){
        return  this;
    }
}
