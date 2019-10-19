package org.uma.jmetal.algorithm.multiobjective.lemas.utils;


//import javafx.scene.paint.Color;

import org.knowm.xchart.style.Styler;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

/**
 * Main constants used by EMAS algorithms.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * */
public class Constants {

    /* EMAS Constants */
    public static int MATURITY_AGE = 2;
    public static int LOG_LEVEL = 0;
    public static final double REPRODUCTION_LEVEL_VALUE = 2;
    public static final double PROGRESSIVE_REPRODUCTION_LEVEL_VALUE = 1;
    public static final double INITIAL_RESOURCE_VALUE = 1;
    public static final double OFFSPRING_INITIAL_RESOURCE_VALUE = 0;
    public static final double DEATH_LEVEL_VALUE = 0;
    public static final double TRANSFER_RESOURCE_VALUE = 1.0;
    public static final int ENV_ENERGY = 170;
    public static int MAX_ITERATIONS = 1000;
    public static int MAX_EVALUATIONS = 2500;
    public static int NUMBER_OF_ISLANDS = 1;
    public static int MAX_KNOWLEDGE_CAPACITY = 100;

    /* Offspring allowed to EMAS */
    public final static int ALWAYS = 0;
    public final static int IF_BETTER = 1;
    public final static int IF_NOT_WORSE = 2;
    public final static int IF_BETTER_AND_COULD_NOT_KNOW = 3;

    /* Comparator results */
    public final static int FIRST_IS_BETTER = -1;
    public final static int NEITHER_IS_BETTER = 0;
    public final static int SECOND_IS_BETTER = 1;
    public static int IS_BETTER = 1;
    public static int IS_WORSE = -1;
    public static int IS_NEITHER_BETTER_NOR_WORSE = 0;

    /* Charts */
    public static final int HV_FREQUENCY = 50;
    public static final int POP_FREQUENCY = 1;
    public static final int MEETINGS_FREQUENCY = 50;
    public static final int POPULATION_SIZE_FREQUENCY = 50;
    public static final int NUMBER_OF_DECISION_VARIABLES_TO_SHOW = 3;
    public static final Styler.ChartTheme CHART_THEME = Styler.ChartTheme.XChart;

    /* Problems */
    public static final ExperimentProblem<DoubleSolution> PROBLEM = new ExperimentProblem<>(new ZDT1());
    public static final ExperimentProblem<DoubleSolution> PROBLEM_ZDT2 = new ExperimentProblem<>(new ZDT2());
    public static final ExperimentProblem<DoubleSolution> PROBLEM_ZDT3 = new ExperimentProblem<>(new ZDT3());
    public static final ExperimentProblem<DoubleSolution> PROBLEM_ZDT4 = new ExperimentProblem<>(new ZDT4());
    public static String REF_FRONT_DIR = "./jmetal-problem/src/test/resources/pareto_fronts/";
    public static String REF_FRONT_DIR_UNIX = "../jMetal/jmetal-problem/src/test/resources/pareto_fronts/";

    /* Operators */
    public static final CrossoverOperator<DoubleSolution> XOP = new SBXCrossover(1.0, 1.0);
    public static final MutationOperator<DoubleSolution> MOP =
            new PolynomialMutation(1.0 / PROBLEM.getProblem().getNumberOfVariables(),
                    10.0);
    public static final MutationOperator<DoubleSolution> STRONG_MOP =
            new PolynomialMutation(1.0,
                    20.0);
    static final SelectionOperator SEL_OP = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    /* comparators ? */
    public static double NOISE_LEVEL = 0.0;
    public static double[][] ALPHA_VALUES = {{0.0, 0,1},{0.1, 0.0}};

    /* Colors */
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    //    public static Color REFERENCE_COLOR = Color.GREEN;
    //    public static boolean WITH_VISUALIZATION = true;
}