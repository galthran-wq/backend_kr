package com.resitplatform.api.operation;

import com.resitplatform.rest.support.LocalFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "flower", path = "${api.version}", configuration = LocalFeignConfig.class)
public interface FlowerClient extends ResitOperations {
}
