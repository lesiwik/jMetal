package org.uma.jmetal.algorithm.multiobjective.lemas.Utils;


import org.knowm.xchart.style.Styler;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.Utils.ReproCondition;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.NullMutation;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.operator.crossover.impl.SBXCrossover;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.point.PointSolution;

/**
 * Main constants used by EMAS Algorithms.
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
    public static final int MAX_ITERATIONS = 1000;
    public static final int MAX_EVALUATIONS = 2500;
    public static final int NUMBER_OF_ISLANDS = 1;
    public static final int MAX_KNOWLEDGE_CAPACITY = 100;
    public static final double RADIUS_TO_CHECK_MET_AGENTS_IN = 0.005;

    /* Offspring allowed to EMAS */
    public final static int ALWAYS = 0;
    public final static int IF_BETTER = 1;
    public final static int IF_NOT_WORSE = 2;
    public final static int IF_BETTER_AND_COULD_NOT_KNOW = 3;

    /* Comparator results */
    public final static int FIRST_IS_BETTER = -1;
    public final static int NEITHER_IS_BETTER = 0;
    public final static int SECOND_IS_BETTER = 1;
    public final static int IS_BETTER = 1;
    public final static int IS_WORSE = -1;
    public final static int IS_NEITHER_BETTER_NOR_WORSE = 0;

    /* Comparator types */
    public final static String EMAS_DOMINANCE_COMPARATOR = "EmasDominanceComparator";
    public final static String AREA_UNDER_CONTROL_COMPARATOR = "AreaUnderControlComparator";
    public final static String AREA_UNDER_CONTROL_COUNTER_COMPARATOR = "AreaUnderControlCounterComparator";
    public final static String AREA_UNDER_CONTROL_EXTENDED_COMPARATOR = "AreaUnderControlExtendedComparator";
    public final static String NOT_WORSE_COMPARATOR = "NotWorseComparator";

    /* Agent types */
    public final static String BASE_AGENT = "JMetal5BaseAgent";
    public final static String GLOBAL_RANK_AGENT = "JMetal5GlobalRankAgent";
    public final static String PROGRESSIVE_AGENT  = "JMetal5ProgressiveAgent";
    public final static String RADIUS_AGENT  = "JMetal5RadiusAgent";

    /* EMAS types */
    public final static String BASE_EMAS = "JMetal5BaseEMAS";
    public final static String GLOBAL_RANK_EMAS = "JMetal5GlobalRankEMAS";
    public final static String PROGRESSIVE_EMAS = "JMetal5ProgressiveEMAS";

    /* Reproduction levels */
    public final static ReproCondition PROGRESSIVE_REPRODUCTION_LEVEL =
            (double resourceLevel) -> resourceLevel >= Constants.PROGRESSIVE_REPRODUCTION_LEVEL_VALUE;

    public final static ReproCondition BASIC_REPRODUCTION_LEVEL =
            (double resourceLevel) -> resourceLevel >= Constants.REPRODUCTION_LEVEL_VALUE;

    /* Charts */
    public static final int HV_FREQUENCY = 50;
    public static final int INDICATOR_FREQUENCY = 50;
    public static final int POP_FREQUENCY = 1;
    public static final int MEETINGS_FREQUENCY = 50;
    public static final int EVALUATION_FREQUENCY = 50;
    public static final int POPULATION_SIZE_FREQUENCY = 50;
    public static final int NUMBER_OF_DECISION_VARIABLES_TO_SHOW = 3;
    public static final Styler.ChartTheme CHART_THEME = Styler.ChartTheme.XChart;

    /* Indicators */
    public static final String HYPERVOLUME_INDICATOR_TYPE = "HyperVolume";
    public static final String EPISLON_INDICATOR_TYPE = "Epsilon";
    public static final String GENERATIONAL_DISTANCE_INDICATOR_TYPE = "GD";
    public static final String INVERTED_GENERATIONAL_DISTANCE_INDICATOR_TYPE = "IDG";
    public static final String INVERTED_GENERATIONAL_DISTANCE_PLUS_INDICATOR_TYPE = "IDGPlus";
    public static final String SPREAD_INDICATOR_TYPE = "Spread";

    /* Problems */
    public static final ZDT1 PROBLEM = new ZDT1();
    public static final ZDT2 PROBLEM_ZDT2 = new ZDT2();
    public static final ZDT3 PROBLEM_ZDT3 = new ZDT3();
    public static final ZDT4 PROBLEM_ZDT4 = new ZDT4();
    public static String REF_FRONT_DIR = "./referenceFronts/";
    public static String REF_FRONT_DIR_UNIX = "../jMetal/jmetal-problem/src/test/resources/pareto_fronts/";

    /* Operators */
    public static final CrossoverOperator<DoubleSolution> XOP = new SBXCrossover(1.0, 1.0);
    public static final MutationOperator<DoubleSolution> MOP =
            new PolynomialMutation(1.0 / PROBLEM.getNumberOfVariables(),
                    10.0);
    public static final MutationOperator<DoubleSolution> STRONG_MOP =
            new PolynomialMutation(1.0,
                    20.0);

    public static final MutationOperator<PointSolution> NO_MUTATION_MOP = new NullMutation<>();
    static final SelectionOperator SEL_OP = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    /* Comparators ? */
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




    /* NSGAII Constants */
    public static final int NSGAII_INITIAL_POPULATION_SIZE = 100;
    public static final int NSGAII_MAX_EVALUATIONS = 25000;
}