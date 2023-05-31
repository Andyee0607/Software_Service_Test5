package org.example.controller;

import org.example.entity.CommonResult;
import org.example.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class consumerServicefallback {
    @RequestMapping(value = "/fallback",method= RequestMethod.GET)
    public CommonResult GetCommonResult(){
        return  new CommonResult<>(403,"由于ConsumerService异常进行服务降级",new User());
    }
}
