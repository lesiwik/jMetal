package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.front.imp.ArrayFront;

import java.io.FileNotFoundException;
import java.util.List;


public class HVChart extends ProgressBaseChart<Double> {
    private PISAHypervolume hv;

    public HVChart() {
        super();
        chart.setTitle("HV (iteracja % "+Constants.HV_FREQUENCY+")");

        try {
            hv = new PISAHypervolume<DoubleSolution>(new ArrayFront("../jMetal/jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HVChart(List<Algorithm> algorithmsToShow) {
        super(algorithmsToShow);
        chart.setTitle("HV (iteracja % "+Constants.HV_FREQUENCY+")");

        try {
            String fileName = Constants.REF_FRONT_DIR + Constants.PROBLEM.getProblem().getName() + ".pf";
            hv = new PISAHypervolume<DoubleSolution>(new ArrayFront(fileName));
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
            yValues.get(seriesName).add(hv.evaluate(
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
