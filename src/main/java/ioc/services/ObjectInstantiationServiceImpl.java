package ioc.services;

import ioc.exceptions.BeanInstantiationException;
import ioc.exceptions.PostConstructInstantiationException;
import ioc.exceptions.ServiceInstantiationException;
import ioc.exceptions.PreDestroyExecutionException;
import ioc.models.ServiceBeanDetails;
import ioc.models.ServiceDetails;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectInstantiationServiceImpl implements ObjectInstantiationService {
    private  static  final String INVALID_PARAMETERS_COUNT_MSG="Invalid parameters count for '%'.";
    @Override
    public void createInstance(ServiceDetails<?> serviceDetails, Object... constructorParams) throws ServiceInstantiationException {
        Constructor<?> targetConstructor = serviceDetails.getTargetConstructor();
        if(constructorParams.length!=targetConstructor.getParameterCount()){
            throw  new ServiceInstantiationException(String.format(INVALID_PARAMETERS_COUNT_MSG,serviceDetails.getServiceType().getName()));
        }
        try {
            Object instance= targetConstructor.newInstance(constructorParams);
            serviceDetails.setInstance(instance);
            this.callPostConstruct(serviceDetails);
        } catch (IllegalAccessException|InvocationTargetException e){
                throw new ServiceInstantiationException(e.getMessage(),e);
        }catch (InstantiationException  e) {
              e.printStackTrace();
        }
    }
    private void callPostConstruct(ServiceDetails<?> serviceDetails) {
        if(serviceDetails.getPostConstructMethod()==null){
        return;
        }
        try {
            serviceDetails.getPostConstructMethod().invoke(serviceDetails.getInstance());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PostConstructInstantiationException(e.getMessage(),e);
        }
    }
    @Override
    public void createBeanInstance(ServiceBeanDetails<?> serviceBeanDetails) throws BeanInstantiationException {
        Method originMethod=serviceBeanDetails.getOriginMethod();
        Object rootInstance= serviceBeanDetails.getRootService().getInstance();
        try{
            Object instance = originMethod.invoke(rootInstance);
            serviceBeanDetails.setInstance(instance);
        }catch (IllegalAccessException|InvocationTargetException e){
            throw  new BeanInstantiationException(e.getMessage(),e);
        }

    }

    @Override
    public void destroyInstance(ServiceDetails<?> serviceDetails) throws PreDestroyExecutionException {
        if(serviceDetails.getPreDestroyMethod()!=null){
            try {
                serviceDetails.getPreDestroyMethod().invoke (serviceDetails.getInstance());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PreDestroyExecutionException(e.getMessage(),e);
            }
        }
        serviceDetails.setInstance(null);
    }
}
