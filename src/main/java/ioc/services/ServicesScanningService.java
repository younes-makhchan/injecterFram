package ioc.services;

import ioc.models.ServiceDetails;

import java.util.Set;

public interface ServicesScanningService {
    Set<ServiceDetails<?>> mapServices(Set<Class<?>> locatedClasses);
}
