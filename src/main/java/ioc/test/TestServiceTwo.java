package ioc.test;

import ioc.annotations.*;

@Service
public class TestServiceTwo {
    private final TestServiceOne serviceOne;

    @AutoWired
    public TestServiceTwo(TestServiceOne serviceOne){
        this.serviceOne=serviceOne;
    }
    @PostConstruct
    private void onInit(){

    }
    @PreDestroy
    public void  onDestroy(){

    }
    @Bean
    public OtherService otherService(){
        return new OtherService();
    }
}
