//package org.uma.jmetal.runner.multiobjective.rewera.visualization;
//
//import org.knowm.xchart.XChartPanel;
//import org.knowm.xchart.internal.chartpart.Chart;
//import org.uma.jmetal.algorithm.Algorithm;
//import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
//import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.config.MeetingType;
//import org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.*;
//import org.uma.jmetal.solution.doublesolution.DoubleSolution;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public class SingleProblemChartWrapper<T extends Chart> {
//    private List<BaseChart> charts = new ArrayList<>();
//    private List<XChartPanel<T>> chartPanels = new ArrayList();
//    private int rowsAmount = 3;
//    private int columnsAmount = 3;
//
//    public SingleProblemChartWrapper(List<Algorithm> algorithmToShow, int numberOFDecisionVariablesToShow) {
//        charts.add(new PopulationChart(algorithmToShow));
//        charts.add(new HVRChart(algorithmToShow));
//        //charts.add(new IGDPlusChart(algorithmToShow));
//        charts.add(new EvaluationHVRChart(algorithmToShow));
//        charts.add(new EvaluationIGDPlusChart(algorithmToShow));
//        for (int i = 0; i < numberOFDecisionVariablesToShow ; i++) {
//            charts.add(new SingleVariableChart(algorithmToShow,i));
//        }
//        charts.add(new LegendChart(algorithmToShow));
//        charts.add(new MeetingsChart(algorithmToShow, MeetingType.I_AM_BETTER));
//        charts.add(new MeetingsChart(algorithmToShow, MeetingType.NEITHER_IS_BETTER));
//    }
//
//    public void updateChart(List<DoubleSolution> data, String seriesName,
//                            JMetal5BaseEMAS emas, int seriesNumber){
//        SwingUtilities.invokeLater(() -> {
//            for (int i = 0; i < charts.size() ; i++) {
//                charts.get(i).update(data,seriesName,emas,seriesNumber);
//                repaintChart(i);
//            }
//        });
//    }
//
//    public JPanel createPanel() {
//        final JPanel panel = new JPanel();
//        SwingUtilities.invokeLater(() -> {
//            panel.setLayout(new GridLayout(this.rowsAmount, this.columnsAmount));
//            Iterator<BaseChart> var1 = this.charts.iterator();
//
//            while (var1.hasNext()) {
//                BaseChart baseChart = var1.next();
//                if (baseChart != null) {
//                    XChartPanel<T> chartPanel = new XChartPanel(baseChart.getChart());
//                    this.chartPanels.add(chartPanel);
//                    panel.add(chartPanel);
//                } else {
//                    JPanel chartPanelx = new JPanel();
//                    panel.add(chartPanelx);
//                }
//            }
//            panel.setVisible(true);
//
//        });
//        return panel;
//    }
//
//    private void repaintChart(int index) {
//        this.chartPanels.get(index).revalidate();
//        this.chartPanels.get(index).repaint();
//    }
//}
