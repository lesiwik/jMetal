package org.uma.jmetal.runner.multiobjective.rewera;/*
package org.uma.jmetal.runner.multiobjective.rewera;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.ProgressiveEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.AreaUnderControlDistanceToClosesNeighbourComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.multiobjective.rewera.visualization.MultiTabFrame;
import org.uma.jmetal.runner.multiobjective.rewera.visualization.SingleProblemChartWrapper;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;

import java.util.*;

import static org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants.ALWAYS;
import static org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants.IF_BETTER;

public class JMetal5EMASMultiProblemExperimentVisualRunner extends AbstractAlgorithmRunner {

    public static void main(String[] args) throws JMetalException {
        JMetal5EMASMultiProblemExperimentVisualRunner runner = new JMetal5EMASMultiProblemExperimentVisualRunner();
        runner.run();
    }

    private void run() {
        List<Problem<DoubleSolution>> problemsToSolve = Arrays.asList(
                Constants.PROBLEM_ZDT4.getProblem(),
                Constants.PROBLEM_ZDT3.getProblem(),
                Constants.PROBLEM_ZDT2.getProblem(),
                Constants.PROBLEM.getProblem());

        Map<String, List<Algorithm>> algorithmsToRun = createAlgorithmsMap(problemsToSolve);
        MultiTabFrame mainFrame = new MultiTabFrame(algorithmsToRun, Constants.NUMBER_OF_DECISION_VARIABLES_TO_SHOW);


        registerAllCharts(algorithmsToRun, mainFrame);
        executeAlgorithmsInMapOrder(algorithmsToRun);
    }

    private Map<String, List<Algorithm>> createAlgorithmsMap(List<Problem<DoubleSolution>> problems) {
        Map<String, List<Algorithm>> algorithms = new TreeMap<>();
        for (Problem<DoubleSolution> problem : problems) {
            algorithms.put(problem.getName(), createAlgorithmsFor(problem));
        }
        return algorithms;
    }

    private List<Algorithm> createAlgorithmsFor(Problem<DoubleSolution> problem) {
        List<Algorithm> algorithms = new ArrayList<>();
        algorithms.add(new BaseEMAS<>(problem,  "BaseEMAS",
                ALWAYS, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new EmasDominanceComparator()));

        algorithms.add(new BaseEMAS<>(problem, "AreaUnderControl",
                ALWAYS, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new AreaUnderControlComparator()));

        algorithms.add(new ProgressiveEMAS<>(problem,
                "ProgresiveAreaUnderControl",
                ALWAYS, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new AreaUnderControlComparator()));

        algorithms.add(new ProgressiveEMAS<>(problem,
                "ProgresiveEliteAreaUnderControl",
                IF_BETTER, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new AreaUnderControlComparator()));

        return algorithms;
    }

    private void registerAllCharts(Map<String, List<Algorithm>> algorithms, MultiTabFrame mainFrame) {
        for (Map.Entry<String, List<Algorithm>> entry : algorithms.entrySet()) {
            String currentProblem = entry.getKey();
            List<Algorithm> currentAlgorithms = entry.getValue();
            SingleProblemChartWrapper<?> currentWrapper = mainFrame.getWrapper(currentProblem);
            registerChart(currentAlgorithms, currentWrapper);
        }
    }

    private void registerChart(List<Algorithm> algorithms, SingleProblemChartWrapper<?> wrapper) {
        algorithms.forEach(algorithm -> {
            ((Measurable) algorithm).getMeasureManager()
                    .getPushMeasure("currentPopulation")
                    .register(population ->
                            wrapper.updateChart((List<DoubleSolution>) population, algorithm.getName(), (BaseEMAS<?>) algorithm, algorithms.indexOf(algorithm)));
        });
    }

    private void executeAlgorithmsInMapOrder(Map<?, List<Algorithm>> algorithms) {
        for (List<Algorithm> currentAlgorithms : algorithms.values())
            currentAlgorithms.parallelStream().forEach(algorithm ->
                new AlgorithmRunner.Executor(algorithm)
                        .execute());
    }

    private void executeAlgorithmsInRandomOrder(Map<?, List<Algorithm>> algorithms) {
        algorithms.values().parallelStream().forEach(list ->
            list.parallelStream().forEach(algorithm ->
                new AlgorithmRunner.Executor(algorithm)
                    .execute()));
    }
}
*/
