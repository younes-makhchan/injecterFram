package ioc;

import ioc.config.InjectorConfiguration;
import ioc.enums.DirectoryType;
import ioc.models.Directory;
import ioc.models.ServiceDetails;
import ioc.services.*;

import java.util.Set;

public class injectorMain {
    public static void main(String[] args) {
        run(injectorMain.class);
    }
    public  static  void run(Class<?> startupClass){

    run(startupClass,new InjectorConfiguration());
    }
    public  static  void  run(Class<?> startupClass,InjectorConfiguration configuration){
        ServicesScanningService servicesScanningService=new ServicesScanningServiceImpl(configuration.annotations());
        Directory directory=new DirectoryResolverImpl().resolveDirectory(startupClass);

        ClassLocater classLocater=new ClasssLocatorForDirectory();
        if(directory.getDirectoryType()== DirectoryType.JAR_FILE){
            classLocater=new ClassLocatorForJARFile();
        }
        Set<Class<?>> locateClasses=classLocater.locateClasses(directory.getDirectory());
        System.out.println(locateClasses);
        Set<ServiceDetails<?>> serviceDetails=servicesScanningService.mapServices(locateClasses);
    }
}
