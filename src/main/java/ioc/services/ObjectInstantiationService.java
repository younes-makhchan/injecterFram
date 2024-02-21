package ioc.services;

import ioc.exceptions.BeanInstantiationException;
import ioc.exceptions.ServiceInstantiationException;
import ioc.exceptions.PreDestroyExecutionException;
import ioc.models.ServiceBeanDetails;
import ioc.models.ServiceDetails;

public interface ObjectInstantiationService {
    void createInstance(ServiceDetails<?> serviceDetails,Object... constructorParams)throws ServiceInstantiationException;
    void createBeanInstance(ServiceBeanDetails<?> serviceBeanDetails) throws BeanInstantiationException;
    void destroyInstance(ServiceDetails<?> serviceDetails) throws PreDestroyExecutionException;

}
