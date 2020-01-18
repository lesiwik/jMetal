package org.uma.jmetal.algorithm.multiobjective.EMAS.utils;

import org.uma.jmetal.problem.Problem;


/**
 * Dummy problem class which is supposed to do nothing but provide problem as to avoid NullPointers and not change Solution, while being able to proceed with the test.
 * Made specifically for {@link org.uma.jmetal.algorithm.multiobjective.EMAS.agents.JMetal5ProgressiveAgentTest}.
 * */
public class NullProblem<S> implements Problem<S> {


    @Override
    public int getNumberOfVariables() {
        return -1;
    }

    @Override
    public int getNumberOfObjectives() {
        return -1;
    }

    @Override
    public int getNumberOfConstraints() {
        return -1;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void evaluate(S solution) {

    }

    @Override
    public S createSolution() {
        return null;
    }
}
