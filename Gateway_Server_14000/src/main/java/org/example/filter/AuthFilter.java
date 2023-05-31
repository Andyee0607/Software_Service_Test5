package org.example.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String token =exchange.getRequest().getQueryParams().getFirst("token");
        if(token==null){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);//401错误
            return exchange.getResponse().setComplete();
        }

        //用责任链模式，继续执行过滤器的下一个步骤
        return chain.filter(exchange);
    }
}
