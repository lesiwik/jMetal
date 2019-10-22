package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JMetal5AreaControlAgent<S extends Solution<?>> extends JMetal5Agent<S>{


    private List<JMetal5AreaControlAgent<S>> listOfKnownNonDominatedAgents;



    public JMetal5AreaControlAgent(int id) {
        super(id);
        listOfKnownNonDominatedAgents = new ArrayList<>();
    }


    public JMetal5AreaControlAgent() {
        listOfKnownNonDominatedAgents = new ArrayList<>();
    }


    /**
     * Updates {@link JMetal5AreaControlAgent#listOfKnownNonDominatedAgents} by adding agent in parameter if its not present already.
     * @param meetingPartner agent to check.
     * */
    public void updateListOfKnownNondominatedAgents(JMetal5AreaControlAgent<S> meetingPartner) {
        //Tutaj trzeba by sie przejsc po liscie i sprawdzic czy ten dodawany nie dominuje ktoregos
        //jesli dominuje to wywalic te dominowane,
        //od razu sprawdzic czy nie jest przez ktoregos dominowany
        // jesli jest to nie dodajemy //TODO

        if (!this.getListOfKnownNonDominatedAgents().contains(meetingPartner)) {
            this.getListOfKnownNonDominatedAgents().add(meetingPartner);
        }

        if (!meetingPartner.getListOfKnownNonDominatedAgents().contains(this)) {
            meetingPartner.getListOfKnownNonDominatedAgents().add(this);
        }
    }

    @Override
    public String getAgentType() {
        return "JMetal5AreaControlAgent";
    }
}
