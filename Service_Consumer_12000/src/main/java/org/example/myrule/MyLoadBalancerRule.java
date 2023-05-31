package org.example.myrule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
public class MyLoadBalancerRule implements ReactorServiceInstanceLoadBalancer {
    private int total=0;    // 被调用的次数
    private int index=0;    // 当前是谁在提供服务
    // 服务列表
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public MyLoadBalancerRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable();
        return supplier.get().next().map(this::getInstanceResponse);
    }

    //每个服务访问5次，然后换下一个服务
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        System.out.println("进入自定义负载均衡");
        if (instances.isEmpty()) {
            return new EmptyResponse();
        }
        System.out.println("每个服务访问5次后轮询");
        int size = instances.size();
        ServiceInstance serviceInstance=null;
        while (serviceInstance == null) {
            System.out.println("===");
            if (total < 5) {
                serviceInstance = instances.get(index);
                total++;
            } else {
                total=0;
                index++;
                if (index>=size) {
                    index=0;
                }
                serviceInstance=instances.get(index);
            }
        }
        return new DefaultResponse(serviceInstance);

    }
}
