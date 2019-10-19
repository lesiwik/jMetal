package org.uma.jmetal.algorithm.multiobjective.lemas.agents;

import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.solution.Solution;

/**
 * Progressive agent class derived from {@link BaseAgent}. Main idea is to have agent which has lower productivity requirement which means it will reproduce more than base {@link BaseAgent}.
 * @see Constants#PROGRESSIVE_REPRODUCTION_LEVEL_VALUE
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 9/02/2018
 * */
public class ProgressiveAgent<S extends Solution<?>> extends BaseAgent {


    public ProgressiveAgent(int id)
    {
        super(id);
        this.setReproCondition(() -> getResourceLevel() >= Constants.PROGRESSIVE_REPRODUCTION_LEVEL_VALUE);
    }


    public ProgressiveAgent() {
        this.setReproCondition(() -> getResourceLevel() >= Constants.PROGRESSIVE_REPRODUCTION_LEVEL_VALUE);
    }

    @Override
    public String getAgentType() {
        return "ProgressiveAgent";
    }
}
