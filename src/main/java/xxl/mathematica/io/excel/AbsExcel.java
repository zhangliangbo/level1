package xxl.mathematica.io.excel;

public abstract class AbsExcel implements IExcel {
  /**
   * 指定Excel实现
   *
   * @param method
   * @return
   */
  public static IExcel getExcelImpl(int method) {
    switch (method) {
      case IExcel.POI:
        return PoiExcel.getInstance();
      case IExcel.JXL:
        return JxlExcel.getInstance();
      default:
        throw new IllegalArgumentException("no such implementation");
    }
  }
}
