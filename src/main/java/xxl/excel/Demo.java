package xxl.excel;

import com.alibaba.excel.annotation.ExcelProperty;

public class Demo {
  @ExcelProperty("年龄")
  private int age = 25;
  @ExcelProperty("名称")
  private String name = "111";

//  public int getAge() {
//    return age;
//  }

  public void setAge(int age) {
    this.age = age;
  }

//  public String getName() {
//    return name;
//  }

  public void setName(String name) {
    this.name = name;
  }
}
