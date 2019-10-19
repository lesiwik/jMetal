package org.uma.jmetal.algorithm.multiobjective.lemas.visualization;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.BaseEMAS;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;


public abstract class BaseChart {
    protected final XYChart chart;

    public BaseChart() {
        this.chart = new XYChartBuilder().width(500).height(300).theme(Constants.CHART_THEME).build();
        this.chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        this.chart.getStyler().setLegendVisible(false);
    }

    public XYChart getChart() {
        return chart;
    }

    public abstract void update(List<DoubleSolution> population);

    public void update(List<DoubleSolution> population, String seriesName, BaseEMAS emas, int seriesNumber) {
        update(population, seriesName, emas);
    }

    public void update(List<DoubleSolution> population, String seriesName, Algorithm emas, int seriesNumber) {
        if (this instanceof MeetingsChart || this instanceof PopulationSizeChart) {
            if (emas instanceof BaseEMAS) {
                update(population, seriesName, (BaseEMAS) emas);
            }
        } else {
            update(population, seriesName, emas);
        }
    }

    public abstract void update(List<DoubleSolution> population, String seriesName);

    public void update(List<DoubleSolution> population, String seriesName, BaseEMAS emas) {
        update(population, seriesName);
    }

    public void update(List<DoubleSolution> population, String seriesName, Algorithm emas) {
        update(population, seriesName);
    }

}
