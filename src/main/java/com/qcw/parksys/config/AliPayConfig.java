package com.qcw.parksys.config;

import com.alipay.easysdk.kernel.Config;
import lombok.Data;


public class AliPayConfig {

    public static Config getConfig(){

        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";

        config.appId = "2016102800775128";

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7lEyUH4R1tMKWCiqitOuiviNEh70d1w6r4DTLejiVaV7g9zZZ0xTeYtBw67oL4jB/00pxs8CFBTJIUOsOhN0WG9G4mZ9q456kVLlKubWk10QWPwBo5euZJGmhlX/dbO90iSIJpZTP+O7KUDo1XBAkBIgVv7AxYRRk6E/sFEq1uvrGX/fu9yUSOZ3AXuPBe4uoy46Lt8lbZgFGRcm4tLIaM8iXgKGheGMpTS00DaMZlZRsgQrAb30YoZjWyms/qrdx8Tz/CRp054NDU/hTwvBpmLRw+2QRiaqVrJo+b72KzpMPIr/rpRwKNQcMxmXF+N2X2aoDt91VM/RkpVtDfohdAgMBAAECggEAJoN8da1C9Sf4C/Zqap3bA78mXpvAKLBPQtr1/BFMLOFFqcsYTbkZP8/qHEVKHcaDTruDPXU6whUfDdoPaRu+iRuNI+nm4Xt6xyLeKImY0g+g2zB0VRVgGkFvrs2TQ1NqlvLRGkn1E/54iPGQAgS1C7AigNSqyHi2R0Grpz1DWayAc75PgnOw/XwsITz7WSanhwzm5w4Ngr+10V2XG2kSxgvFvW23mFUtnFJWhbadrgcr24oThiZqdMZKZhBoRz6qitiCj8u6dfTd71BbErfM98b2ElGN73lvV9O/uI6rgSrrh2UM4xNJZJqHzkxxGUKfPsfiXPQIt3WMOoGn13HoAQKBgQDcmEWe0ykdigMo5ecnW7GoVCpNcwEMgVCVticVCY9uHclVUQlU72qy7XZzdyGkQKpmihtht4HrmL5faPTwY1aqNqWImnEpHhZlzEHdkPBf9tTiZBufTN6mLv2tTEM8hwcK9W+WW0w065GQWoCaWu4kyMgFZEjA9dG4i42KM45dYQKBgQDZr38K3QmgUXmLu4zN76S9BRjsGALAL15QJYPo3qDi5jjp0ObrLC3XeQYoYt/BRTFOtTurxSmGBh0Tcs2Y2cLRsVg7/0LR9DRGesGLooBh4hmzvPnxMOTgJ08zVSJhqinqYJ0qBJ/lJKbhX2UowqNeYCHBJPGXIZ+m/uD65XDwfQKBgDJrL0Zs1vHkcHJxkZKiV4m0qvKGvY+hIs0FFAuv4rN1ZWX1np9mFUoOEkuzx+Gl0gybtkRwT+aMQ77E227yFYXT9aOyQPj34oCj3c4JaZdVcDTKUrHnPOBYzyxDyP5VkluD21xPQ6y4xs1qkgg1jkakCWuKD0LjLGr0ooYa4IghAoGBALDcMv2ihHWqfHQY1tdiQpgIEi2PfHykf53iPP/wEQsIPWlKfrLHXB9ahrQ9UMn3642lFWwDbi8jvG/WsE/lbLFtwuBMDgACxo4UojdOubvNNVCg0vkw3mN3zJNKMfrjg9aW1FoVE8vaMXOeS9QH/y9oL0R7PYWqncnR/vwZed8JAoGABC/qX9bvm1K9Xc5f4UfiEATEOEGrnUhnIH2b60HPEhE8HDDrD4eHasPbLqAsJKjbnR8cjf/ocV3toI5JOYuBJx/qliwqQSatwdwUthbtdsavoWBEyN+DmD+DFVGL2/HWJ94So8IvE1KnfmJf30/aDBES8+Bk02E0MSN57GsKSf4=";

        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh0bFO0AiDMMUDpInFagUzVg3vxEY5PNv1pE+0zA9auUCCtOykcnpvVBmVSZpY6uhlNA7dXh3Ms1mEO3JfSqMkZL9ASR4PxBWSVBDO5GveJZg0ueH07CE1ZyfG5j4vtfn7QEnlNO9ail9Yq/Ve25i5si2xKZYLNM35cLoUaH94PZYBf5OoVfdEQNa4GCCaU5zr3yWaT39zf0yUA0QGIr1LPFVtwk67O0GRbCH4tT2Ger+dE4dANze0gCf/HpkyQ4MKebamGYeq0dYJ+vQr3a9/NjYvJxbDE1eXeHHA2kmSgRFSkCirNGL6pCuxOLwL10xs5spuOxvc3KJMHfT4haW1QIDAQAB";

        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "http://localhost:8088/parksys/order/paySuccess";

        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
        //config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";

        return config;

    }

}
