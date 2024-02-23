package ioc.services;

import ioc.annotations.Service;
import ioc.config.configurations.InstantiationConfiguration;
import ioc.exceptions.ServiceInstantiationException;
import ioc.models.EnqueuedServiceDetails;
import ioc.models.ServiceBeanDetails;
import ioc.models.ServiceDetails;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ServicesInstantiationServiceImpl implements ServicesInstantiationService {
    private  final InstantiationConfiguration configuration;
    private  final  ObjectInstantiationService instantiationService;
    private  final LinkedList<EnqueuedServiceDetails> enqueuedServiceDetails;

    private  final  List<Class<?>> allAvailableClasses;
    private final List<ServiceDetails<?>> instantiatedServices;
    private  final  String MAX_NUMBER_OF_ALLOWED_ITTERATION_REACHED="Maximum number of allowed iteration was reached '%s' ";
    public ServicesInstantiationServiceImpl(InstantiationConfiguration configuration, ObjectInstantiationService instantiationService ) {
        this.configuration = configuration;
        this.instantiationService = instantiationService;
        this.enqueuedServiceDetails = new LinkedList<>();
        this.allAvailableClasses = new ArrayList<>();
        this.instantiatedServices=new ArrayList<>();
    }

    @Override
    public List<ServiceDetails<?>> instantiateServicesAndBeans(Set<ServiceDetails<?>> mappedServices) throws ServiceInstantiationException {
        this.init(mappedServices);
        this.checkForMissingServices(mappedServices);
        int counter=0;
        int maxNumberOfIteration=this.configuration.getMaximumNumberOfiterations();
        while(!this.enqueuedServiceDetails.isEmpty()){
            if(counter>this.configuration.getMaximumNumberOfiterations()){
                throw  new ServiceInstantiationException(String.format(MAX_NUMBER_OF_ALLOWED_ITTERATION_REACHED));
            }
            EnqueuedServiceDetails enqueuedServiceDetails1=this.enqueuedServiceDetails.removeFirst();
            if(enqueuedServiceDetails1.isResolved()){
                ServiceDetails<?> serviceDetails=enqueuedServiceDetails1.getServiceDetails();
                Object[] dependencyInstances=enqueuedServiceDetails1.getDependencyInstances();
                this.instantiationService.createInstance(
                        serviceDetails,
                       dependencyInstances
                );
                this.registerInstantiatedService(serviceDetails);
                this.registerBeans(serviceDetails);
            }else{
                this.enqueuedServiceDetails.addLast(enqueuedServiceDetails1);
                counter++;
            }
        }
        return this.instantiatedServices;
    }

    private void registerBeans(ServiceDetails<?> serviceDetails) {
        for(Method beanMethod:serviceDetails.getBeans()){
            ServiceBeanDetails<?> beanDetails =new ServiceBeanDetails<>(beanMethod.getReturnType(),beanMethod,serviceDetails);
            this.instantiationService.createBeanInstance(beanDetails);
            this.registerInstantiatedService(beanDetails);
        }
     }

    private void registerInstantiatedService(ServiceDetails<?> serviceDetails) {

        if(!(serviceDetails instanceof ServiceBeanDetails)){

        }
        this.instantiatedServices.add(serviceDetails);

        for (EnqueuedServiceDetails enqueuedServiceDetail : this.enqueuedServiceDetails) {
            if (enqueuedServiceDetail.isDependencyRequired(serviceDetails.getServiceType())){
                enqueuedServiceDetail.addDependencyInstance(serviceDetails.getInstance());
            }
        }
    }
    private  void updateDependantServices(ServiceDetails<?> newService){
        for (Class<?> parameterType : newService.getTargetConstructor().getParameterTypes()) {
            for (ServiceDetails<?> instantiatedService : this.instantiatedServices) {
                if (parameterType.isAssignableFrom(instantiatedService.getServiceType())){
                    instantiatedService.addDependantService(newService);
                }
            }
        }
    }
    private  void init(Set<ServiceDetails<?>> mappedServices){
        this.enqueuedServiceDetails.clear();
        this.allAvailableClasses.clear();
        this.instantiatedServices.clear();
        for(ServiceDetails<?> serviceDetails:mappedServices){
            this.enqueuedServiceDetails.add(new EnqueuedServiceDetails((serviceDetails)));
            this.allAvailableClasses.add(serviceDetails.getServiceType());
            this.allAvailableClasses.addAll(Arrays.stream(serviceDetails.getBeans()).map(Method::getReturnType).toList());

        }
    }
    private  void checkForMissingServices(Set<ServiceDetails<?>> mappedService) throws ServiceInstantiationException{
        for (ServiceDetails<?> serviceDetails : mappedService) {
            for (Class<?> parameterType : serviceDetails.getTargetConstructor().getParameterTypes()) {
                    if(!this.isAssignableTypePresent(parameterType)){
                        throw  new ServiceInstantiationException(String.format("Could not create  instance of '%s'. Parameter '%s' implementation was not found ",serviceDetails.getServiceType().getName(),parameterType.getName()));
                    }
            }
        }
    }
    private  boolean isAssignableTypePresent(Class<?> cls){
        for (Class<?> allAvailableClass : this.allAvailableClasses) {
            if(cls.isAssignableFrom(allAvailableClass)){
                return  true;
            }
        }
        return  false;
    }

}
