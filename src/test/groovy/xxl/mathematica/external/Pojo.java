package xxl.mathematica.external;

import xxl.mathematica.annotation.ExcelColumnName;

public class Pojo {

  @ExcelColumnName("名称")
  private String name;

  @ExcelColumnName("年龄")
  private int age;

  @ExcelColumnName("性别")
  private String sex;

  public Pojo(String name, int age, String sex) {
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

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }
}
