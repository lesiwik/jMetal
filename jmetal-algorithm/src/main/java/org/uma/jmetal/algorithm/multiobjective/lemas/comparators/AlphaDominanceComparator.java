package org.uma.jmetal.algorithm.multiobjective.lemas.comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.BaseAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;


public class AlphaDominanceComparator<S extends BaseAgent<?>> extends EmasDominanceComparator<S> {

    private double[][] alphaValues;

    public AlphaDominanceComparator(double[][] alphaValues){
        this.alphaValues = alphaValues;
    }

    @Override
    public int compare(BaseAgent a1, BaseAgent a2) {
        Solution modifiedSolution1 = a1.getGenotype().copy();
        Solution modifiedSolution2 = a2.getGenotype().copy();

        int objectivesQty = Constants.PROBLEM.getProblem().getNumberOfObjectives();

        for (int i = 0; i < objectivesQty; i++) {
            for (int j = 0; j < objectivesQty; j++) {
                double modifiedObjectivePartA1 = 0;
                double modifiedObjectivePartA2 = 0;
                if (i != j) {
                    modifiedObjectivePartA1 += alphaValues[i][j] * a1.getGenotype().getObjective(j);
                    modifiedObjectivePartA2 += alphaValues[i][j] * a2.getGenotype().getObjective(j);
                }

                modifiedSolution1.setObjective(i, modifiedSolution1.getObjective(i) + modifiedObjectivePartA1);
                modifiedSolution2.setObjective(i, modifiedSolution2.getObjective(i) + modifiedObjectivePartA2);
            }
        }

        return new DominanceComparator().compare(modifiedSolution1, modifiedSolution2);
    }
}
