package org.uma.jmetal.example.multiobjective.emas.deprecated;/*
package org.uma.jmetal.runner.multiobjective.rewera;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5ProgressiveEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlDistanceToClosesNeighbourComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.multiobjective.rewera.Visualization.MultiTabFrame;
import org.uma.jmetal.runner.multiobjective.rewera.Visualization.SingleProblemChartWrapper;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;

import java.util.*;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.ALWAYS;
import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.IF_BETTER;

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
        Map<String, List<Algorithm>> Algorithms = new TreeMap<>();
        for (Problem<DoubleSolution> problem : problems) {
            Algorithms.put(problem.getName(), createAlgorithmsFor(problem));
        }
        return Algorithms;
    }

    private List<Algorithm> createAlgorithmsFor(Problem<DoubleSolution> problem) {
        List<Algorithm> Algorithms = new ArrayList<>();
        Algorithms.add(new JMetal5BaseEMAS<>(problem,  "BaseEMAS",
                ALWAYS, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new EmasDominanceComparator()));

        Algorithms.add(new JMetal5BaseEMAS<>(problem, "AreaUnderControl",
                ALWAYS, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new AreaUnderControlComparator()));

        Algorithms.add(new JMetal5ProgressiveEMAS<>(problem,
                "ProgresiveAreaUnderControl",
                ALWAYS, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new AreaUnderControlComparator()));

        Algorithms.add(new JMetal5ProgressiveEMAS<>(problem,
                "ProgresiveEliteAreaUnderControl",
                IF_BETTER, false,
                new AreaUnderControlDistanceToClosesNeighbourComparator(),
                new AreaUnderControlComparator()));

        return Algorithms;
    }

    private void registerAllCharts(Map<String, List<Algorithm>> Algorithms, MultiTabFrame mainFrame) {
        for (Map.Entry<String, List<Algorithm>> entry : Algorithms.entrySet()) {
            String currentProblem = entry.getKey();
            List<Algorithm> currentAlgorithms = entry.getValue();
            SingleProblemChartWrapper<?> currentWrapper = mainFrame.getWrapper(currentProblem);
            registerChart(currentAlgorithms, currentWrapper);
        }
    }

    private void registerChart(List<Algorithm> Algorithms, SingleProblemChartWrapper<?> wrapper) {
        Algorithms.forEach(algorithm -> {
            ((Measurable) algorithm).getMeasureManager()
                    .getPushMeasure("currentPopulation")
                    .register(population ->
                            wrapper.updateChart((List<DoubleSolution>) population, algorithm.getName(), (JMetal5BaseEMAS<?>) algorithm, Algorithms.indexOf(algorithm)));
        });
    }

    private void executeAlgorithmsInMapOrder(Map<?, List<Algorithm>> Algorithms) {
        for (List<Algorithm> currentAlgorithms : Algorithms.values())
            currentAlgorithms.parallelStream().forEach(algorithm ->
                new AlgorithmRunner.Executor(algorithm)
                        .execute());
    }

    private void executeAlgorithmsInRandomOrder(Map<?, List<Algorithm>> Algorithms) {
        Algorithms.values().parallelStream().forEach(list ->
            list.parallelStream().forEach(algorithm ->
                new AlgorithmRunner.Executor(algorithm)
                    .execute()));
    }
}
*/
