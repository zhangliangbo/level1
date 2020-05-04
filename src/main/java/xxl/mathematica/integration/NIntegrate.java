package xxl.mathematica.integration;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator;
import org.apache.commons.math3.analysis.integration.MidPointIntegrator;
import xxl.mathematica.function.Function;

/**
 * 数值积分
 */
public class NIntegrate {
  /**
   * 数值积分
   *
   * @param f
   * @param min
   * @param max
   * @return
   */
  public static double nIntegrate(Function<Double, Double> f, double min, double max) {
    MidPointIntegrator integrator = new MidPointIntegrator();
    return integrator.integrate(BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT,
        new UnivariateFunction() {
          @Override
          public double value(double x) {
            return f.apply(x);
          }
        },
        min,
        max);
  }
}
