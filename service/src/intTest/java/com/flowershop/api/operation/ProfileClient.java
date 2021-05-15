 
package com.flowershop.api.operation;

import com.flowershop.rest.support.LocalFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "profile", path = "${api.version}", configuration = LocalFeignConfig.class)
public interface ProfileClient extends ProfileOperations{
}
