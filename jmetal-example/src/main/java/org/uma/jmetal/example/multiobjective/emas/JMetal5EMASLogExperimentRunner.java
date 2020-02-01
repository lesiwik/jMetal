package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

import java.io.FileNotFoundException;
import java.util.List;

import static org.uma.jmetal.util.AbstractAlgorithmRunner.printFinalSolutionSet;
import static org.uma.jmetal.util.AbstractAlgorithmRunner.printQualityIndicators;


public class JMetal5EMASLogExperimentRunner {

    public static void main(String[] args) throws JMetalException, FileNotFoundException {

        Algorithm<List<DoubleSolution>> algorithm = new AlgorithmFactory()
                .addEMAS("BaseEMAS").getAlgorithm(0);

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();
        JMetal5BaseEMAS<DoubleSolution> emas = (JMetal5BaseEMAS<DoubleSolution>)algorithm;
        List<List<DoubleSolution>> results = emas.getResults();
        List<DoubleSolution> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        printFinalSolutionSet(population);
        for (List<DoubleSolution>l:results)
        {
            //System.out.println();
            printQualityIndicators(l, "referenceFronts/ZDT1.pf");
        }
    }
}
