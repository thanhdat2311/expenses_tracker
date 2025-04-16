package vn.dathocjava.dathocjava_sample.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SigInRequest implements Serializable {
    private  String email;
    private String password;
    private String platform;// web, mobile, miniAPpp
    private String deviceToken;
    private String versionApp;
}
