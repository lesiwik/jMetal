package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;

import java.io.FileNotFoundException;
import java.util.List;


public class IGDPlusChart<S extends Solution<?>> extends ProgressBaseChart<Double, S> {
    private InvertedGenerationalDistancePlus<S> igdPlus;

    public IGDPlusChart(List<Algorithm<List<S>>> algorithmsToShow, Front referenceFront) {
        super(algorithmsToShow);
        chart.setTitle("IGDPlus");
        igdPlus = new InvertedGenerationalDistancePlus<>(referenceFront);
    }

    @Override
    public void update(List<S> population) {
        update(population, DEFAULT_SERIES_NAME);
    }

    @Override
    public void update(List<S> population, String seriesName) {
        if (isItTimeForUpdate(seriesName, Constants.HV_FREQUENCY)) {
            yValues.get(seriesName).add(igdPlus.evaluate(population));

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
