package ioc.config.configurations;

import ioc.config.BaseSubConfiguration;
import ioc.config.InjectorConfiguration;
import ioc.constants.Constants;

public class InstantiationConfiguration extends BaseSubConfiguration {
    private  int maximumNumberOfiterations;
    protected InstantiationConfiguration(InjectorConfiguration parentConfig,) {
        super(parentConfig);
        this.maximumNumberOfiterations= Constants.MAX_NUMBER_OF_INSTANTIATIONS
    }

    public  InstantiationConfiguration setMaximumNumberofAllowedIteration(int num){
        this.maximumNumberOfiterations=num;
        return  this;

    }
    public  int getMaximumNumberOfiterations(){
        return  this.maximumNumberOfiterations;
    }
}
