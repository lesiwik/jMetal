package org.uma.jmetal.algorithm.multiobjective.lemas.agents;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AreaControlAgent<S extends Solution<?>> extends BaseAgent<S> {


    private List<AreaControlAgent<S>> listOfKnownNonDominatedAgents;



    AreaControlAgent(int id) {
        super(id);
        listOfKnownNonDominatedAgents = new ArrayList<>();
    }


    AreaControlAgent() {
        listOfKnownNonDominatedAgents = new ArrayList<>();
    }


    /**
     * Updates {@link AreaControlAgent#listOfKnownNonDominatedAgents} by adding agent in parameter if its not present already.
     * @param meetingPartner agent to check.
     * */
    public void updateListOfKnownNondominatedAgents(AreaControlAgent<S> meetingPartner) {
        //Tutaj trzeba by sie przejsc po liscie i sprawdzic czy ten dodawany nie dominuje ktoregos
        //jesli dominuje to wywalic te dominowane,
        //od razu sprawdzic czy nie jest przez ktoregos dominowany
        // jesli jest to nie dodajemy
        // TODO Sprawdzanie powyzszego dzieje sie i tak w comparatorze chyba, nie lepiej tam wywolac funkcje do tego?
        if (!this.getListOfKnownNonDominatedAgents().contains(meetingPartner)) {
            this.getListOfKnownNonDominatedAgents().add(meetingPartner);

        }
    }

    @Override
    public String getAgentType() {
        return "AreaControlAgent";
    }
}
