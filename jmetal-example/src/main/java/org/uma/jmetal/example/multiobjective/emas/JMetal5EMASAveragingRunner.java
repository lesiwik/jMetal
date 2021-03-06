package org.uma.jmetal.example.multiobjective.emas;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.AlgorithmFactory;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.TimeFormatter;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.impl.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;
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

    private static final int NUMBER_OF_RUNS = 30;
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

        File csvFolders = new File(System.getProperty("user.dir") + "/emas_results/average/");
        boolean isPathValid = csvFolders.mkdirs();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("MM-dd - HH;mm");
        df.setTimeZone(tz);
        String fileName = df.format(new Date()) + ".csv";
        String absolutePath = csvFolders + "\\" + fileName;
        try (FileWriter fileWriter = new FileWriter(absolutePath)) {
            fileWriter.write(csvFormatData.toString());
            System.out.println("\n\nCSV File created at: " + absolutePath);
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
                System.out.println("[" + emasRef.getName() + "] [Run " + (i+1) + "/" + NUMBER_OF_RUNS + "] Total execution time: " + TimeFormatter.msToTime(computingTime));
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
        algorithmsToRun = AlgorithmFactory.getAlgorithms();

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
