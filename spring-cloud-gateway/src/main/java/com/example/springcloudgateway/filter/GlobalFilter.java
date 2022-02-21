package com.example.springcloudgateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {super(Config.class);}

    @Override
    public GatewayFilter apply(Config config) {
        //Global preFilter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global filter baseMessage -> {}", config.getBaseMessage());

            if(config.preLogger){
                log.info("Global filter start: request id -> {}", request.getId());
            }

            //Global postFilter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.postLogger){
                    log.info("Global filter end: response code -> {}", response.getStatusCode());
                }
            }));
        };
    }

    @Data
    public static class Config{
        private String baseMessage;
        private Boolean preLogger;
        private Boolean postLogger;

    }
}
