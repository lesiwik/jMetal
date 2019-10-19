package org.uma.jmetal.algorithm.multiobjective.lemas.visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.front.imp.ArrayFront;

import java.io.FileNotFoundException;
import java.util.List;


public class IGDPlusChart extends ProgressBaseChart<Double> {
    private InvertedGenerationalDistancePlus igdPlus;

    public IGDPlusChart(List<Algorithm> algorithmsToShow) {
        super(algorithmsToShow);
        chart.setTitle("IGDPlus (iteracja % " + Constants.HV_FREQUENCY + ")");

        try {
            String fileName = Constants.REF_FRONT_DIR + Constants.PROBLEM.getProblem().getName() + ".pf";
            igdPlus = new InvertedGenerationalDistancePlus<DoubleSolution>(new ArrayFront(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(List<DoubleSolution> population) {
        update(population, DEFAULT_SERIES_NAME);
    }

    @Override
    public void update(List<DoubleSolution> population, String seriesName) {
        if (isItTimeForUpdate(seriesName, Constants.HV_FREQUENCY)) {
            yValues.get(seriesName).add(igdPlus.evaluate(
                    getNonDominatedSolutions(population)));

            xValues.get(seriesName).add(iterationCounter.get(seriesName));
            chart.updateXYSeries(
                    seriesName,
                    xValues.get(seriesName),
                    yValues.get(seriesName),
                    null);
        }
        incrementIterationCounterFor(seriesName);
    }
}
