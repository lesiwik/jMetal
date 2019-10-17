package org.uma.jmetal.experiment;/*
package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.EMAS.Comparators.AlphaDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasSingleObjectiveComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.ALWAYS;

public class LEMASStudy {
    private static final int INDEPENDENT_RUNS = 25;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new JMetalException("Needed arguments: experimentBaseDirectory");
            // should be set to:  ../Results/ProjectAcronim/IssueNumber/TimeStamp
        }
        String experimentBaseDirectory = args[0];

        List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
        problemList.add(new ExperimentProblem<>(new ZDT1()));
        problemList.add(new ExperimentProblem<>(new ZDT2()));
        problemList.add(new ExperimentProblem<>(new ZDT3()));
        problemList.add(new ExperimentProblem<>(new ZDT4()));
        problemList.add(new ExperimentProblem<>(new ZDT6()));

        List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList =
                configureAlgorithmList(problemList);

        Experiment<DoubleSolution, List<DoubleSolution>> experiment =
                new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("LEMASStudy")
                        .setAlgorithmList(algorithmList)
                        .setProblemList(problemList)
                        .setExperimentBaseDirectory(experimentBaseDirectory)
                        .setOutputParetoFrontFileName("FUN")
                        .setOutputParetoSetFileName("VAR")
                        .setReferenceFrontDirectory(experimentBaseDirectory + "/referenceFronts")
                        .setIndicatorList(Arrays.asList(
                                new Epsilon<DoubleSolution>(), new Spread<DoubleSolution>(), new GenerationalDistance<DoubleSolution>(),
                                new PISAHypervolume<DoubleSolution>(),
                                new InvertedGenerationalDistance<DoubleSolution>(), new InvertedGenerationalDistancePlus<DoubleSolution>()))
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(8)
                        .build();

        new ExecuteAlgorithms<>(experiment).run();
        new GenerateReferenceParetoSetAndFrontFromDoubleSolutions(experiment).run();
        new ComputeQualityIndicators<>(experiment).run();
        new GenerateLatexTablesWithStatistics(experiment).run();
        new GenerateWilcoxonTestTablesWithR<>(experiment).run();
        new GenerateFriedmanTestTables<>(experiment).run();
        new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run();
    }


    static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
            List<ExperimentProblem<DoubleSolution>> problemList) {
        List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();


        for (int i = 0; i < problemList.size(); i++) {
            Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMAS<>(problemList.get(i).getProblem(),
                    "BaseEMAS", ALWAYS,
                    false,
                    new EmasDominanceComparator(), new EmasDominanceComparator());
            algorithms.add(new ExperimentAlgorithm<>(algorithm, algorithm.getName(), problemList.get(i), i));
        }


        for (int i = 0; i < problemList.size(); i++) {
            Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMAS<>(problemList.get(i).getProblem(),
                    "AreaUnderControl", ALWAYS, false,
                    new AreaUnderControlComparator(), new EmasDominanceComparator());
            algorithms.add(new ExperimentAlgorithm<>(algorithm, algorithm.getName(), problemList.get(i), i));
        }


        for (int i = 0; i < problemList.size(); i++) {
            Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMAS<>(problemList.get(i).getProblem(),
                    "AlphaDominance", ALWAYS, false,
                    new AlphaDominanceComparator(Constants.ALPHA_VALUES),
                    new EmasDominanceComparator());
            algorithms.add(new ExperimentAlgorithm<>(algorithm, algorithm.getName(), problemList.get(i), i));
        }

//    for (int i = 0; i < problemList.size(); i++) {
//     Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMASBuilder<>(
//              problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 5),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0),
//              new PolynomialMutation(1.0, 20.0),
//              "JMetalBaseEMAS",0, new EmasDominanceComparator())
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, algorithm.getName(), problemList.get(i).getTag()));
//    }
//
//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMASBuilder<>(
//              problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 5),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0),
//              new PolynomialMutation(1.0, 20.0),
//              "JMetalBaseEMASElite",0, new EmasDominanceComparator())
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, algorithm.getName(), problemList.get(i).getTag()));
//    }


//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMASBuilder<>(
//              problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 5),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0),
//              new PolynomialMutation(1.0, 20.0),
//              "JMetalProgressiveEMAS",0)
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, algorithm.getName(), problemList.get(i).getTag()));
//    }
//
//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMASBuilder<>(
//              problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 5),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0),
//              new PolynomialMutation(1.0, 20.0),
//              "JMetalGlobalRankEMAS",0)
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, algorithm.getName(), problemList.get(i).getTag()));
//    }

//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5ProgressiveEMASBuilder1<>(
//              problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 5),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0),
//              new PolynomialMutation(1.0, 20.0),
//              "JMetalProgressiveEMAS")
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, "JMetalProgressiveEMAS", problemList.get(i).getTag()));
//    }

//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5GlobalRankEMASBuilder1<>(
//              problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 5),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0),
//              new PolynomialMutation(1.0, 20.0),
//              "JMetalGlobalRank")
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, "JMetalGlobalRankEmas", problemList.get(i).getTag()));
//    }
//
//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(
//              problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 5),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0))
//              .setMaxEvaluations(25000)
//              .setPopulationSize(100)
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIa", problemList.get(i).getTag()));
//    }

//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMASBuilder<>(
//               problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 20),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0),
//              new PolynomialMutation(1.0, 40.0),
//              "JMetalBaseEMAS")
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, "JMetalBaseEMASb", problemList.get(i).getTag()));
//    }
//
//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMASBuilder<>(
//               problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 40),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 40.0),
//              new PolynomialMutation(1.0, 80.0),
//              "JMetalBaseEMAS")
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, "JMetalBaseEMASc", problemList.get(i).getTag()));
//    }
//    for (int i = 0; i < problemList.size(); i++) {
//      Algorithm<List<DoubleSolution>> algorithm = new JMetal5BaseEMASBuilder<>(
//               problemList.get(i).getProblem(),
//              new SBXCrossover(1.0, 80),
//              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 80.0),
//              new PolynomialMutation(1.0, 100.0),
//              "JMetalBaseEMAS")
//              .build();
//      algorithms.add(new ExperimentAlgorithm<>(algorithm, "JMetalBaseEMASd", problemList.get(i).getTag()));
//    }
        return algorithms;
    }

}*/
