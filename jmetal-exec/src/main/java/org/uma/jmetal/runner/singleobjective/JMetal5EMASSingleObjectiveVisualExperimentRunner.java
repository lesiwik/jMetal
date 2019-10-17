package org.uma.jmetal.runner.singleobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasSingleObjectiveComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.SingleObjectiveChartWrapper;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.measure.Measurable;

import java.util.ArrayList;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.IF_BETTER;

public class JMetal5EMASSingleObjectiveVisualExperimentRunner extends AbstractAlgorithmRunner {

    public static void main(String[] args) throws JMetalException {

        List<Algorithm> algorithmsToRun = new ArrayList<>();

        algorithmsToRun.add(new JMetal5BaseEMAS<>("BaseEMAS",
                IF_BETTER, false,
                new EmasSingleObjectiveComparator(), new EmasSingleObjectiveComparator()));


        SingleObjectiveChartWrapper chartWrapper = new SingleObjectiveChartWrapper(algorithmsToRun, Constants.NUMBER_OF_DECISION_VARIABLES_TO_SHOW);
        algorithmsToRun.forEach(algorithm -> ((Measurable) algorithm).getMeasureManager()
                .getPushMeasure("currentPopulation")
                .register(population ->
                        chartWrapper.updateChart((List<DoubleSolution>) population, algorithm.getName(), (JMetal5BaseEMAS) algorithm, algorithmsToRun.indexOf(algorithm))));

        algorithmsToRun
                .parallelStream()
                .forEach(algorithm -> new AlgorithmRunner.Executor(algorithm)
                        .execute());


    }
}
