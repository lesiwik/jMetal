package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;


import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.util.List;

public class LegendChart<S extends Solution<?>> extends BaseChart<S>{

    public LegendChart(List<Algorithm<S>> algorithmToShow) {
        super();
        this.chart.getStyler().setLegendVisible(true);

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideN);
        chart.getStyler().setPlotBorderVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);
        for (Algorithm alg : algorithmToShow) {
            chart.addSeries(alg.getName(), new double[]{0}, new double[]{0});
        }
    }

    @Override
    public void update(List<S> population) {
    }

    @Override
    public void update(List<S> population, String seriesName) {
    }
}

