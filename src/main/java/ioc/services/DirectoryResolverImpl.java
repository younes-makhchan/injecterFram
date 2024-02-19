package ioc.services;

import ioc.enums.DirectoryType;
import ioc.models.Directory;

import java.io.File;

public class DirectoryResolverImpl implements DirectoryResolver {
    private  static final String JAR_FILE_EXTENSION=".jar";
    @Override
    public Directory resolveDirectory(Class<?> startupCLass) {
        final  String directory=this.getDirectory(startupCLass);
        return  new Directory(directory,this.getDirectoryType(directory));
    }
    public String getDirectory(Class<?> cls){
        return  cls.getProtectionDomain().getCodeSource().getLocation().getFile();
    }
    public DirectoryType getDirectoryType(String directory){
        File file=new File(directory);
        if(!file.isDirectory()&& directory.endsWith(JAR_FILE_EXTENSION)){
            return DirectoryType.JAR_FILE;
        }
        return  DirectoryType.DIRECTORY;
    }
}
