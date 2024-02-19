package ioc.config;

public abstract class BaseSubConfiguration {
    private  final  InjectorConfiguration parentConfig;
    protected BaseSubConfiguration (InjectorConfiguration parentConfig){
        this.parentConfig=parentConfig;
    }
    public InjectorConfiguration and(){
        return this.parentConfig;
    }
}
