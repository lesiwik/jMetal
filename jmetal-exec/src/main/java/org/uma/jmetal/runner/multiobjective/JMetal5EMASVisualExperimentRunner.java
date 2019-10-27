package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.EMASBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.PausableChartWrapper;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.measure.Measurable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.*;

public class JMetal5EMASVisualExperimentRunner extends AbstractAlgorithmRunner {

    private static Boolean isDebugMode = false;

     public static void main(String[] args) throws JMetalException {

        List<Algorithm> algorithmsToRun = new ArrayList<>();
        if (args.length > 0 && args[0].equals("-debug"))
            isDebugMode = true;

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

    public static JMetal5BaseEMAS createBaseEMAS(String name, int whenAddOffspring)
    {
        return new EMASBuilder<>()
                .emasType(BASE_EMAS)
                .algorithmName(name)
                .allowKnowledgeExchange(false)
                .whenAddOffspringToPopulation(whenAddOffspring)
                .comparator(EMAS_DOMINANCE_COMPARATOR)
                .parentToChildComparator(EMAS_DOMINANCE_COMPARATOR)
                .build();
    }

    public static void addAreaUnderControlBaseEMASes(List<Algorithm> algorithms)
    {
        algorithms.addAll(
                Arrays.asList(
//                        createAreaControlBaseEMAS("Base[Area] - If better", IF_BETTER),
//                        createAreaControlBaseEMAS("Base[Area] - If not worse", IF_NOT_WORSE),
                        createAreaControlBaseEMAS("Base[Area] - Always", ALWAYS)
                ));
    }

    public static JMetal5BaseEMAS createAreaControlBaseEMAS(String name, int whenAddOffspring)
    {
        return new EMASBuilder<>()
                .emasType(BASE_EMAS)
                .agentType(BASE_AGENT)
                .algorithmName(name)
                .allowKnowledgeExchange(false)
                .whenAddOffspringToPopulation(whenAddOffspring)
                .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                .parentToChildComparator(AREA_UNDER_CONTROL_COMPARATOR)
                .build();
    }
}
