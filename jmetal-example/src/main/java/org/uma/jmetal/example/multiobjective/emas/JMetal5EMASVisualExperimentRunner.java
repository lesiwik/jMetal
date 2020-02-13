package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
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

         List<Algorithm<List<Solution<?>>>> algorithmsToRun = new AlgorithmFactory<>()
//                 .addProgressiveEMAS("Progressive_BETTER_AND_COULD", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
//                 .addProgressiveEMAS("Progressive_NOT_WORSE", Constants.IF_NOT_WORSE)
//                 .addProgressiveEMAS("Progressive_ALWAYS", Constants.ALWAYS)
//                 .addProgressiveEMAS("Progressive_BETTER", Constants.IF_BETTER)
//                 .addProgressiveAreaEMAS("ProgressiveArea_BETTER_AND_COULD", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
//                 .addProgressiveAreaEMAS("ProgressiveArea_NOT_WORSE", Constants.IF_NOT_WORSE)
//                 .addProgressiveAreaEMAS("ProgressiveArea_ALWAYS", Constants.ALWAYS)
//                 .addProgressiveAreaEMAS("ProgressiveArea_BETTER", Constants.IF_BETTER)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_BETTER_AND_COULD", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_ALWAYS", Constants.ALWAYS)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_NOT_WORSE", Constants.IF_NOT_WORSE)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_BETTER", Constants.IF_BETTER)
//                 .addEMAS("BaseEMAS")
//                 .addNotWorseEMAS("NotWorseEMAS")
                 .addAreaEMAS("AreaEMAS")
//                 .addProgressiveAreaNotWorseEMAS("ProgressiveAreaNotWorseEMAS", Constants.IF_NOT_WORSE)
//                 .addAreaCountingEMAS("ReproductiveAreaCountingEMAS")
//                 .addReproductiveAreaEMAS("ReproductiveAreaEMAS")
//                 .addReproductiveEMAS("ReproductiveEMAS")
//                 .addBaseNSGAII(Constants.NSGAII_INITIAL_POPULATION_SIZE, Constants.NSGAII_MAX_EVALUATIONS)
                 .getAlgorithms();

         PausableChartWrapper<Solution<?>> chartWrapper = new PausableChartWrapper<Solution<?>>(algorithmsToRun, Constants.NUMBER_OF_DECISION_VARIABLES_TO_SHOW);
         algorithmsToRun.forEach(algorithm -> ((Measurable) algorithm).getMeasureManager()
                 .getPushMeasure("currentPopulation")
                 .register(population ->
                         chartWrapper.updateChart((List<Solution<?>>) population, algorithm.getName(),algorithm,algorithmsToRun.indexOf(algorithm))));

         Map<String, AlgorithmRunner> algorithmRunners = new HashMap<>();
         algorithmsToRun
                 .parallelStream()
                 .forEach(algorithm -> algorithmRunners.put(algorithm.getName(), new AlgorithmRunner.Executor(algorithm)
                         .execute()));

         algorithmRunners.forEach((name, algorithmRunner) -> {
             long computingTime = algorithmRunner.getComputingTime();
             System.out.println("[" + name + "] Total execution time: " + computingTime/60 + "s");
         });
    }
}
