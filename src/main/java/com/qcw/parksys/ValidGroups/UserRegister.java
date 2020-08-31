package com.qcw.parksys.ValidGroups;


import javax.validation.groups.Default;

/**
 * 自定义分组时,需要继承 Default(即默认分组) 类,否则其他字段不会验证
 */
public interface UserRegister extends Default {


}
