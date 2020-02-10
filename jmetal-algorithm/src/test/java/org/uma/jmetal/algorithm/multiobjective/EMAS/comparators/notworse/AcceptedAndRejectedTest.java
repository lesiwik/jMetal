package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.notworse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.emasdominance.EmasDominanceComparisonTest;
import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.AgentUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;

public class AcceptedAndRejectedTest {

    private JMetal5Agent<?> parent;
    private JMetal5Agent<?> offspringBelow;
    private JMetal5Agent<?> offspringAbove;
    private JMetal5Agent<?> offspringBetter;
    int COUNTER = 0;

    /**
     * Parent and its offspring must be non dominated by each other in order for NotWorseComparator to work.
     * */
    @Before
    public void setup()
    {
        COUNTER ++;
        String comparatorType = Constants.NOT_WORSE_COMPARATOR;
        String agentType      = Constants.BASE_AGENT;

        parent          = AgentUtils.createPointSolutionAgent(new double[]{0.5, 3.5}, comparatorType, agentType);
        offspringBelow  = AgentUtils.createPointSolutionAgent(new double[]{0.7, 3.0}, comparatorType, agentType);
        offspringAbove  = AgentUtils.createPointSolutionAgent(new double[]{1.3, 3.0}, comparatorType, agentType);
        offspringBetter = AgentUtils.createPointSolutionAgent(new double[]{0.2, 3.0}, comparatorType, agentType);
    }

    @Test
    public void dominationChecks()
    {
        EmasDominanceComparisonTest.compareAgentWithResult(parent, offspringBelow, Constants.NEITHER_IS_BETTER);
        EmasDominanceComparisonTest.compareAgentWithResult(parent, offspringAbove, Constants.NEITHER_IS_BETTER);
        EmasDominanceComparisonTest.compareAgentWithResult(parent, offspringBetter, Constants.SECOND_IS_BETTER);
    }

    /**
     * Offspring is below the line, therefore he should be accepted (result shown as its better than parent).
     * */
    @Test
    public void belowLineIsAccepted()
    {
        NotWorseComparisonTest.compareAgentWithResult(parent, offspringBelow, Constants.SECOND_IS_BETTER);
    }

    /**
     * Offspring is above the line, therefore he should be rejected (result shown as its worse than parent).
     * */
    @Test
    public void aboveLineIsRejected()
    {
        NotWorseComparisonTest.compareAgentWithResult(parent, offspringAbove, Constants.FIRST_IS_BETTER);
    }

    /**
     * Offspring is better than parent, so he should be shown as better.
     * */
    @Test
    public void clearlyBetterIsAccepted()
    {
        NotWorseComparisonTest.compareAgentWithResult(parent, offspringBetter, Constants.SECOND_IS_BETTER);
    }

    @After
    public void printResult()
    {
        System.out.println("\n== Test NotWorseAccepted has ended ==");
    }
}
