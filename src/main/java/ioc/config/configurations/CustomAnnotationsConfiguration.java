package ioc.config.configurations;

import ioc.config.BaseSubConfiguration;
import ioc.config.InjectorConfiguration;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomAnnotationsConfiguration extends BaseSubConfiguration {
    private final Set<Class<? extends Annotation>> customServiceAnnotations;
    private final Set<Class<? extends Annotation>> customBeanAnnotations;
    public CustomAnnotationsConfiguration(InjectorConfiguration parentConfig){
        super(parentConfig);
        this.customBeanAnnotations=new HashSet<>();
        this.customServiceAnnotations=new HashSet<>();
    }
    public  CustomAnnotationsConfiguration addCustomServiceAnnotation(Class<? extends  Annotation> annotation){
        this.customServiceAnnotations.add(annotation);
        return  this;
    }
    public   CustomAnnotationsConfiguration addCustomBeanAnnotation(Class<? extends  Annotation> annotation){
        this.customServiceAnnotations.add(annotation);
        return  this;
    }
    public  CustomAnnotationsConfiguration addCustomServiceAnnotations(Class<? extends  Annotation> ...annotations){
        this.customServiceAnnotations.addAll(Arrays.asList(annotations));
        return  this;
    }
    public   CustomAnnotationsConfiguration addCustomBeanAnnotations(Class<? extends  Annotation> ...annotations){
        this.customServiceAnnotations.addAll(Arrays.asList(annotations));
        return  this;
    }

    public Set<Class<? extends Annotation>> getCustomServiceAnnotations() {
        return customServiceAnnotations;
    }

    public Set<Class<? extends Annotation>> getCustomBeanAnnotations() {
        return customBeanAnnotations;
    }
}
