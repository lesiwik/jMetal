package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.front.imp.ArrayFront;

import java.io.FileNotFoundException;
import java.util.List;


public class EvaluationIGDPlusChart extends ProgressBaseChart<Double> {
    private InvertedGenerationalDistancePlus igdPlus;

    public EvaluationIGDPlusChart(List<Algorithm> algorithmsToShow) {
        super(algorithmsToShow);
        this.getChart().setTitle("IGDPlus (ewaluacja % " + Constants.HV_FREQUENCY + ")");

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
            xValues.get(seriesName).add(iterationCounter.get(seriesName));
            yValues.get(seriesName).add(igdPlus.evaluate(
                    getNonDominatedSolutions(population)));

            chart.updateXYSeries(
                    seriesName,
                    xValues.get(seriesName),
                    yValues.get(seriesName),
                    null);
        }
        incrementIterationCounterFor(seriesName);
    }

    @Override
    public void update(List<DoubleSolution> population, String seriesName, JMetal5BaseEMAS emas) {
        if (isItTimeForUpdate(seriesName, Constants.HV_FREQUENCY)) {
            xValues.get(seriesName).add(emas.getEvaluations());
            yValues.get(seriesName).add(igdPlus.evaluate(
                    getNonDominatedSolutions(population)));

            chart.updateXYSeries(
                    seriesName,
                    xValues.get(seriesName),
                    yValues.get(seriesName),
                    null);
        }
        incrementIterationCounterFor(seriesName);
    }
}
