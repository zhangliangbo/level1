package xxl.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.ArrayList;
import java.util.List;

public class Excel {
  public static void demoExport() {
    List<Demo> list = new ArrayList<>();
    Demo demo = new Demo();
    demo.setName("aaa");
    demo.setAge(10);
    list.add(demo);
    Demo demo1 = new Demo();
    demo1.setName("bbb");
    demo1.setAge(11);
    list.add(demo1);
    // 这里 需要指定写用哪个class去读
    ExcelWriter excelWriter = EasyExcel.write("666.xlsx", Demo.class).build();
    WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();

    excelWriter.write(list, writeSheet);
    /// 千万别忘记finish 会帮忙关闭流
    excelWriter.finish();
  }
}
