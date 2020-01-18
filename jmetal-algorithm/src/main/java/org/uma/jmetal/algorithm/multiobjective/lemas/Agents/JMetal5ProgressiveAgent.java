package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Progressive agent class derived from {@link JMetal5Agent}. Main idea is to have agent which has lower productivity requirement which means it will reproduce more than base {@link JMetal5Agent}.
 * @see Constants#PROGRESSIVE_REPRODUCTION_LEVEL_VALUE
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 9/02/2018
 * */
public class JMetal5ProgressiveAgent<S extends Solution<?>> extends JMetal5Agent<S> {


    public JMetal5ProgressiveAgent(int id)
    {
        super(id);
    }


    public JMetal5ProgressiveAgent() {
    }

    @Override
    public List<JMetal5Agent<S>> reproAct(int numberOfChildren, List<? extends JMetal5Agent<S>> parentList, List<? extends JMetal5Agent<S>> offspringList) {
            List<JMetal5Agent<S>> listOfOffspringToBeReturned = new ArrayList<>();

            for (int index = 0; index < numberOfChildren; index++) {
                JMetal5Agent<S> offSpring = offspringList.get(index);
                JMetal5Agent<S> parent    = parentList.get(index); // TODO: Shouldn't it compare to ALL parents instead of one?
                offSpring.mutate();
                offSpring.evaluate(EMAS.getProblem());

                int isOffspringBetter = 0;
                if (EMAS.getWhenAddOffspringToPopulation() != Constants.ALWAYS) {
                    isOffspringBetter = getParentToChildComparator().compare(parent, offSpring);
                }


                boolean isOffspringGoodEnough = false;
                switch(EMAS.getWhenAddOffspringToPopulation())
                {

                    case Constants.IF_NOT_WORSE:
                        if(isOffspringBetter == Constants.NEITHER_IS_BETTER || isOffspringBetter == Constants.IS_BETTER) {
                            isOffspringGoodEnough = true;
                        }
                        break;

                    case Constants.IF_BETTER_AND_COULD_NOT_KNOW:
                    case Constants.IF_BETTER:
                        if(isOffspringBetter == Constants.IS_BETTER)
                            isOffspringGoodEnough = true;
                        break;

                    case Constants.ALWAYS:
                    default:
                        isOffspringGoodEnough = true;
                        break;
                }

                if(isOffspringGoodEnough) {
                    offSpring.transferResourcesFrom(parent, EMAS.getInitialAgentResourceLevel());
                    listOfOffspringToBeReturned.add(offSpring);
                    parent.hasAlreadyReproduced = true;
                }
            }
            return listOfOffspringToBeReturned;
    }

    @Override
    public String getAgentType() {
        return Constants.PROGRESSIVE_AGENT;
    }
}
