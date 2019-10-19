package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.EMASBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.visualization.PausableChartWrapper;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.measure.Measurable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants.*;

public class JMetal5EMASVisualExperimentRunner extends AbstractAlgorithmRunner {

     public static void main(String[] args) throws JMetalException {

        List<Algorithm> algorithmsToRun = new ArrayList<>();

        //addBaseEMASes(algorithmsToRun);
        addAreaUnderControlBaseEMASes(algorithmsToRun);

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

    public static void addBaseEMASes(List<Algorithm> algorithms)
    {
        algorithms.addAll(
                Arrays.asList(
                    createBaseEMAS("Base - If better", IF_BETTER),
                    createBaseEMAS("Base - If not worse", IF_NOT_WORSE),
                    createBaseEMAS("Base - Always", ALWAYS)
        ));
    }

    public static BaseEMAS createBaseEMAS(String name, int whenAddOffspring)
    {
        return new EMASBuilder<>()
                .emasType("Base")
                .algorithmName(name)
                .allowKnowledgeExchange(false)
                .whenAddOffspringToPopulation(whenAddOffspring)
                .comparator(new EmasDominanceComparator())
                .parentToChildComparator(new EmasDominanceComparator())
                .build();
    }

    public static void addAreaUnderControlBaseEMASes(List<Algorithm> algorithms)
    {
        algorithms.addAll(
                Arrays.asList(
                        //createAreaControlBaseEMAS("Base[Area] - If better", IF_BETTER),
                        createAreaControlBaseEMAS("Base[Area] - If not worse", IF_NOT_WORSE)
                        //createAreaControlBaseEMAS("Base[Area] - Always", ALWAYS)
                ));
    }

    public static BaseEMAS createAreaControlBaseEMAS(String name, int whenAddOffspring)
    {
        return new EMASBuilder<>()
                .emasType("Base")
                .agentType("AreaControlAgent")
                .algorithmName(name)
                .allowKnowledgeExchange(false)
                .whenAddOffspringToPopulation(whenAddOffspring)
                .comparator(new AreaUnderControlComparator())
                .parentToChildComparator(new AreaUnderControlComparator())
                .build();
    }
}
