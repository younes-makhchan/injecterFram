package ioc.config;

import ioc.config.configurations.CustomAnnotationsConfiguration;
import ioc.config.configurations.InstantiationConfiguration;

public class InjectorConfiguration {
    private final CustomAnnotationsConfiguration annotations;
    private  final InstantiationConfiguration instantiations;
    public  InjectorConfiguration(){
        this.annotations=new CustomAnnotationsConfiguration(this);
        this.instantiations=new InstantiationConfiguration(this);
    }
    public CustomAnnotationsConfiguration annotations(){
        return  this.annotations;
    }
    public InstantiationConfiguration instantiations(){
return this.instantiations;
    }
    public  InjectorConfiguration build(){
        return  this;
    }
}
