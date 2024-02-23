Goal Of the Project
---------------------------------
Create mini framework similar to Spring IOC with Options :
* XML configuration file
* Annotations
* injection of constructors, attributes,setters

Features
-----------------------
* Create instance of your services
* Automatically resolve dependencies
* Create Beans
* Add your own custom mapping annotations for services and beans
* Manage instantiated services (get them by annotation, qualifier, type)
* Reload instantiated services
* Handle custom service and bean scopes by using proxies.
* Enrich your service with aspects
  
Run the app
------------------
* Run 'mvn package' and get the generated jar file.
* Import it into your project
* In your main method call 'InjectorFram.run(StartupClass.class);'
* Annotate your startup class with @Service
* Create a void method and annotate it with @StartUp

You can also run the app with 'InjectorFram.run(StartupClass.class, new InjectorConfiguration());'.

Supported annotations by default: 
--------------------------------------------------------------------------------

* Bean - Specify bean producing method.
* Service - Specify service.
* Autowired - Specify which constructor will be used to create instance of a service.
also you can annotate fields with this annotation.
* PostConstruct - Specify a method that will be executed after the service has been created.
* PreDestroy - Specify a method that will be executed just before the service has been disposed.
* StartUp - Specify the startup method for the app.
