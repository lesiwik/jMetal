package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.TimeFormatter;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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

    private List<List<String>> listOfResults;

    private JMetal5EMASAveragingRunner()
    {
        listOfResults        = new ArrayList<>();
        indicators           = new ArrayList<>();
        normalizedIndicators = new ArrayList<>();
        evaluations          = new CopyOnWriteArrayList<>();
        referenceIndicators  = new ArrayList<>();
        evaluatedIndicators  = new CopyOnWriteArrayList<>();
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
        runner.saveToFile();
    }

    private void saveToFile(){
        StringBuilder csvFormatData = new StringBuilder();
        for(int lineIndex = 0; lineIndex < listOfResults.size() ; lineIndex++)
        {
            for (int wordIndex = 0 ; wordIndex < listOfResults.get(lineIndex).size() ; wordIndex++)
            {
                String word = listOfResults.get(lineIndex).get(wordIndex);
                csvFormatData.append(word);
                if (wordIndex < listOfResults.get(lineIndex).size() - 1)
                    csvFormatData.append(',');
            }
            csvFormatData.append('\n');
        }

//        System.out.println("\n\n\n\nCSV File:\n"+ csvFormatData.toString());

        File csvFolders = new File(System.getProperty("user.dir") + "/emas_results/average/");
        boolean isPathValid = csvFolders.mkdirs();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("MM-dd - HH;mm");
        df.setTimeZone(tz);
        String fileName = "/" + df.format(new Date()) + ".csv";
        try (FileWriter fileWriter = new FileWriter(csvFolders + fileName)) {
            fileWriter.write(csvFormatData.toString());
            System.out.println("\n\nCSV File created at: " + csvFolders + fileName);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveResults()
    {
        List<String> indicatorHeaders = indicators.stream().map(GenericIndicator::getName).collect(Collectors.toList());
        indicatorHeaders.addAll(normalizedIndicators.stream().map(GenericIndicator::toString).map(string -> string = string + "(R+N)").collect(Collectors.toList()));
        indicatorHeaders.add(0, "Name");
        indicatorHeaders.add("Evaluations");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        printDataRow(indicatorHeaders);
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        listOfResults.add(indicatorHeaders);
        for(int algorithmIndex = 0 ; algorithmIndex < algorithmsToRun.size() ; algorithmIndex++)
        {
            List<String> results = new ArrayList<>(indicatorHeaders.size() + 2);
            JMetal5BaseEMAS<Solution<?>> emasAlgorithm = (JMetal5BaseEMAS<Solution<?>>) algorithmsToRun.get(algorithmIndex);
            Map<GenericIndicator<?>, Double> algorithmIndicators = evaluatedIndicators.get(algorithmIndex);
            Integer numberOfEvaluations = evaluations.get(algorithmIndex);
            results.add(emasAlgorithm.getName());
            indicators.forEach(indicator -> {
                double indicatorValue = algorithmIndicators.get(indicator);
                results.add(String.format("%.5f", indicatorValue).replace(",", "."));
            });
            normalizedIndicators.forEach(normalizedIndicator -> {
                double indicatorValue = algorithmIndicators.get(normalizedIndicator);
                results.add(String.format("%.5f", indicatorValue).replace(",", "."));
            });
            results.add(numberOfEvaluations.toString());
            listOfResults.add(results);
            printDataRow(results);
        }
    }

    private void printDataRow(List<String> data)
    {
        String format = new String(new char[data.size()]).replace("\0", "%20s");
        System.out.printf(format.trim(), data.toArray());
        System.out.println();
    }

    private void averageResults(){
        evaluatedIndicators = evaluatedIndicators.stream().map(indicatorMap -> {
            indicatorMap = indicatorMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue() / NUMBER_OF_RUNS));
            return indicatorMap;
        }).collect(Collectors.toList());

        evaluations = evaluations.parallelStream().map(value -> value / NUMBER_OF_RUNS).collect(Collectors.toList());
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
                System.out.println("[" + emasRef.getName() + "] [Run " + (i+1) + "/5] Total execution time: " + TimeFormatter.msToTime(computingTime));
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
}
