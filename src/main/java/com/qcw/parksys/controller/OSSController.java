package com.qcw.parksys.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.qcw.parksys.common.utils.R;
import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云 oss Controller
 */
@Controller
@RequestMapping("parksys/oss")
public class OSSController {

    @Autowired(required = false)
    OSS ossClient;

    @Autowired
    UserService userService;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    public String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    public String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    public String accessId;

    /**
     * @param file
     * @return
     * 用户从前台上传头像，然后上传到阿里云 oss
     */
    @PostMapping("/upload")
    @ResponseBody
    public R uploadImg(Integer userId,MultipartFile file) throws IOException {

        String key = "parksys/userhead/"+file.getOriginalFilename();

        //创建PutObjectRequest对象。
        //PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, "测试1111", new File("C:\\Users\\root\\Desktop\\编程\\测试图片\\windows10_wallpaper_2x.jpg!0x0.jpg"));

        //上传文件
        PutObjectResult putObjectResult = ossClient.putObject(bucket,key,file.getInputStream());

        //关闭流
        ossClient.shutdown();

        String imgUrl = "https://qinfengoss.oss-cn-shenzhen.aliyuncs.com/parksys/userhead/" + file.getOriginalFilename();
        //更新用户头像信息
        UserEntity user = userService.getById(userId);
        user.setHeadImg(imgUrl);
        userService.updateById(user);

        return R.ok();
    }

    /**
     * 获得图片的url链接
     *
     * @param key
     * @return
     */
    @RequestMapping("/getImgUrl")
    @ResponseBody
    public R getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucket, key, expiration);
        if (url != null) {
            return R.ok().put("img_url",url.toString());
        }
        return R.error();
    }

}
