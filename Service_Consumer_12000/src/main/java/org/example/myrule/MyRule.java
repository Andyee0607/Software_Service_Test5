package org.example.myrule;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//注入自定义负载均衡规则
@Configuration
public class MyRule {

    // 参数 serviceInstanceListSupplierProvider 会自动注入
    @Bean
    public ReactorServiceInstanceLoadBalancer customLoadBalancer
    (ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        return (ReactorServiceInstanceLoadBalancer) new MyRandomRule(serviceInstanceListSupplierProvider);
    }
}
