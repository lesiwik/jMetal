package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.uma.jmetal.util.AbstractAlgorithmRunner.printFinalSolutionSet;

public class JMetal5EMASLogExperimentRunner {

    public static void main(String[] args) throws JMetalException, IOException {
        String emasName = "AUCEMAS";
        for(int i=0;i<1;i++) {
            String fileName = String.format (emasName + "%05d.out", i);

        Algorithm<List<Solution<?>>> algorithm = new AlgorithmFactory<>()
                .addAreaEMAS("AreaEMAS")
                .getAlgorithm(0);

            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                    .execute();
            JMetal5BaseEMAS<DoubleSolution> emas = (JMetal5BaseEMAS<DoubleSolution>) algorithm;
            List<List<DoubleSolution>> results = emas.getResults();
            List<DoubleSolution> population = results.get(results.size()-1);
            long computingTime = algorithmRunner.getComputingTime();

        List<Solution<?>> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

//            try(FileWriter fw = new FileWriter(fileName, true);
//                BufferedWriter bw = new BufferedWriter(fw);
//                PrintWriter out = new PrintWriter(bw))
//            {
//                for (List<DoubleSolution> l : results) {
//
//                    Front referenceFront = new ArrayFront("referenceFronts/ZDT1.pf");
//                    FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
//
//                    Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
//                    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(l));
//                    List<PointSolution> normalizedPopulation = FrontUtils
//                            .convertFrontToSolutionList(normalizedFront);
//                    //System.out.println();
//                    //new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(population) + "\n";
//    //                System.out.println(new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//                    //out.println(new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation).toString()
//                            );
//    //            printQualityIndicators(l, "referenceFronts/ZDT1.pf");
//                }
//            } catch (IOException e) {
//                //exception handling left as an exercise for the reader
//            }
        }
    }
}
