package org.uma.jmetal.algorithm.multiobjective.EMAS;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.lemas.agents.BaseAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.AlphaDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.agents.AgentBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import static org.junit.Assert.assertEquals;


public class AlphaDominanceComparatorTest {

    private AgentBuilder builder;

    @Before
    public void setup(){
        builder = new AgentBuilder();
    }

    @Test
    public void shouldDominateSolutionsWithEveryObjectiveWorse(){
        BaseAgent agent1 = agent(new double[]{10, 20});
        BaseAgent agent2 = agent(new double[]{1, 1});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 1);
    }

    @Test
    public void shouldDominateSolutionsWithOnlyOneWorseObjective(){
        BaseAgent agent1 = agent(new double[]{0.9, 20});
        BaseAgent agent2 = agent(new double[]{1, 1});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 1);
    }

    @Test
    public void shouldNotDominateExactSolution(){
        BaseAgent agent1 = agent(new double[]{1, 1});
        BaseAgent agent2 = agent(new double[]{1, 1});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 0);
    }

    @Test
    public void shouldNotDominateVeryCloseSolution(){
        BaseAgent agent1 = agent(new double[]{1.1, 1});
        BaseAgent agent2 = agent(new double[]{1, 1.11});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 0);
    }

    private BaseAgent agent(double[] objectivesValues){
        PointSolution solution = new PointSolution(new ArrayPoint(objectivesValues));
        return builder.withGenotype(solution).build();
    }
}
