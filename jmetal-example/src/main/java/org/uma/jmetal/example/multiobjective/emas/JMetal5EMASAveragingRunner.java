package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import java.io.FileNotFoundException;
import java.util.*;

public class JMetal5EMASAveragingRunner<S extends Solution<?>> {

    private Front referenceFront;
    private Front normalizedReferenceFront;
    private FrontNormalizer frontNormalizer;

    private Problem<?> currentProblem = Constants.PROBLEM;

    private List<GenericIndicator<S>> indicators;
    private List<GenericIndicator<PointSolution>> normalizedIndicators;
    private List<Double> referenceIndicators;
    private List<Integer> evaluations;

    private static final int NUMBER_OF_RUNS = 5;
    private Map<GenericIndicator<?>, Double> evaluatedIndicators;
    private List<Algorithm<List<Solution<?>>>> algorithmsToRun;

    public static void main(String[] args)
            throws FileNotFoundException {
        JMetal5EMASAveragingRunner<?> runner = new JMetal5EMASAveragingRunner<>();
        runner.initializeFronts();
        runner.initializeIndicators();
        runner.initializeAlgorithms();
    }

    private JMetal5EMASAveragingRunner()
    {
        indicators           = new ArrayList<>();
        normalizedIndicators = new ArrayList<>();
        evaluations          = new ArrayList<>();
        referenceIndicators  = new ArrayList<>();
        evaluatedIndicators  = new HashMap<>();
    }

    private void initializeAlgorithms()
    {
        algorithmsToRun = new AlgorithmFactory<>()
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
//                 .addAreaEMAS("AreaEMAS")
//                 .addRadiusBaseEMAS("RadiusEMAS")
//                 .addRadiusAreaEMAS("RadiusAreaEMAS")
//                 .addProgressiveAreaNotWorseEMAS("ProgressiveAreaNotWorseEMAS", Constants.IF_NOT_WORSE)
//                 .addAreaCountingEMAS("AreaCountingEMAS")
//                 .addAreaCountingRadiusEMAS("AreaCountingRadiusEMAS")
//                 .addQualityAverageAreaEMAS("QualityAverageAreaEMAS")
//                 .addQualityConstantAreaEMAS("QualityConstantAreaEMAS")
                .addQualityDifferenceAreaEMAS("QualityDifferenceAreaEMAS")
//                 .addReproductiveAreaEMAS("ReproductiveAreaEMAS")
//                 .addReproductiveEMAS("ReproductiveEMAS")
//                 .addBaseNSGAII(Constants.NSGAII_INITIAL_POPULATION_SIZE, Constants.NSGAII_MAX_EVALUATIONS)
                .getAlgorithms();
    }

    private void initializeFronts()
            throws FileNotFoundException {
        String paretoFrontFile = Constants.REF_FRONT_DIR + currentProblem.getName() + ".pf";
        referenceFront = new ArrayFront(paretoFrontFile);
        frontNormalizer = new FrontNormalizer(referenceFront);
        normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
    }

    private void initializeIndicators()
    {
        PISAHypervolume<S> pisaHyperVolume                                                    = new PISAHypervolume<>(referenceFront);
        PISAHypervolume<PointSolution> pisaHyperVolumeRatio                                   = new PISAHypervolume<>(referenceFront);
        InvertedGenerationalDistance<S> invertedGenerationalDistance                          = new InvertedGenerationalDistance<>(referenceFront);
        InvertedGenerationalDistance<PointSolution> invertedGenerationalDistanceRatio         = new InvertedGenerationalDistance<>(referenceFront);
        InvertedGenerationalDistancePlus<S> invertedGenerationalDistancePlus                  = new InvertedGenerationalDistancePlus<>(referenceFront);
        InvertedGenerationalDistancePlus<PointSolution> invertedGenerationalDistancePlusRatio = new InvertedGenerationalDistancePlus<>(referenceFront);

        indicators.addAll(Arrays.asList(pisaHyperVolume,
                invertedGenerationalDistance,
                invertedGenerationalDistancePlus));

        normalizedIndicators.addAll(Arrays.asList(pisaHyperVolumeRatio,
                        invertedGenerationalDistanceRatio,
                        invertedGenerationalDistancePlusRatio));

        normalizedIndicators.forEach(indicator ->
        {
            double referenceIndicator = indicator.evaluate(FrontUtils.convertFrontToSolutionList(normalizedReferenceFront));
            referenceIndicators.add(referenceIndicator);
            evaluatedIndicators.put(indicator, 0. );
        });
        indicators.forEach(indicator -> evaluatedIndicators.put(indicator, 0.));
    }



    /**
     * Print all the available quality indicators
     * @param population population based on which indicators will be calculated.
     */
    public String constructQualityIndicatorsString(List<S> population) {

        Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population)) ;
        List<PointSolution> normalizedPopulation = FrontUtils
                .convertFrontToSolutionList(normalizedFront) ;

        String outputString = "\n" ;
        outputString += "Hypervolume (N) : " +
                new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
        outputString += "Hypervolume     : " +
                new PISAHypervolume<S>(referenceFront).evaluate(population) + "\n";
        outputString += "IGD (N)         : " +
                new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
        outputString +="IGD             : " +
                new InvertedGenerationalDistance<S>(referenceFront).evaluate(population) + "\n";
        outputString += "IGD+ (N)        : " +
                new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
        outputString += "IGD+            : " +
                new InvertedGenerationalDistancePlus<S>(referenceFront).evaluate(population) + "\n";

        JMetalLogger.logger.info(outputString);
        return outputString;
    }
}
