package org.uma.jmetal.algorithm.multiobjective.lemas.visualization;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.BaseEMAS;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SingleObjectiveChartWrapper {
    private final SwingWrapper<XYChart> wrapper;
    private List<BaseChart> charts;


    public SingleObjectiveChartWrapper(List<Algorithm> algorithmToShow, int numberOFDecisionVariablesToShow) {
        charts = new ArrayList();
        charts.add(new SingleObjectivePopulationChart(algorithmToShow));
//        charts.add(new HVChart(algorithmToShow));
//        charts.add(new HVRChart(algorithmToShow));
//        charts.add(new IGDPlusChart(algorithmToShow));
//        charts.add(new EvaluationHVRChart(algorithmToShow));
//        charts.add(new EvaluationIGDPlusChart(algorithmToShow));
        for (int i = 0; i < numberOFDecisionVariablesToShow; i++) {
            charts.add(new VariableChart(algorithmToShow, i));
        }
        charts.add(new LegendChart(algorithmToShow));

        wrapper = new SwingWrapper(charts.stream().map(BaseChart::getChart).collect(Collectors.toList()), 3, 3);
        wrapper.displayChartMatrix();
    }


    //    public void updateChart(List<DoubleSolution> data) {
//        javax.swing.SwingUtilities.invokeLater(() -> {
//        for (int i = 0; i < charts.size() ; i++) {
//            charts.get(i).update(data);
//            wrapper.repaintChart(i);
//            }
//        });
//    }
//
    public void updateChart(List<DoubleSolution> data, String seriesName, BaseEMAS emas, int seriesNumber) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).update(data, seriesName, emas, seriesNumber);
                wrapper.repaintChart(i);
            }
        });
    }

//    public void updateChart(List<DoubleSolution> data, String seriesName){
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            for (int i = 0; i < charts.size() ; i++) {
//                charts.get(i).update(data,seriesName);
//                wrapper.repaintChart(i);
//            }
//        });
//    }

}


