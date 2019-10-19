package org.uma.jmetal.algorithm.multiobjective.lemas.algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.BaseAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.DeepCopier;
import org.uma.jmetal.solution.Solution;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * Class that stores important variables of single {@link BaseEMAS} instance so that it can be saved on disc and later be loaded.
 * @author M. Kasprzyk michal0kasprzyk@gmail.com & M. Budziaszek budziaszek.m@gmail.com
 * @since 27/08/2019
 * */
public class BaseEMASSaver<S extends Solution<?>> implements Serializable {

    public int iterations;
    public int evaluations;
    public List<BaseAgent<S>> agentsRecords;

    /**
     * Saves instance of {@link BaseEMAS} inside instance of {@link BaseEMASSaver}.
     * @param emas instance of emas to be saved.
     * */
    @SuppressWarnings("unchecked")
    public void save(BaseEMAS emas){
        iterations = emas.getIteration();
        evaluations = emas.getEvaluations();
        agentsRecords = (List<BaseAgent<S>>) DeepCopier.copy(emas.agentsRecords);
    }

    /**
     * Loads data from {@link BaseEMASSaver} to provided instance of {@link BaseEMAS}.
     * @param emas Cannot be null. Data will be loaded to provided instance.
     * */
    public void load(BaseEMAS emas){
        emas.setIteration(iterations);
        emas.setEvaluations(evaluations);
        emas.agentsRecords = agentsRecords;
        BaseAgent.idCount = agentsRecords.stream().max(Comparator.comparing(BaseAgent::getId)).get().getId();
    }
}