package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

import java.io.FileNotFoundException;
import java.util.List;

import static org.uma.jmetal.util.AbstractAlgorithmRunner.printFinalSolutionSet;
import static org.uma.jmetal.util.AbstractAlgorithmRunner.printQualityIndicators;


public class JMetal5EMASLogExperimentRunner {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws JMetalException, FileNotFoundException {

        Algorithm<List<DoubleSolution>> algorithm = new AlgorithmFactory<>()
                .addAreaEMAS("AreaEMAS")
                .getAlgorithm(0);

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();

        List<DoubleSolution> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        printFinalSolutionSet(population);
        printQualityIndicators(population, "referenceFronts/ZDT1.pf");
    }
}
