package ioc.services;

import ioc.exceptions.ClassLocationException;
import ioc.models.Directory;

import java.util.Set;

public interface ClassLocater {
    Set<Class<?>> locateClasses(String directory) throws ClassLocationException;
}
