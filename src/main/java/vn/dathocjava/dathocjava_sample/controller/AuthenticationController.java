package vn.dathocjava.dathocjava_sample.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dathocjava.dathocjava_sample.controller.request.SigInRequest;
import vn.dathocjava.dathocjava_sample.response.TokenResponse;

@RestController
@RequestMapping("/auth")
@Slf4j(topic = "AUTHEN-CONTROLLER")
@Tag(name = "authen controller")
public class AuthenticationController {
    @PostMapping("/access-token")
    public TokenResponse getAccessToken(@RequestBody SigInRequest request){
    log.info("request access token");
    return  TokenResponse.builder()
            .accessToken("DUMMY-ACCESS-TOKEN")
            .refreshToken("DUMMY-REFRESH-TOKEN")
            .build();
    }
    @PostMapping("/refresh-token")
    public TokenResponse getRefreshToken(@RequestBody String refreshToken){
        log.info("refresh access token");
        return  TokenResponse.builder()
                .accessToken("DUMMY-ACCESS-TOKEN")
                .refreshToken("DUMMY-REFRESH-TOKEN")
                .build();
    }
}
