package ioc.services;

import ioc.annotations.*;
import ioc.config.configurations.CustomAnnotationsConfiguration;
import ioc.models.ServiceDetails;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ServicesScanningServiceImpl implements ServicesScanningService {
    private  final CustomAnnotationsConfiguration configuration;

    public ServicesScanningServiceImpl(CustomAnnotationsConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Set<ServiceDetails<?>> mapServices(Set<Class<?>> locatedClasses) {
        final Set<ServiceDetails<?>> serviceDetailsStorage=new HashSet<>();
        final Set<Class<? extends Annotation>> customServiceAnnotations=configuration.getCustomServiceAnnotations();
        customServiceAnnotations.add(Service.class);

        for(Class<?> cls:locatedClasses){
            if(cls.isInterface()){
                continue;
            }
            for (Annotation annotation : cls.getAnnotations()) {

                if(customServiceAnnotations.contains(annotation.annotationType())){
                    ServiceDetails<?> serviceDetails=new ServiceDetails(cls,
                            annotation,
                            this.findSuitableConstructure(cls),
                            this.findVoidMethodWithZeroParamsAndAnnotations(PostConstruct.class,cls),
                            this.findVoidMethodWithZeroParamsAndAnnotations(PreDestroy.class,cls),
                            this.findBeans(cls));

                    serviceDetailsStorage.add(serviceDetails);
                }
            }

        }
        return serviceDetailsStorage;
    }
    private Constructor<?> findSuitableConstructure(Class<?> cls) {
        for (Constructor<?> ctr : cls.getDeclaredConstructors()) {
            if(ctr.isAnnotationPresent(AutoWired.class)){
                ctr.setAccessible(true);
                return  ctr;
            }
        }
        return  cls.getConstructors()[0];


    }
    private Method findVoidMethodWithZeroParamsAndAnnotations(Class<?extends Annotation> annotation,Class<?>cls){
        for (Method declaredMethod : cls.getDeclaredMethods()) {
            if(declaredMethod.getParameterCount()!=0 ||
                    (declaredMethod.getReturnType()!=void.class && declaredMethod.getReturnType()!=Void.class)||!declaredMethod.isAnnotationPresent(annotation)
            ){
                continue;

            }
            declaredMethod.setAccessible(true);
            return  declaredMethod;

        }
        return  null;
    }
    private Method[] findBeans(Class<?> cls){
        //get customs annotations first
        final Set<Class<? extends Annotation>> beanAnnotations=this.configuration.getCustomBeanAnnotations();
        final Set<Method> beanMethods=new HashSet<>();
        for (Method declaredMethod : cls.getDeclaredMethods()) {
            if(declaredMethod.getParameterCount()!=0 ||declaredMethod.getReturnType()==void.class||declaredMethod.getReturnType()==Void.class){
                continue;
            }
            for (Class<? extends Annotation> beanAnnotation : beanAnnotations) {
                if(declaredMethod.isAnnotationPresent(beanAnnotation)){
                    beanMethods.add(declaredMethod);
                }

            }
        }
        return  beanMethods.toArray(Method[]::new);
    }

}
