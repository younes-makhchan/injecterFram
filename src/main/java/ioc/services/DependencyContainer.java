package ioc.services;

import ioc.exceptions.AlreadyInitializedException;
import ioc.models.ServiceDetails;

import java.lang.annotation.Annotation;
import java.util.List;

public interface DependencyContainer {
    void init(List<ServiceDetails<?>> servicesAndBeans,ObjectInstantiationService objectInstantiationService) throws AlreadyInitializedException;
    <T> void reload(ServiceDetails<T> serviceDetails,boolean reloadDependantServices);
    <T> T reload(T service);
    <T> T reload(T service , boolean reloadDependantServices);
    <T> T getService(Class<T>  serviceType);
    <T> ServiceDetails<T> getServiceDetails(Class<?> serviceType);

    List<ServiceDetails<?>> getServiceByAnnotation(Class<? extends Annotation> annotationType);
    List<Object> getAllServices();
    List<ServiceDetails<?>> getAllServiceDetails();


}
