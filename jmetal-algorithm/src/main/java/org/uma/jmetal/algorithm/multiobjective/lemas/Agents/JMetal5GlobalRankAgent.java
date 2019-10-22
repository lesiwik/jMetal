package org.uma.jmetal.algorithm.multiobjective.lemas.agents;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.RankingComparator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Specialized agent class. In addition to base implementation of {@link JMetal5Agent} it allows to sort Agents based on Global ranking {@link Ranking<DoubleSolution>}.
 * This allows to sort population based on the rank and discard those who are too 'weak' (their genotypes too far from majority) by comparison with {@link RankingComparator}.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 9/02/2018
 * */
public class GlobalRankAgent<S extends Solution<?>> extends BaseAgent<S> {

    /**
     * Comparator based on global ranking.
     * */
    private RankingComparator comparator = new RankingComparator();

    /**
     * Global ranking of dominance.
     * */
    private Ranking<DoubleSolution> ranking = new DominanceRanking<>();

    public GlobalRankAgent() { }


    public GlobalRankAgent(int id)
    {
        super(id);
    }


    /**
     * Computes ranking and returns reference to instance of {@link Ranking<DoubleSolution>} with ranking already computed in it.
     * @param population population to compute ranking from.
     * @return instance of computed ranking.
     * */
    private Ranking<DoubleSolution> computeDominanceRanking(List<? extends BaseAgent> population) {
        List solutionList = population.stream().map(p -> p.getGenotype()).collect(Collectors.toList());
        ranking.computeRanking(solutionList);
        return ranking;
    }

    /**
     * Calls {@link JMetal5Agent#doMeeting(List, double)} and does meeting based its result. Next meet is done using based on result from {@link JMetal5GlobalRankAgent#computeDominanceRanking(List)}.
     * @param meetPopulation population from which to search Agents to meet.
     * @param transferResourceValue value of resources to transfer between Agents during meeting.
     * @return result of meeting in form of integer.
     * */
    @Override
    public int doMeeting(List<? extends BaseAgent<S>> meetPopulation, double transferResourceValue) {
        int domResult = super.doMeeting(meetPopulation, transferResourceValue);
            if(domResult==0){
                BaseAgent meetingPartner = findMeetingPartner(meetPopulation);
                computeDominanceRanking(meetPopulation);
                int compResult = comparator.compare(this.getGenotype(), meetingPartner.getGenotype());
                if (compResult == -1) {
                    transferResourcesFrom(meetingPartner, transferResourceValue);
                } else if (compResult == 1) {
                    transferResourcesTo(meetingPartner, transferResourceValue);
                }
                if (compResult != 0) {
                    this.setMet(true);
                    meetingPartner.setMet(true);
                }
                return compResult;

            }
        return domResult;
    }

    @Override
    public String getAgentType() {
        return "GlobalRankAgent";
    }
}
