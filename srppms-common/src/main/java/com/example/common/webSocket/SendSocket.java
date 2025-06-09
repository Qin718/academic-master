package com.example.common.webSocket;

import lombok.Data;

@Data
public class SendSocket {
    //socket类型
    public String type;
    //socket提示信息
    public String message;
    //socket携带数据
    public Object data;

    @Override
    public String toString() {
        return "{" +
                "\"type\"=\"" + type + "\"" +
                ",\"message\"=\"" + message + "\"" +
                ",\"data\"=\"" + data + "\"" +
                "}";
    }
}
