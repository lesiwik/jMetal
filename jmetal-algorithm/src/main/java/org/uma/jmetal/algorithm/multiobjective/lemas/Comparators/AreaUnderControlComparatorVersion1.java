package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;
import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

@Setter
@Getter
public class AreaUnderControlComparatorVersion1<Agent extends JMetal5Agent<?>> extends EmasDominanceComparator<Agent>
{
        protected List<Agent> listOfKnownNonDominatedAgents;

        public AreaUnderControlComparatorVersion1() { listOfKnownNonDominatedAgents = new ArrayList<>(); }

        /**
         * @param agent1 agent to compare.
         * @param agent2 agent to compare.
         * @return result of comparison.
         * */
        @Override
        public int compare(Agent agent1, Agent agent2) {
                int isBetter = super.compare(agent1, agent2);
                if (isBetter == Constants.NEITHER_IS_BETTER) {
                        int firstDominateSecond = checkNonDominatedAgents(agent1, agent2);
                        int secondDominateFirst = checkNonDominatedAgents(agent2, agent1);

                        if(!(firstDominateSecond == secondDominateFirst)) {
                                if( firstDominateSecond == Constants.FIRST_IS_BETTER
                                        || secondDominateFirst == Constants.SECOND_IS_BETTER ){
                                        isBetter = Constants.FIRST_IS_BETTER;
                                }
                                else if ( firstDominateSecond == Constants.SECOND_IS_BETTER
                                || secondDominateFirst == Constants.FIRST_IS_BETTER){
                                        isBetter = Constants.SECOND_IS_BETTER;
                                }
                        }
                }

                if(isBetter == Constants.NEITHER_IS_BETTER){
                        updateListOfKnownNondominatedAgents(agent1, agent2);
                        int agent1Score = 0, agent2Score = 0;
                        for (int i = 0; i < agent1.genotype.getNumberOfObjectives(); i++) {
                                double objValue1 = agent1.genotype.getObjective(i),
                                        objValue2 = agent2.genotype.getObjective(i);
                                double diff, threshold, normalize;
                                       // = objValue2 - objValue1;
                                if(objValue1 > 0 && objValue2 > 0 ){
                                        diff = objValue2 - objValue1;
                                        threshold = diff > 0 ? objValue1/objValue2 : objValue2/objValue1;
                                }
                                else if(objValue1 < 0 && objValue2 < 0){
                                        diff = objValue2 - objValue1;
                                        threshold = diff > 0 ? objValue1/objValue2 : objValue2/objValue1;

                                }
                                else
                                {
                                        diff = objValue2 - objValue1;
                                        threshold = diff > 0 ? -objValue1/objValue2 : -objValue2/objValue1;
                                }
                                normalize = (abs(objValue1) + abs(objValue2))/abs(objValue1)*abs(objValue2);

                                if(normalize > 0.7 && normalize < 0.8){
                                        if(diff > 0)
                                                agent1Score++;
                                                // agent2 better function value
                                        else if(diff < 0)
                                                agent2Score++;
                                }
                        }

                        if(agent1Score > agent2Score){
                                isBetter = Constants.FIRST_IS_BETTER;
                        }
                        else if (agent1Score < agent2Score){
                                isBetter = Constants.SECOND_IS_BETTER;
                        }
                }

                return isBetter;
        }

        protected int checkNonDominatedAgents(Agent agent1, Agent agent2) {
                List<Agent> listOfKnownNonDominatedAgents =
                        ((AreaUnderControlComparatorVersion1) agent1.getComparator()).listOfKnownNonDominatedAgents;
                for (Agent agent:  listOfKnownNonDominatedAgents) {
                        int isPartnerUnderControl = super.compare(agent, agent2);
                        if (isPartnerUnderControl != Constants.NEITHER_IS_BETTER)
                                return isPartnerUnderControl;
                }
                return Constants.NEITHER_IS_BETTER;
        }

        protected void updateListOfKnownNondominatedAgents(Agent agent1, Agent agent2) {
                List<Agent> agent1ListOfNonDominatedAgents =
                        ((AreaUnderControlComparatorVersion1) agent1.getComparator()).listOfKnownNonDominatedAgents;
                if (!agent1ListOfNonDominatedAgents.contains(agent2)) {
                        boolean agent2IsDominated = checkifAgentIsDominated(agent1ListOfNonDominatedAgents, agent2);
                        if(agent2IsDominated)
                                agent1ListOfNonDominatedAgents.add(agent2);
                }

                List<Agent> agent2ListOfNonDominatedAgents =
                        ((AreaUnderControlComparatorVersion1) agent2.getComparator()).listOfKnownNonDominatedAgents;
                if (!agent2ListOfNonDominatedAgents.contains(agent1)) {
                        boolean agent1IsDominated = checkifAgentIsDominated(agent2ListOfNonDominatedAgents, agent1);
                        if(agent1IsDominated)
                                agent2ListOfNonDominatedAgents.add(agent1);
                }
        }

        private boolean checkifAgentIsDominated(List<Agent> listOfKnownNonDominatedAgents, Agent agent2)
        {
                int comparison = 0;
                List<Agent> listOfDominatedAgents = new ArrayList();
                for (Agent agent : listOfKnownNonDominatedAgents) {
                        comparison = super.compare(agent, agent2);

                        if(comparison == Constants.FIRST_IS_BETTER) {
                                return false;
                        }
                        else if (comparison == Constants.SECOND_IS_BETTER){
                                listOfDominatedAgents.add(agent2);
                        }
                }
                listOfKnownNonDominatedAgents.removeAll(listOfDominatedAgents);
                return true;
        }
}