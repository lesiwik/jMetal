package org.uma.jmetal.algorithm.multiobjective.EMAS;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AlphaDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5AgentBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import static org.junit.Assert.assertEquals;


public class AlphaDominanceComparatorTest {

    private JMetal5AgentBuilder builder;

    @Before
    public void setup(){
        builder = new JMetal5AgentBuilder();
    }

    @Test
    public void shouldDominateSolutionsWithEveryObjectiveWorse(){
        JMetal5Agent agent1 = agent(new double[]{10, 20});
        JMetal5Agent agent2 = agent(new double[]{1, 1});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 1);
    }

    @Test
    public void shouldDominateSolutionsWithOnlyOneWorseObjective(){
        JMetal5Agent agent1 = agent(new double[]{0.9, 20});
        JMetal5Agent agent2 = agent(new double[]{1, 1});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 1);
    }

    @Test
    public void shouldNotDominateExactSolution(){
        JMetal5Agent agent1 = agent(new double[]{1, 1});
        JMetal5Agent agent2 = agent(new double[]{1, 1});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 0);
    }

    @Test
    public void shouldNotDominateVeryCloseSolution(){
        JMetal5Agent agent1 = agent(new double[]{1.1, 1});
        JMetal5Agent agent2 = agent(new double[]{1, 1.11});

        AlphaDominanceComparator comparator = new AlphaDominanceComparator(Constants.ALPHA_VALUES);

        int compareResult = comparator.compare(agent1, agent2);

        assertEquals(compareResult, 0);
    }

    private JMetal5Agent agent(double[] objectivesValues){
        PointSolution solution = new PointSolution(new ArrayPoint(objectivesValues));
        return builder.withGenotype(solution).build();
    }
}
