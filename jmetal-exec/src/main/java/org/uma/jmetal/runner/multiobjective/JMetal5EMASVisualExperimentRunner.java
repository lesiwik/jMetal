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
