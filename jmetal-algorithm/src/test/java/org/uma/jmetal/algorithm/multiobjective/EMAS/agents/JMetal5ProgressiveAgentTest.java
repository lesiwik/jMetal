package org.uma.jmetal.algorithm.multiobjective.EMAS.agents;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.AgentUtils;
import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.NullProblem;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ProgressiveAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.EMASBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.point.PointSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JMetal5ProgressiveAgentTest {


    private List<JMetal5ProgressiveAgent<PointSolution>> parentList;
    private JMetal5ProgressiveAgent<PointSolution> parent1;
    private JMetal5ProgressiveAgent<PointSolution> parent2;

    private List<JMetal5ProgressiveAgent<PointSolution>> betterOffspringsList;
    private List<JMetal5ProgressiveAgent<PointSolution>> worseOffspringsList;

    /**
     * Worse offspring than Parent 1 only.
     * */
    private JMetal5ProgressiveAgent<PointSolution> worseOffspringP1;

    /**
     * Worse offspring than both parents.
     * */
    private JMetal5ProgressiveAgent<PointSolution> worseOffspring;


    /**
     * Better offspring than Parent 2 only.
     * */
    private JMetal5ProgressiveAgent<PointSolution> betterOffspringP2;

    /**
     * Better offspring than both parents.
     * */
    private JMetal5ProgressiveAgent<PointSolution> betterOffspring;

    private final String COMPARATOR_TYPE = Constants.EMAS_DOMINANCE_COMPARATOR;
    private final String AGENT_TYPE = Constants.PROGRESSIVE_AGENT;
    private final int WHEN_TO_ADD_OFFSPRING = Constants.IF_BETTER;
    private final Problem<PointSolution> DUMMY_PROBLEM = new NullProblem<>();
    private final JMetal5BaseEMAS<PointSolution> EMAS = new EMASBuilder<PointSolution>()
            .whenAddOffspringToPopulation(WHEN_TO_ADD_OFFSPRING)
            .problem(DUMMY_PROBLEM)
            .build();

    @Before
    public void setup()
    {
        parent1 = (JMetal5ProgressiveAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{1, 3}, COMPARATOR_TYPE, AGENT_TYPE, COMPARATOR_TYPE);
        parent2 = (JMetal5ProgressiveAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{2, 2}, COMPARATOR_TYPE, AGENT_TYPE, COMPARATOR_TYPE);

        betterOffspringP2 = (JMetal5ProgressiveAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{1.5, 1.5}, COMPARATOR_TYPE, AGENT_TYPE, COMPARATOR_TYPE);
        worseOffspringP1 = (JMetal5ProgressiveAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{1.5, 3.5}, COMPARATOR_TYPE, AGENT_TYPE, COMPARATOR_TYPE);

        betterOffspring = (JMetal5ProgressiveAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{0.5, 1}, COMPARATOR_TYPE, AGENT_TYPE, COMPARATOR_TYPE);
        worseOffspring = (JMetal5ProgressiveAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{3, 4}, COMPARATOR_TYPE, AGENT_TYPE, COMPARATOR_TYPE);
        parentList = new ArrayList<>(Arrays.asList(parent1, parent2));

        betterOffspringsList = new ArrayList<>(Arrays.asList(betterOffspring, betterOffspringP2));
        worseOffspringsList = new ArrayList<>(Arrays.asList(worseOffspring, worseOffspringP1));

        parentList.forEach(agent -> agent.setEMAS(EMAS));
        betterOffspringsList.forEach(agent -> agent.setEMAS(EMAS));
        worseOffspringsList.forEach(agent -> agent.setEMAS(EMAS));
    }

    /**
     * Checks if {@link JMetal5ProgressiveAgent#reproAct(int, List, List)} returns list of correct agents (which includes better offspring).
     * */
    @Test
    public void betterOffspringIsReturned()
    {
        int numberOfChildren = 2;
        List<JMetal5Agent<PointSolution>> expectedList = new ArrayList<>();
        expectedList.add(betterOffspring); /* TODO: Should it really be only this one? This test is supposed to fail before that is resolved. */

        System.out.println("\n== Checking if better offspring is returned from the list == \n ");
        List<JMetal5Agent<PointSolution>> resultedList = parent1.reproAct(numberOfChildren, parentList, betterOffspringsList);
        System.out.println("\n== Checking if lists are equal. == \nExpected list: " + expectedList
                + "\nActual result list:" + resultedList);

        assertEquals(expectedList, resultedList);
    }

    /**
     * Checks if {@link JMetal5ProgressiveAgent#reproAct(int, List, List)} returns list of correct agents (which doesn't include worse offspring).
     * */
    @Test
    public void worseOffspringIsRejected()
    {
        int numberOfChildren = 2;
        List<JMetal5Agent<PointSolution>> expectedList = new ArrayList<>();

        System.out.println("\n== Checking if worse offspring is NOT returned from the list == \n ");
        List<JMetal5Agent<PointSolution>> resultedList = parent1.reproAct(numberOfChildren, parentList, worseOffspringsList);
        System.out.println("\n== Checking if lists are equal. == \nExpected list: " + expectedList
                + "\nActual result list:" + resultedList);

        assertEquals(expectedList, resultedList);
    }


    @After
    public void printResults()
    {
        System.out.println("\n == JMetalProgressiveAgent tests has ended. ==");
    }
}
