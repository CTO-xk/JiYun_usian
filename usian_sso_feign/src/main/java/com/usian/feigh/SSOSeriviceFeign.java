package com.usian.feigh;

import com.usian.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("usian-sso-service")
public interface SSOSeriviceFeign {

    @RequestMapping("/service/checkUserInfo/{checkValue}/{checkFlag}")
    boolean checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag);

    @RequestMapping("/service/sso/userRegister")
    Integer userRegister(@RequestBody TbUser user);

    @RequestMapping("/service/sso/userLogin")
    Map userLogin(@RequestParam String username, @RequestParam String password);

    @PostMapping("/service/sso/getUserByToken/{token}")
    TbUser getUserByToken(@PathVariable String token);

    @PostMapping("/service/sso/logOut")
    Boolean logOut(@RequestParam String token);
}
