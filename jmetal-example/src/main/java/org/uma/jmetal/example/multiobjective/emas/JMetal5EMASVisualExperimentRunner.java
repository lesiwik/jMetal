package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.TimeFormatter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.PausableChartWrapper;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.measure.Measurable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JMetal5EMASVisualExperimentRunner extends AbstractAlgorithmRunner {

     public static void main(String[] args) throws JMetalException {

         List algorithmsToRun = AlgorithmFactory.getAlgorithms();

         algorithmsToRun.add((Algorithm) AlgorithmFactory.createBaseNSGAII(Constants.NSGAII_INITIAL_POPULATION_SIZE, Constants.NSGAII_MAX_EVALUATIONS ));

         PausableChartWrapper<Solution<?>> chartWrapper = new PausableChartWrapper<Solution<?>>(algorithmsToRun, Constants.NUMBER_OF_DECISION_VARIABLES_TO_SHOW);
         algorithmsToRun.forEach(algorithm -> ((Measurable) algorithm).getMeasureManager()
                 .getPushMeasure("currentPopulation")
                 .register(population ->
                         chartWrapper.updateChart((List<Solution<?>>) population, ((Algorithm<?>) algorithm).getName(), (Algorithm<List<Solution<?>>>) algorithm,algorithmsToRun.indexOf(algorithm))));

         Map<String, AlgorithmRunner> algorithmRunners = new HashMap<>();
         algorithmsToRun
                 .parallelStream()
                 .forEach(algorithm -> algorithmRunners.put(((Algorithm<?>) algorithm).getName(), new AlgorithmRunner.Executor((Algorithm<?>) algorithm)
                         .execute()));

         chartWrapper.displayAdditionalStatistics();

         algorithmRunners.forEach((name, algorithmRunner) -> {
             long computingTime = algorithmRunner.getComputingTime();
             System.out.println("[" + name + "] Total execution time: " + TimeFormatter.msToTime(computingTime));
         });
    }
}
