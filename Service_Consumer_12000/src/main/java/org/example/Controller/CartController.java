package org.example.Controller;
import com.netflix.discovery.DiscoveryClient;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.entity.CommonResult;
import org.example.entity.User;
import org.example.feign.UserFeignService;
import org.example.loadbalance.CustomerLoadBalanceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cart")
@RefreshScope
@LoadBalancerClient(name="provider-server",configuration=CustomerLoadBalanceConfiguration.class)
public class CartController {
    @Autowired
    private UserFeignService userFeignService;
    private RestTemplate restTemplate;
    @SentinelResource("hot")
    @GetMapping({"/addCart/{userId}", "/getUserById/{userId}"})
    public CommonResult getUserById(@PathVariable("userId") Integer userId){

        //使用微服务名替换IP地址和端口

        CommonResult result = restTemplate.getForObject(
                "http://provider-server/user/getUserById/"+userId, CommonResult.class);
        return result;
    }
    @PostMapping({"/postCart/{userId}","/postUserById/{userId}"})
    public CommonResult postUserById(@PathVariable("userId") Integer userId){
        CommonResult result = restTemplate.getForObject(
                "http://provider-server/user/postUserById/"+userId, CommonResult.class);
        return result;
    }
    @PutMapping({"putCart/{userId}","/putUserById/{userId}"})
    public CommonResult putUserById(@PathVariable("userId") Integer userId){
        CommonResult result = restTemplate.getForObject(
                "http://provider-server/user/putUserById/"+userId, CommonResult.class);
        return result;
    }
    @DeleteMapping({"/deleteCart/{userId}","/deleteUserById/{userId}"})
    public CommonResult deleteUserById(@PathVariable("userId") Integer userId){
        CommonResult result = restTemplate.getForObject(
                "http://provider-server/user/deleteUserById/"+userId, CommonResult.class);
        return result;
    }


    @GetMapping("/hello")
    public String hello(){
        return userFeignService.hello();
    }

 //   @CircuitBreaker(name = "bulkheadA",fallbackMethod = "fallback",type= Bulkhead.Type.THREADPOOL)//fallback提供服务降级的返回
//    public CompletableFuture<User> addCart(@PathVariable Integer userId) throws InterruptedException {
//
//        System.out.println("进入方法");
//        //Thread.sleep(10000L);//阻塞10秒
//        CompletableFuture<User> result=CompletableFuture.supplyAsync(()->{
//            return userFeignService.getUserById(userId).getResult();
//        });
//        CommonResult<User> list=userFeignService.getUserById(userId);
//        System.out.println("离开方法");
//        return result;
//    }

//    public CompletableFuture<User> fallback(Integer userId, Throwable e){
//
//        e.printStackTrace();
//        System.out.println("fallback已经调用");
//        CompletableFuture<User> result=CompletableFuture.supplyAsync(()->{
//            return new CommonResult<>(400,"当前用户服务不正常，请稍后再试",new User())
//        });
//        return result;
//    }
}

