package org.uma.jmetal.algorithm.multiobjective.EMAS.utils;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;

public class ComparatorUtils {


    public static String comparatorResultToString(final int result)
    {
        switch(result)
        {
            case Constants.FIRST_IS_BETTER:
                return "FIRST_IS_BETTER";
            case Constants.SECOND_IS_BETTER:
                return "SECOND_IS_BETTER";
            case Constants.NEITHER_IS_BETTER:
                return "NEITHER_IS_BETTER";
            default:
                throw new IllegalArgumentException("Unsupported integer");
        }
    }

    public static String constructAreaUnderComparisonResultString(final int firstComparison, final int secondComparison)
    {
        return String.format(
                        "\nFirst comparison (A2 to list): %s" + "\nSecond comparison (A1 to list): %s",
        comparatorResultToString(firstComparison), comparatorResultToString(secondComparison));
    }

    public static String constructComparisonResultString(final EmasDominanceComparator comparator,
                                                         final JMetal5Agent<?> a1,
                                                         final JMetal5Agent<?> a2,
                                                         final int comparatorResult,
                                                         final int expectedResult,
                                                         final String additionalInformations)
    {
        return String.format(
                "\nCompared with: %s "
                        + "\nAgent 1: %s"
                        + "\nAgent 2: %s"
                        + additionalInformations
                        + "\nFinal result: %s"
                        + "\nExpected result: %s",
                comparator.getClass().getSimpleName(), a1, a2, comparatorResultToString(comparatorResult), comparatorResultToString(expectedResult));
    }
}