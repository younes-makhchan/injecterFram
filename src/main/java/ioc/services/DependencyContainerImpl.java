package ioc.services;

import ioc.exceptions.AlreadyInitializedException;
import ioc.models.ServiceBeanDetails;
import ioc.models.ServiceDetails;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class DependencyContainerImpl implements DependencyContainer {
    private  boolean isInit;
    private  final  String ALREADY_INITED="Dependancy container already started";
    private  List<ServiceDetails<?>> servicesAndBeans;
    private  ObjectInstantiationService instantiationService;
    public DependencyContainerImpl(){
        this.isInit=false;
    }
    public void init(List<ServiceDetails<?>> servicesAndBeans, ObjectInstantiationService objectInstantiationService) throws AlreadyInitializedException {
        if(this.isInit){
            throw  new AlreadyInitializedException(ALREADY_INITED);
        }
        this.servicesAndBeans=servicesAndBeans;
        this.instantiationService=objectInstantiationService;
    }

    @Override
    public <T> void reload(ServiceDetails<T> serviceDetails, boolean reloadDependantServices) {
        this.instantiationService.destroyInstance(serviceDetails);
        this.handleReload(serviceDetails);
        if(reloadDependantServices){
            for (ServiceDetails<?> dependantService : serviceDetails.getDependantServices()) {
                this.reload(dependantService,reloadDependantServices);
            }
        }
    }
    private  void handleReload(ServiceDetails<?> serviceDetails){
        if(serviceDetails instanceof ServiceBeanDetails){
            this.instantiationService.createBeanInstance((ServiceBeanDetails<?> )serviceDetails);
        }else{
            this.instantiationService.createInstance(serviceDetails,this.collectDependecies(serviceDetails));
        }
    }
    private  Object[] collectDependecies(ServiceDetails<?> serviceDetails){
        Class<?>[] parameterType=serviceDetails.getTargetConstructor().getParameterTypes();
        Object[] dependencyInstances=new Object[parameterType.length];
        for (int i = 0; i < parameterType.length; i++) {
            dependencyInstances[i]=this.getService(parameterType[i]);
        }
        return  dependencyInstances;
    }

    @Override
    public <T> T reload(T service) {
        return this.reload(service,false);
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T> T reload(T service, boolean reloadDependantServices) {
        ServiceDetails<T> serviceDetails =(ServiceDetails<T>) this.getServiceDetails(service.getClass());
        if(serviceDetails==null)return null;
        this.reload(serviceDetails,reloadDependantServices);
        return  serviceDetails.getInstance();
    }

    @Override
    public <T> T getService(Class<T> serviceType) {
        ServiceDetails<T> serviceDetails=this.getServiceDetails(serviceType);
        if(serviceDetails!=null){
            return  serviceDetails.getInstance();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ServiceDetails<T> getServiceDetails(Class<?> serviceType) {
        return (ServiceDetails<T>) this.servicesAndBeans.stream()
                .filter(sd->serviceType.isAssignableFrom(sd.getServiceType()))
                .findFirst().orElse(null);
    }

    @Override
    public List<ServiceDetails<?>> getServiceByAnnotation(Class<? extends Annotation> annotationType) {
        return this.servicesAndBeans.stream()
                .filter(sd->sd.getAnnotation().annotationType()==annotationType)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> getAllServices() {
        return null;
    }

    @Override
    public List<ServiceDetails<?>> getAllServiceDetails() {
        return null;
    }
}
