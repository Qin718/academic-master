package com.example.common.utils;


import org.springframework.mobile.device.Device;

public class MobileUtils {
    public static String getDeviceFrom(Device device) {
        if (device.isMobile()) {
            return "手机";
        }
        if (device.isTablet()) {
            return "平板";
        }
        if (device.isNormal()) {
            return "PC";
        }
        return "其它" ;
    }
}

