package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.config.MeetingType;
import org.uma.jmetal.solution.DoubleSolution;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChartWrapper {
    protected final SwingWrapper<XYChart> wrapper;
    protected List<BaseChart> charts;

    public ChartWrapper() {
        charts = new ArrayList<>();
        charts.add(new PopulationChart());
        charts.add(new HVRChart());
        wrapper = new SwingWrapper<>(charts.stream().map(BaseChart::getChart).collect(Collectors.toList()));
        wrapper.displayChartMatrix();
    }

    public ChartWrapper(List<Algorithm> algorithmToShow, int numberOFDecisionVariablesToShow) {
        charts = new ArrayList<>();
        charts.add(new PopulationChart(algorithmToShow));
        charts.add(new PopulationChart(algorithmToShow, true));

        //charts.add(new NoReferencePopulationChart(algorithmToShow));
        charts.add(new HVChart(algorithmToShow));
        charts.add(new HVRChart(algorithmToShow));
        charts.add(new IGDPlusChart(algorithmToShow));
        charts.add(new EvaluationHVRChart(algorithmToShow));
        charts.add(new EvaluationIGDPlusChart(algorithmToShow));
        for (int i = 0; i < numberOFDecisionVariablesToShow; i++) {
            charts.add(new SingleVariableChart(algorithmToShow, i));
        }
        charts.add(new LegendChart(algorithmToShow));

        charts.add(new MeetingsChart(algorithmToShow, MeetingType.I_AM_BETTER));
        charts.add(new MeetingsChart(algorithmToShow, MeetingType.NEITHER_IS_BETTER));
        charts.add(new PopulationSizeChart(algorithmToShow));

        wrapper = new SwingWrapper<>(charts.stream().map(BaseChart::getChart).collect(Collectors.toList()), 3, 3);
        wrapper.displayChartMatrix();
    }

    public void updateChart(List<DoubleSolution> data) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                    charts.get(i).update(data);
                    wrapper.repaintChart(i);
            }
        });
    }

    public void updateChart(List<DoubleSolution> data, String seriesName, JMetal5BaseEMAS emas, int seriesNumber) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).update(data, seriesName, emas, seriesNumber);
                wrapper.repaintChart(i);
            }
        });
    }


    public void updateChart(List<DoubleSolution> data, String seriesName, Algorithm emas, int seriesNumber) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).update(data, seriesName, emas, seriesNumber);
                wrapper.repaintChart(i);
            }
        });

    }


    public void updateChart(List<DoubleSolution> data, String seriesName) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).update(data, seriesName);
                wrapper.repaintChart(i);
            }
        });
    }
}


