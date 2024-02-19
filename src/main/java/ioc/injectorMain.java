package ioc;

import ioc.config.InjectorConfiguration;
import ioc.enums.DirectoryType;
import ioc.models.Directory;
import ioc.services.ClassLocater;
import ioc.services.ClassLocatorForJARFile;
import ioc.services.ClasssLocatorForDirectory;
import ioc.services.DirectoryResolverImpl;

import java.util.Set;

public class injectorMain {
    public static void main(String[] args) {
        run(injectorMain.class);
    }
    public  static  void run(Class<?> startupClass){

    run(startupClass,new InjectorConfiguration());
    }
    public  static  void  run(Class<?> startupClass,InjectorConfiguration configuration){
        Directory directory=new DirectoryResolverImpl().resolveDirectory(startupClass);

        System.out.println("Dir type :"+directory.getDirectoryType());
        ClassLocater classLocater=new ClasssLocatorForDirectory();
        if(directory.getDirectoryType()== DirectoryType.JAR_FILE){
            classLocater=new ClassLocatorForJARFile();
        }
        Set<Class<?>> classes=classLocater.locateClasses(directory.getDirectory());
        System.out.println(classes);
    }
}
