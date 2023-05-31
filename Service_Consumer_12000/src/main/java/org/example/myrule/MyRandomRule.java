package org.example.myrule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
@Configuration
public class MyRandomRule {
    public MyRandomRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
    }
    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        //返回随机轮询负载均衡方式
        return new RandomLoadBalancer(loadBalancerClientFactory.
                getLazyProvider(name, ServiceInstanceListSupplier.class),
                name);

    }
}
