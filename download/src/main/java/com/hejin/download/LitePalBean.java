package com.hejin.download;

import org.litepal.crud.DataSupport;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/30 16:16
 * 类描述 : 实体类
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 *  
 */
public class LitePalBean extends DataSupport {

    private String name;
    private String age;
    private String sex;

    public LitePalBean(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "LitePalBean{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
