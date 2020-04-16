package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.jmetal5version.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.ArrayList;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.*;

/**
 * Basic Algorithm Factory that uses its builder. Basically wrapper functions for set configurations of builder.
 *
 * @author Michal Kasprzyk <michal0kasprzyk@gmail.com>
 * @since 29/10/2019
 */
@Data
public class AlgorithmFactory<S extends Solution<?>> {


    private @Setter(AccessLevel.NONE) final EMASBuilder<S> EMAS_BUILDER = new EMASBuilder<>();
    private List<Algorithm<List<S>>> algorithms;
    private @Setter(AccessLevel.NONE) static AlgorithmFactory<Solution<?>> algorithmFactory = new AlgorithmFactory<>();

    public AlgorithmFactory() {
        algorithms = new ArrayList<>();
    }

    private List<Algorithm<List<S>>> getListOfAlgorithms() { return algorithms; }


    public static List<Algorithm<List<Solution<?>>>> getAlgorithms()
    {
        return algorithmFactory
        //         .addProgressiveEMAS("Progressive_BETTER_AND_COULD", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
//                 .addProgressiveEMAS("Progressive_NOT_WORSE", Constants.IF_NOT_WORSE)
//                 .addProgressiveEMAS("Progressive_ALWAYS", Constants.ALWAYS)
//                 .addProgressiveEMAS("Progressive_BETTER", Constants.IF_BETTER)
//                 .addProgressiveAreaEMAS("ProgressiveArea_BETTER_AND_COULD", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
//                 .addProgressiveAreaEMAS("ProgressiveArea_NOT_WORSE", Constants.IF_NOT_WORSE)
//                 .addProgressiveAreaEMAS("ProgressiveArea_ALWAYS", Constants.ALWAYS)
//                 .addProgressiveAreaEMAS("ProgressiveArea_BETTER", Constants.IF_BETTER)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_BETTER_AND_COULD", Constants.IF_BETTER_AND_COULD_NOT_KNOW)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_ALWAYS", Constants.ALWAYS)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_NOT_WORSE", Constants.IF_NOT_WORSE)
//                 .addReproductiveProgressiveAreaEMAS("ReproductiveProgressiveArea_BETTER", Constants.IF_BETTER)
//                 .addEMAS("BaseEMAS")
                 .addParallelBaseEMASEval("ParallelBaseEMAS", 400)
//                 .addParallelAreaEMASEval("ParallelAreaEMAS", 300)
//                 .addParallelBaseRadiusEMASEval("ParallelBaseRadiusEMAS", 400)
//                 .addParallelAreaRadiusEMASEval("ParallelAreaRadiusEMAS", 300)
//                 .addAreaEMAS("AreaEMAS")
//                 .addEMAS("BaseEMAS")
//                 .addNotWorseEMAS("NotWorseEMAS")
//                 .addAreaEMAS("AreaEMAS")
//                 .addRadiusBaseEMAS("RadiusEMAS")
//                 .addRadiusAreaEMAS("RadiusAreaEMAS")
//                 .addProgressiveAreaNotWorseEMAS("ProgressiveAreaNotWorseEMAS", Constants.IF_NOT_WORSE)
//                 .addAreaCountingEMAS("AreaCountingEMAS")
//                 .addAreaCountingRadiusEMAS("AreaCountingRadiusEMAS")
//                 .addQualityAverageAreaEMAS("QualityAverageAreaEMAS")
//                 .addQualityConstantAreaEMAS("QualityConstantAreaEMAS")
//                 .addQualityDifferenceAreaEMAS("QualityDifferenceAreaEMAS")
//                 .addReproductiveAreaEMAS("ReproductiveAreaEMAS")
//                 .addReproductiveEMAS("ReproductiveEMAS")
//                 .addBaseNSGAII(Constants.NSGAII_INITIAL_POPULATION_SIZE, Constants.NSGAII_MAX_EVALUATIONS)
//                 .addMeetingBaseEMAS("MeetingBaseEMAS")
//                 .addMeetingAreaEMAS("MeetingAreaEMAS")
            .getListOfAlgorithms();
    }


    public static Algorithm<List<Solution<?>>> getAlgorithm(int index) { return getAlgorithms().get(index); }

