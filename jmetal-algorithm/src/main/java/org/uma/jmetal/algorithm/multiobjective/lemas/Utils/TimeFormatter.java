package org.uma.jmetal.algorithm.multiobjective.lemas.Utils;

public class TimeFormatter {

    private static final int SECOND = 1000;

    public static String msToTime(long miliseconds)
    {
        StringBuilder stringBuilder = new StringBuilder();
        double ms = miliseconds;
        String type = "";
        if (ms >= SECOND)
        {
            ms /= SECOND;
            type = " s.";
            stringBuilder.append(String.format("%.2f", ms)).append(type);
        }
        else
        {
            type = " ms.";
            stringBuilder.append(ms).append(type);
        }
        return stringBuilder.toString();
    }
}
