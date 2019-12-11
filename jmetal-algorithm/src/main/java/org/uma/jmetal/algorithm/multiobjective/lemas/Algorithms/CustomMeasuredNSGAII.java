package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.component.ranking.Ranking;
import org.uma.jmetal.component.termination.Termination;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.measure.Measurable;
import org.uma.jmetal.util.measure.MeasureManager;
import org.uma.jmetal.util.measure.impl.BasicMeasure;
import org.uma.jmetal.util.measure.impl.SimpleMeasureManager;

import java.util.Comparator;
import java.util.List;

public class CustomMeasuredNSGAII<S extends Solution<?>> extends NSGAII<S> implements Measurable {
    private SimpleMeasureManager measureManager = new SimpleMeasureManager();
    private BasicMeasure<Object> solutionListMeasure;
    public int iterations;

    /* Measures code */
    private void initMeasures() {
        solutionListMeasure = new BasicMeasure<>() ;
        measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected void updateProgress() {
        iterations++;
        solutionListMeasure.push(getPopulation());
    }

    protected void updateProgress(int iteration) {
        iterations++;
        solutionListMeasure.push(getPopulation());
    }

    @Override
    protected void initProgress() {
        iterations = 0;

    }

    public CustomMeasuredNSGAII(Problem<S> problem,
                                int populationSize,
                                int offspringPopulationSize,
                                CrossoverOperator<S> crossoverOperator,
                                MutationOperator<S> mutationOperator,
                                Termination termination) {
          super(problem, populationSize, offspringPopulationSize, crossoverOperator, mutationOperator, termination );
//        referenceFront = new ArrayFront() ;

        initMeasures() ;
    }

    public CustomMeasuredNSGAII(Problem<S> problem, int populationSize,
                                int offspringPopulationSize,
                                CrossoverOperator<S> crossoverOperator,
                                MutationOperator<S> mutationOperator,
                                Termination termination,
                                Ranking<S> ranking) {
        super(problem, populationSize, offspringPopulationSize, crossoverOperator, mutationOperator, termination, ranking);
        initMeasures();
    }


    @Override
    public MeasureManager getMeasureManager() {
        return measureManager;
    }
}
