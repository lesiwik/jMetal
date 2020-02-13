package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.solution.Solution;

import java.util.List;


public abstract class BaseChart<S extends Solution<?>> {
    protected final XYChart chart;

    public BaseChart() {
        this.chart = new XYChartBuilder().width(500).height(300).theme(Constants.CHART_THEME).build();
        this.chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        this.chart.getStyler().setLegendVisible(false);
    }

    public XYChart getChart() {
        return chart;
    }

    public abstract void update(List<S> population);

    public void update(List<S> population, String seriesName, JMetal5BaseEMAS<S> emas, int seriesNumber) {
        update(population, seriesName, emas);
    }

    public void update(List<S> population, String seriesName, Algorithm<List<S>> emas, int seriesNumber) {
        if (this instanceof MeetingsChart || this instanceof PopulationSizeChart || this instanceof EvaluationsChart) {
            if (emas instanceof JMetal5BaseEMAS) {
                update(population, seriesName, (JMetal5BaseEMAS<S>) emas);
            }
        } else {
            update(population, seriesName, emas);
        }
    }

    public abstract void update(List<S> population, String seriesName);

    public void update(List<S> population, String seriesName, JMetal5BaseEMAS<S> emas) {
        update(population, seriesName);
    }

    public void update(List<S> population, String seriesName, Algorithm<List<S>> emas) {
        update(population, seriesName);
    }

}
