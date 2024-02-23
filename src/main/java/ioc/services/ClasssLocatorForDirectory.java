package ioc.services;

import ioc.constants.Constants;
import ioc.exceptions.ClassLocationException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ClasssLocatorForDirectory implements ClassLocater{
    private  static final String INVALID_DIRECTORY_MSG ="Invalid directory '%s '.";
    private final Set<Class<?>> locatedClasses;
    public ClasssLocatorForDirectory(){
        this.locatedClasses=new HashSet<>();
    }
    @Override
    public Set<Class<?>> locateClasses(String directory) throws ClassLocationException {
        File file=new  File(directory);
        //check if it's a directory
        if(!file.isDirectory()){
            throw new ClassLocationException(String.format(INVALID_DIRECTORY_MSG,directory));
        }
        for(File innerFile:file.listFiles()){
            this.scanDir(innerFile,"");

        }
        return this.locatedClasses;
    }
    private void scanDir(File file, String packageName)  {
        if(file.isDirectory()){
            packageName+=file.getName()+".";
            for(File innerFile:file.listFiles()){
                this.scanDir(innerFile,packageName);
            }
        }else{
            if(!file.getName().endsWith(Constants.JAVA_BINARY_EXTENSION)){
                return;
            }
            final String className=packageName+file.getName().replace(Constants.JAVA_BINARY_EXTENSION,"");
            try{
                this.locatedClasses.add(Class.forName(className));

            }catch (ClassNotFoundException e){
                throw new ClassLocationException(e.getMessage(),e);
            }

        }
    }
}
