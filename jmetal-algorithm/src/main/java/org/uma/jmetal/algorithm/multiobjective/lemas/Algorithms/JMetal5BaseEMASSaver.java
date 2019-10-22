package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.DeepCopier;
import org.uma.jmetal.solution.Solution;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * Class that stores important variables of single {@link JMetal5BaseEMAS} instance so that it can be saved on disc and later be loaded.
 * @author M. Kasprzyk michal0kasprzyk@gmail.com & M. Budziaszek budziaszek.m@gmail.com
 * @since 27/08/2019
 * */
public class JMetal5BaseEMASSaver<S extends Solution<?>> implements Serializable {

    public int iterations;
    public int evaluations;
    public List<JMetal5Agent<S>> agentsRecords;

    /**
     * Saves instance of {@link JMetal5BaseEMAS} inside instance of {@link JMetal5BaseEMASSaver}.
     * @param emas instance of emas to be saved.
     * */
    @SuppressWarnings("unchecked")
    public void save(JMetal5BaseEMAS emas){
        iterations = emas.getIteration();
        evaluations = emas.getEvaluations();
        agentsRecords = (List<JMetal5Agent<S>>) DeepCopier.copy(emas.agentsRecords);
    }

    /**
     * Loads data from {@link JMetal5BaseEMASSaver} to provided instance of {@link JMetal5BaseEMAS}.
     * @param emas Cannot be null. Data will be loaded to provided instance.
     * */
    public void load(JMetal5BaseEMAS emas){
        emas.setIteration(iterations);
        emas.setEvaluations(evaluations);
        emas.agentsRecords = agentsRecords;
        JMetal5Agent.idCount = agentsRecords.stream().max(Comparator.comparing(JMetal5Agent::getId)).get().getId();
    }
}