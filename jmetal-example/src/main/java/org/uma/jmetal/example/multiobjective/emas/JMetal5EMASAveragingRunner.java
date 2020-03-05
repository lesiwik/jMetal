package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.example.AlgorithmRunner;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JMetal5EMASAveragingRunner<S extends Solution<?>> {

    private Front referenceFront;
    private Front normalizedReferenceFront;
    private FrontNormalizer frontNormalizer;

    private Problem<?> currentProblem = Constants.PROBLEM;

    private List<GenericIndicator<S>> indicators;
    private List<GenericIndicator<PointSolution>> normalizedIndicators;
    private List<List<Double>> referenceIndicators;
    private List<Integer> evaluations;

    private static final int NUMBER_OF_RUNS = 5;
    private List<Map<GenericIndicator<?>, Double>> evaluatedIndicators;
    private List<Algorithm<List<Solution<?>>>> algorithmsToRun;

    private JMetal5EMASAveragingRunner()
    {
        indicators           = new ArrayList<>();
        normalizedIndicators = new ArrayList<>();
        evaluations          = new ArrayList<>();
        referenceIndicators  = new ArrayList<>();
        evaluatedIndicators  = new ArrayList<>();
    }

    public static void main(String[] args)
            throws FileNotFoundException {
        JMetal5EMASAveragingRunner<?> runner = new JMetal5EMASAveragingRunner<>();
        runner.initializeFronts();
        runner.initializeAlgorithms();
        runner.initializeIndicators();
        runner.runAlgorithms();
        runner.averageResults();
        runner.saveResults();
    }

    private void saveResults()
    {
        //TODO: Save results
        System.out.println(Stream.of(indicators, normalizedIndicators).flatMap(Collection::stream).map(GenericIndicator::getName).collect(Collectors.toList()));
        for(int algorithmIndex = 0 ; algorithmIndex < algorithmsToRun.size() ; algorithmIndex++)
        {

        }
    }

    private void averageResults(){
        evaluatedIndicators.parallelStream().forEach(indicatorMap ->
            indicatorMap.forEach(
                    (indicator, value) -> value = value / NUMBER_OF_RUNS)
        );

        evaluations = evaluations.stream().map(value -> value / NUMBER_OF_RUNS).collect(Collectors.toList());
    }


    private void runAlgorithms()
    {
        algorithmsToRun.parallelStream().forEach(algorithm -> {
            for(int i = 0; i < NUMBER_OF_RUNS; i++)
            {
                JMetal5BaseEMAS<Solution<?>> emasRef = (JMetal5BaseEMAS<Solution<?>>) algorithm;
                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(emasRef)
                        .execute();
                long computingTime = algorithmRunner.getComputingTime();
                System.out.println("[" + emasRef.getName() + "] [Run " + (i+1) + "/5] Total execution time: " + computingTime/60 + "s");
                updateMetrics(emasRef);
                emasRef.resetState();
            }
        });
    }

    private void updateMetrics(JMetal5BaseEMAS<Solution<?>> algorithm)
    {
        List<S> population = (List<S>) algorithm.getResult();
        int algorithmIndex = algorithmsToRun.indexOf(algorithm);
        evaluations.set(algorithmIndex, evaluations.get(algorithmIndex) + algorithm.getEvaluations());

        Map<GenericIndicator<?>, Double> indicatorsForAlgorithm = evaluatedIndicators.get(algorithmIndex);

        indicators.forEach(indicator ->
        {
            double oldValue = indicatorsForAlgorithm.get(indicator);
            indicatorsForAlgorithm.put(indicator, oldValue + indicator.evaluate(population));
        });


        Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
        List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

        for(int indicatorIndex = 0; indicatorIndex < normalizedIndicators.size() ; indicatorIndex++)
        {
            GenericIndicator<PointSolution> normalizedIndicator = normalizedIndicators.get(indicatorIndex);
            double oldValue           = indicatorsForAlgorithm.get(normalizedIndicator);
            double referenceIndicator = referenceIndicators.get(algorithmIndex).get(indicatorIndex);
            double evaluationValue    = normalizedIndicator.evaluate(normalizedPopulation);
            if(Double.compare(referenceIndicator, 0) != 0)
                evaluationValue = evaluationValue / referenceIndicator;
            indicatorsForAlgorithm.put(normalizedIndicator, oldValue + evaluationValue);
        }

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
                 .addEMAS("BaseEMAS")
//                 .addEMAS("BaseEMAS1")
//                 .addEMAS("BaseEMAS2")
//                 .addEMAS("BaseEMAS3")
//                 .addEMAS("BaseEMAS4")
//                 .addNotWorseEMAS("NotWorseEMAS")
//                 .addAreaEMAS("AreaEMAS")
//                 .addRadiusBaseEMAS("RadiusEMAS")
//                 .addRadiusAreaEMAS("RadiusAreaEMAS")
//                 .addProgressiveAreaNotWorseEMAS("ProgressiveAreaNotWorseEMAS", Constants.IF_NOT_WORSE)
//                 .addAreaCountingEMAS("AreaCountingEMAS")
//                 .addAreaCountingRadiusEMAS("AreaCountingRadiusEMAS")
//                 .addQualityAverageAreaEMAS("QualityAverageAreaEMAS")
//                 .addQualityConstantAreaEMAS("QualityConstantAreaEMAS")
//                 .addQualityDifferenceAreaEMAS("QualityDifferenceAreaEMAS")
//                 .addReproductiveAreaEMAS("ReproductiveAreaEMAS")
//                 .addReproductiveEMAS("ReproductiveEMAS")
//                 .addBaseNSGAII(Constants.NSGAII_INITIAL_POPULATION_SIZE, Constants.NSGAII_MAX_EVALUATIONS)
                .getAlgorithms();

        for(int i = 0; i < algorithmsToRun.size() ; i++)
        {
            evaluatedIndicators.add(new HashMap<>());
            referenceIndicators.add(new ArrayList<>());
            evaluations.add(0);
        }
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

        for(int i = 0; i < algorithmsToRun.size() ; i++)
        {
            for(int j = 0; j < normalizedIndicators.size() ; j++)
            {
                GenericIndicator<S> indicator                       = indicators.get(j);
                GenericIndicator<PointSolution> normalizedIndicator = normalizedIndicators.get(j);
                double referenceIndicator = normalizedIndicator.evaluate(FrontUtils.convertFrontToSolutionList(normalizedReferenceFront));
                referenceIndicators.get(i).add(referenceIndicator);
                evaluatedIndicators.get(i).put(indicator, 0.);
                evaluatedIndicators.get(i).put(normalizedIndicator, 0.);
            }

        }
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
        System.out.println(outputString);
        return outputString;
    }
}
