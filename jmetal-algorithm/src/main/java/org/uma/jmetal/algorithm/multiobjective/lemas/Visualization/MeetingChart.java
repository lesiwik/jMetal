package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;//package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;
//
//import org.uma.jmetal.algorithm.Algorithm;
//import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
//import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
//import org.uma.jmetal.problem.Problem;
//import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
//import org.uma.jmetal.solution.DoubleSolution;
//import org.uma.jmetal.util.SolutionListUtils;
//import org.uma.jmetal.util.front.Front;
//import org.uma.jmetal.util.front.imp.ArrayFront;
//import org.uma.jmetal.util.front.util.FrontNormalizer;
//import org.uma.jmetal.util.front.util.FrontUtils;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//public class MeetingChart extends BaseChart {
//    private int iterationCounter = 0;
//    private List<Double> hvValues = new ArrayList<>();
//    private List xValues = new ArrayList<>();
//    private Map<String,List> yValuesMap = new HashMap<>();
//    private Map<String,List> xValuesMap = new HashMap<>();
//    private Map<String,Integer> iterationCounterMap = new HashMap<>();
//
//
//
//
//
//    public MeetingChart() {
//        super();
//        chart.addSeries("Meetings", new double[]{0}, new double[]{0});
//    }
//
//    public MeetingChart(List<Algorithm> algorithmToShow) {
//        super();
//        this.getChart().setTitle("Meeting(evaluation%"+Constants.HV_FREQUENCY+")");
//        for (Algorithm alg: algorithmToShow) {
//            chart.addSeries(alg.getName(), new double[]{0}, new double[]{0});
//            yValuesMap.put(alg.getName(),new ArrayList());
//            xValuesMap.put(alg.getName(),new ArrayList());
//            iterationCounterMap.put(alg.getName(),0);
//        }
//    }
//
//
//    @Override
//    public void update(List<DoubleSolution> population) {
//
//
//        if (iterationCounter % Constants.HV_FREQUENCY == 0) {
////            hvValues.add(hv.evaluate(
////                    getNonDominatedSolutions(population))/referenceHV);
////
////            xValues.add(iterationCounter);
////            chart.updateXYSeries(
////                    "HV",
////                    xValues,
////                    hvValues, null);
//        }
//        iterationCounter++;
//
//    }
//
//    @Override
//    public void update(List<DoubleSolution> population, String seriesName) {
//        if (iterationCounterMap.get(seriesName) % Constants.HV_FREQUENCY == 0) {
//            yValuesMap.get(seriesName).add(hv.evaluate(
//                    getNonDominatedSolutions(population))/referenceHV);
//
//            xValuesMap.get(seriesName).add(iterationCounterMap.get(seriesName));
//            chart.updateXYSeries(
//                    seriesName,
//                    xValuesMap.get(seriesName),
//                    yValuesMap.get(seriesName), null);
//        }
//        iterationCounterMap.put(seriesName,iterationCounterMap.get(seriesName)+1);
//    }
//
//    @Override
//    public void update(List<DoubleSolution> population, String seriesName, JMetal5BaseEMAS emas) {
////        System.out.println(iterationCounterMap.get(seriesName)+ " " + emas.iterations);
//        if (iterationCounterMap.get(seriesName) % Constants.HV_FREQUENCY == 0) {
////        if (emas.iterations % Constants.HV_FREQUENCY == 0) {
////        if (emas.evaluations % Constants.HV_FREQUENCY == 0) {
//            yValuesMap.get(seriesName).add(hv.evaluate(
//                    getNonDominatedSolutions(population))/referenceHV);
//
//            xValuesMap.get(seriesName).add(emas.evaluations);
//            chart.updateXYSeries(
//                    seriesName,
//                    xValuesMap.get(seriesName),
//                    yValuesMap.get(seriesName), null);
//        }
//        iterationCounterMap.put(seriesName,iterationCounterMap.get(seriesName)+1);
//    }
//}
