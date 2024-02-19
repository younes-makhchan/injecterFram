package ioc.models;

import ioc.annotations.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//this one is the scanner for those annotation
public class ServiceDetails<T> {
    private Class<T> serviceType;
    private Annotation annotation;
    private Constructor<T> targetConstructor;
    private  T instance;
    private Method postConstructMethod;
    private Method preDestroyMethod;
    private Method[] beans;
    private  final List<ServiceDetails<?>> dependantServices;
    public  ServiceDetails(){
        this.dependantServices=new ArrayList<>();
    }

    public ServiceDetails(Class<T> serviceType, Annotation annotation, Constructor<T> targetConstructor, Method postConstructMethod, Method preDestroyMethod, Method[] beans) {
       this();
        this.serviceType = serviceType;
        this.annotation = annotation;
        this.targetConstructor = targetConstructor;
        this.postConstructMethod = postConstructMethod;
        this.preDestroyMethod = preDestroyMethod;
        this.beans = beans;
    }

    public Class<T> getServiceType() {
        return serviceType;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Constructor<T> getTargetConstructor() {
        return targetConstructor;
    }

    public T getInstance() {
        return instance;
    }

    public Method getPostConstructMethod() {
        return postConstructMethod;
    }

    public Method getPreDestroyMethod() {
        return preDestroyMethod;
    }

    public Method[] getBeans() {
        return beans;
    }

    public void setServiceType(Class<T> serviceType) {
        this.serviceType = serviceType;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public void setTargetConstructor(Constructor<T> targetConstructor) {
        this.targetConstructor = targetConstructor;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public void setPostConstructMethod(Method postConstructMethod) {
        this.postConstructMethod = postConstructMethod;
    }

    public void setPreDestroyMethod(Method preDestroyMethod) {
        this.preDestroyMethod = preDestroyMethod;
    }

    public void setBeans(Method[] beans) {
        this.beans = beans;
    }
    public  List<ServiceDetails<?>> getDependantServices(){
        return Collections.unmodifiableList(this.dependantServices);
    }
    public void addDependantService(ServiceDetails<?> serviceDetails){
        this.dependantServices.add(serviceDetails);
    }
    public int hashCode(){
        if(this.serviceType==null){
            return super.hashCode();
        }
        return this.serviceType.hashCode();
    }
}
