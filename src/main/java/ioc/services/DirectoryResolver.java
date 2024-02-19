package ioc.services;

import ioc.models.Directory;

public interface DirectoryResolver {

    Directory resolveDirectory(Class<?> startupCLass);
}
