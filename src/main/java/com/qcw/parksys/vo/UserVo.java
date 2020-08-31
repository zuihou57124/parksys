package com.qcw.parksys.vo;


import com.qcw.parksys.ValidGroups.UserRegister;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 用户vo
 */
@Data
public class UserVo {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 4,max = 12,message = "用户名在4位到12位之间")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "邮箱格式错误")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6,max = 20,message = "密码长度在6位到20位之间")
    private String password;

    @NotBlank(message = "请输入验证码")
    private String code;

}
