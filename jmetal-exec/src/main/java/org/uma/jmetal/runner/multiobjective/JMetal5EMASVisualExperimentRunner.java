package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.EMASFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.PausableChartWrapper;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.measure.Measurable;

import java.util.List;


public class JMetal5EMASVisualExperimentRunner extends AbstractAlgorithmRunner {


     public static void main(String[] args) throws JMetalException {

         @SuppressWarnings("unchecked")
         List<Algorithm> algorithmsToRun = new EMASFactory<>()
//                 .addSmartBaseEMAS("SmartEMAS_1", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
//                 .addSmartBaseEMAS("SmartEMAS_2", Constants.IF_NOT_WORSE)
//                 .addSmartBaseEMAS("SmartEMAS_3", Constants.ALWAYS)
//                 .addSmartBaseEMAS("SmartEMAS_4", Constants.IF_BETTER)
                 //.addSmartAreaEMAS("SmartEMAS_1", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
                 //.addSmartAreaEMAS("SmartEMAS_3", Constants.ALWAYS)
                 //.addSmartAreaEMAS("SmartEMAS_2", Constants.IF_NOT_WORSE)
                 //.addSmartAreaEMAS("SmartEMAS_4", Constants.IF_BETTER)
                //.addEMAS("BaseEMAS")
                .addAreaEMAS("AreaEMAS")
                //.addProgressiveAreaEMAS("ProgressiveAreaEMAS")
                //.addProgressiveEMAS("ProgressiveEMAS")
                .getAlgorithms();

         //Slider execution
         PausableChartWrapper chartWrapper = new PausableChartWrapper(algorithmsToRun, Constants.NUMBER_OF_DECISION_VARIABLES_TO_SHOW);
         algorithmsToRun.forEach(algorithm -> ((Measurable) algorithm).getMeasureManager()
                 .getPushMeasure("currentPopulation")
                 .register(population ->
                         chartWrapper.updateChart((List<DoubleSolution>) population, algorithm.getName(),algorithm,algorithmsToRun.indexOf(algorithm))));

         algorithmsToRun
                 .parallelStream()
                 .forEach(algorithm -> new AlgorithmRunner.Executor(algorithm)
                         .execute());
    }
}