    public AlgorithmFactory<S> addEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(BASE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addParallelBaseEMASEval(String name, int envEnergy) {
        algorithms.add(
                EMAS_BUILDER.emasType(PARALLEL_EMAS)
                        .agentType(PARALLEL_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .envEnergy(envEnergy)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .stoppingCondition(StoppingConditions.EVALUATIONS)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addParallelAreaEMASEval(String name, int envEnergy) {
        algorithms.add(
                EMAS_BUILDER.emasType(PARALLEL_EMAS)
                        .agentType(PARALLEL_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .envEnergy(envEnergy)
                        .comparator(THREAD_SAFE_AREA_UNDER_CONTROL_COMPARATOR)
                        .stoppingCondition(StoppingConditions.EVALUATIONS)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addParallelBaseRadiusEMASEval(String name, int envEnergy) {
        algorithms.add(
                EMAS_BUILDER.emasType(PARALLEL_EMAS)
                        .agentType(RADIUS_PARALLEL_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .radiusToCheckMetAgentsIn(RADIUS_TO_CHECK_MET_AGENTS_IN)
                        .envEnergy(envEnergy)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .stoppingCondition(StoppingConditions.EVALUATIONS)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addParallelAreaRadiusEMASEval(String name, int envEnergy) {
        algorithms.add(
                EMAS_BUILDER.emasType(PARALLEL_EMAS)
                        .agentType(RADIUS_PARALLEL_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .radiusToCheckMetAgentsIn(RADIUS_TO_CHECK_MET_AGENTS_IN)
                        .envEnergy(envEnergy)
                        .comparator(THREAD_SAFE_AREA_UNDER_CONTROL_COMPARATOR)
                        .stoppingCondition(StoppingConditions.EVALUATIONS)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addAreaEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(BASE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addNotWorseEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(BASE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(NOT_WORSE_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addReproductiveEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .reproCondition(PROGRESSIVE_REPRODUCTION_LEVEL)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addReproductiveAreaEMAS(String name) {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .reproCondition(PROGRESSIVE_REPRODUCTION_LEVEL)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addProgressiveEMAS(String name, int whenToAddOffspring)
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

    public AlgorithmFactory<S> addProgressiveAreaEMAS(String name, int whenToAddOffspring)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(PROGRESSIVE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .parentToChildComparator(EMAS_DOMINANCE_COMPARATOR)
                        .whenAddOffspringToPopulation(whenToAddOffspring)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addProgressiveAreaNotWorseEMAS(String name, int whenToAddOffspring)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(PROGRESSIVE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .parentToChildComparator(NOT_WORSE_COMPARATOR)
                        .whenAddOffspringToPopulation(whenToAddOffspring)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addReproductiveProgressiveAreaEMAS(String name, int whenToAddOffspring)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(PROGRESSIVE_AGENT)
                        .reproCondition(PROGRESSIVE_REPRODUCTION_LEVEL)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .parentToChildComparator(EMAS_DOMINANCE_COMPARATOR)
                        .whenAddOffspringToPopulation(whenToAddOffspring)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addAreaCountingEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(BASE_AGENT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COUNTER_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addAreaCountingRadiusEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(RADIUS_AGENT)
                        .radiusToCheckMetAgentsIn(RADIUS_TO_CHECK_MET_AGENTS_IN)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COUNTER_COMPARATOR)
                        .build());
        return this;
    }


    public AlgorithmFactory<S> addReproductiveProgressiveBaseEMAS(String name, int whenToAddOffspring)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(PROGRESSIVE_AGENT)
                        .reproCondition(PROGRESSIVE_REPRODUCTION_LEVEL)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .parentToChildComparator(EMAS_DOMINANCE_COMPARATOR)
                        .whenAddOffspringToPopulation(whenToAddOffspring)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addRadiusBaseEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(RADIUS_AGENT)
                        .radiusToCheckMetAgentsIn(RADIUS_TO_CHECK_MET_AGENTS_IN)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addRadiusAreaEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(RADIUS_AGENT)
                        .radiusToCheckMetAgentsIn(RADIUS_TO_CHECK_MET_AGENTS_IN)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addRadiusNotWorseEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(RADIUS_AGENT)
                        .radiusToCheckMetAgentsIn(RADIUS_TO_CHECK_MET_AGENTS_IN)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(NOT_WORSE_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addQualityConstantAreaEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(QUALITY_AGENT)
                        .qualityType(QualityTypes.ABOVE_CONSTANT)
                        .qualityThreshold(ABOVE_THRESHOLD)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addQualityDifferenceAreaEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(QUALITY_AGENT)
                        .qualityType(QualityTypes.DIFFERENCE)
                        .differenceConstant(DIFFERENCE_CONSTANT)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addQualityAverageAreaEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(BASE_EMAS)
                        .agentType(QUALITY_AGENT)
                        .qualityType(QualityTypes.AVERAGE)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addMeetingBaseEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(MEETING_EMAS)
                        .agentType(MEETING_AGENT)
                        .matingDifferenceThreshold(MATING_DIFFERENCE_THRESHOLD)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(EMAS_DOMINANCE_COMPARATOR)
                        .build());
        return this;
    }

    public AlgorithmFactory<S> addMeetingAreaEMAS(String name)
    {
        algorithms.add(
                EMAS_BUILDER.emasType(MEETING_EMAS)
                        .agentType(MEETING_AGENT)
                        .matingDifferenceThreshold(MATING_DIFFERENCE_THRESHOLD)
                        .algorithmName(name)
                        .allowKnowledgeExchange(false)
                        .comparator(AREA_UNDER_CONTROL_COMPARATOR)
                        .build());
        return this;
    }

    public NSGAII<DoubleSolution> createBaseNSGAII(int initialPopulationSize, int maxEvaluations)
    {
        return new NSGAIIBuilder<>(Constants.PROBLEM,
                Constants.XOP, Constants.MOP, initialPopulationSize)
                .setSelectionOperator(
                        new BinaryTournamentSelection<>(
                                new RankingAndCrowdingDistanceComparator<>()))
                .setMaxEvaluations(maxEvaluations)
                .setVariant(NSGAIIBuilder.NSGAIIVariant.Measures)
                .build();
    }

}
