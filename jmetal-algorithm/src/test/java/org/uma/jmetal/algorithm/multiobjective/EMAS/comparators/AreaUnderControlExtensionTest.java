package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5AgentBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Checks to see if merge using extended {@link org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator} was as expected.
 * Assumptions that are also checked:
 * 1. A1 and A2 are not dominating mutually ( with {@link org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator}.
 * (NOTE: It means that they are also not dominated with {@link org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator}.
 * 2. List of known non dominated Agents of either agents contain only mutually non dominated agents - meaning each agent in said list is not
 * dominated by either owner of said list and any agent in that list but CAN be dominated by any other agent that is outside of this scope.
 * (eg. A1 has agent A1_1 in his list and A1_1 gets dominated by A2 or any agent in A2s list).
 * 3. Final merge should:..
 * */
public class AreaUnderControlExtensionTest{

    private JMetal5AgentBuilder<PointSolution> builder;
    private JMetal5Agent<PointSolution> agent1;
    private JMetal5Agent<PointSolution> agent2;
    private ArrayList<JMetal5Agent<PointSolution>> a1_list;
    private ArrayList<JMetal5Agent<PointSolution>> a2_list;
    private ArrayList<JMetal5Agent<PointSolution>> expectedMergedList; /* TODO: Implemented and decide how it should look like. */

    /**
     * Creates agent with specific genotypes to perform merge on.
     * */
    @Before
    public void setup()
    {
        // A1 = [0.5 , 0.5] with list = [0.58, 0.47] + [0.65, 0.38]  LUB [0.52, 0.47] + [0.65, 0.38]
        // A2 = [0.54, 0.45] with list = [0.61, 0.42] + [0.69, 0.4]
        builder = new JMetal5AgentBuilder<>();
        a1_list = new ArrayList<>();
        a2_list = new ArrayList<>();

        agent1 = agent(new double[]{0.5, 0.5});
        AreaUnderControlComparator<JMetal5Agent<PointSolution>> a1Comparator = (AreaUnderControlComparator) agent1.getComparator();
        JMetal5Agent<PointSolution> a1_1 = agent(new double[]{0.52, 0.47});
        JMetal5Agent<PointSolution> a1_2 = agent(new double[]{0.65, 0.38});
        a1_list.add(a1_1);
        a1_list.add(a1_2);
        a1Comparator.setListOfKnownNonDominatedAgents(a1_list);

        agent2 = agent(new double[]{0.54, 0.45});
        AreaUnderControlComparator<JMetal5Agent<PointSolution>> a2Comparator = (AreaUnderControlComparator) agent2.getComparator();
        JMetal5Agent<PointSolution> a2_1 = agent(new double[]{0.61, 0.42});
        JMetal5Agent<PointSolution> a2_2 = agent(new double[]{0.69, 0.4});
        a2_list.add(a2_1);
        a2_list.add(a2_2);
        a2Comparator.setListOfKnownNonDominatedAgents(a2_list);


    }

    private JMetal5Agent<PointSolution> agent(double[] objectivesValues){
        PointSolution solution = new PointSolution(objectivesValues.length);
        for(int i = 0 ; i < objectivesValues.length; i++)
        {
            solution.setObjective(i, objectivesValues[i]);
        }
        return builder.withGenotype(solution)
                .withComparator(Constants.AREA_UNDER_CONTROL_COMPARATOR)
                .build();
    }

    @Test
    public void mergeLists()
    {

        /*TODO: Implemented area under control extended test */

    }

    @Test
    public void emasDominanceTest()
    {
        EmasDominanceComparator comparator = new EmasDominanceComparator();
        int comparatorResult = comparator.compare(agent1, agent2);
        assertEquals(Constants.NEITHER_IS_BETTER, comparatorResult);
    }

    @Test
    public void areaUnderControlTest()
    {
        AreaUnderControlComparator comparator = (AreaUnderControlComparator) agent1.getComparator();
        int comparatorResult = comparator.compare(agent1, agent2);
        assertEquals(Constants.NEITHER_IS_BETTER, comparatorResult);
    }

    @After
    public void printResults()
    {
    }
}
