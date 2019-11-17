package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.*;

/**
 * Basic EMAS Factory that uses its builder. Basically wrapper functions for set configurations of builder.
 *
 * @author Michal Kasprzyk <michal0kasprzyk@gmail.com>
 * @since 29/10/2019
 */
@Data
public class EMASFactory<S extends Solution<?>> {

    @Setter(AccessLevel.NONE)
    private final EMASBuilder<S> EMAS_BUILDER = new EMASBuilder<>();

    private List<Algorithm> algorithms;

    public EMASFactory() {
        algorithms = new ArrayList<>();
    }

    public EMASFactory addEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(BASE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .parentToChildComparator(EMAS_DOMINANCE_COMPARATOR)
                        .build());
        return this;
    }

    public EMASFactory addAreaEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(BASE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .parentToChildComparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public EMASFactory addProgressiveEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .reproCondition(PROGRESSIVE_REPRODUCTION_LEVEL)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .parentToChildComparator(EMAS_DOMINANCE_COMPARATOR)
                        .build());
        return this;
    }

    public EMASFactory addProgressiveAreaEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .reproCondition(PROGRESSIVE_REPRODUCTION_LEVEL)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .parentToChildComparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public EMASFactory addSmartBaseEMAS(String name, int whenToAddOffspring)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(PROGRESSIVE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .parentToChildComparator(EMAS_DOMINANCE_COMPARATOR)
                        .whenAddOffspringToPopulation(whenToAddOffspring)
                        .build());
        return this;
    }

    public EMASFactory addSmartAreaEMAS(String name, int whenToAddOffspring)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(PROGRESSIVE_AGENT)
                        .reproCondition(PROGRESSIVE_REPRODUCTION_LEVEL)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .parentToChildComparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .whenAddOffspringToPopulation(whenToAddOffspring)
                        .build());
        return this;
    }
}
