package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

/**
 * Progressive agent class derived from {@link JMetal5Agent}. Main idea is to have agent which has lower productivity requirement which means it will reproduce more than base {@link JMetal5Agent}.
 * @see Constants#PROGRESSIVE_REPRODUCTION_LEVEL_VALUE
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 9/02/2018
 * */
public class JMetal5ProgressiveAgent<S extends Solution<?>> extends JMetal5Agent {


    public JMetal5ProgressiveAgent(int id)
    {
        super(id);
        this.setReproCondition(() -> getResourceLevel() >= Constants.PROGRESSIVE_REPRODUCTION_LEVEL_VALUE);
    }


    public JMetal5ProgressiveAgent() {
        this.setReproCondition(() -> getResourceLevel() >= Constants.PROGRESSIVE_REPRODUCTION_LEVEL_VALUE);
    }

    @Override
    public String getAgentType() {
        return Constants.PROGRESSIVE_AGENT;
    }
}
