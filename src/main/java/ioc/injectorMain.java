package ioc;

import ioc.annotations.Service;
import ioc.config.InjectorConfiguration;
import ioc.enums.DirectoryType;
import ioc.models.Directory;
import ioc.models.ServiceDetails;
import ioc.services.*;
import ioc.test.TestServiceOne;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
@Service
public class injectorMain {
    public static  final  DependencyContainer DEPENDENCY_CONTAINER;
    static {
        DEPENDENCY_CONTAINER=new DependencyContainerImpl();
    }
    public static void main(String[] args) {
        run(injectorMain.class);
    }
    public  static  void run(Class<?> startupClass){

    run(startupClass,new InjectorConfiguration());
    }

    @StartUp
    public  void appStart(){
        System.out.println("last called");
        DEPENDENCY_CONTAINER.reload(DEPENDENCY_CONTAINER.getService(TestServiceOne.class),true);
    }
    public  static  void  run(Class<?> startupClass,InjectorConfiguration configuration){
        ServicesScanningService servicesScanningService=new ServicesScanningServiceImpl(configuration.annotations());
        ObjectInstantiationService objectInstantiationService = new ObjectInstantiationServiceImpl();
        ServicesInstantiationService instantiationService= new ServicesInstantiationServiceImpl(
                configuration.instantiations(),
                objectInstantiationService
        );

        Directory directory=new DirectoryResolverImpl().resolveDirectory(startupClass);

        ClassLocater classLocater=new ClasssLocatorForDirectory();
        if(directory.getDirectoryType()== DirectoryType.JAR_FILE){
            classLocater=new ClassLocatorForJARFile();
        }
        Set<Class<?>> locateClasses=classLocater.locateClasses(directory.getDirectory());
        Set<ServiceDetails<?>> mappedServices=servicesScanningService.mapServices(locateClasses);
        List<ServiceDetails<?>> serviceDetails=instantiationService.instantiateServicesAndBeans(mappedServices);
        DEPENDENCY_CONTAINER.init(serviceDetails,objectInstantiationService);
        runStartUpMethod(startupClass);
    }
    private  static void  runStartUpMethod(Class<?> startupClass){

        ServiceDetails<?> serviceDetails = DEPENDENCY_CONTAINER.getServiceDetails(startupClass);
        for (Method declaredMethod : serviceDetails.getServiceType().getDeclaredMethods()) {
            if(declaredMethod.getParameterCount()!=0||
                    (declaredMethod.getReturnType()!=void.class&&
                            declaredMethod.getReturnType()!=Void.class)
            ||!declaredMethod.isAnnotationPresent(StartUp.class)){
                continue;
            }
            declaredMethod.setAccessible(true);
            try{
                declaredMethod.invoke(serviceDetails.getInstance());

            }catch (IllegalAccessException| InvocationTargetException e){
                throw new RuntimeException(e);
            }
        }
    }
}
