package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

import java.util.List;


public class HVChart<S extends Solution<?>> extends ProgressBaseChart<Double, S> {
    private PISAHypervolume<S> hv;

    public HVChart(List<Algorithm<S>> algorithmsToShow, Front referenceFront) {
        super(algorithmsToShow);
        chart.setTitle("Hypervolume");
        hv = new PISAHypervolume<>(referenceFront);
    }

    @Override
    public void update(List<S> population) {
        update(population, DEFAULT_SERIES_NAME);
    }

    @Override
    public void update(List<S> population, String seriesName) {
        if (isItTimeForUpdate(seriesName, Constants.HV_FREQUENCY)) {
            xValues.get(seriesName).add(iterationCounter.get(seriesName));
            yValues.get(seriesName).add(hv.evaluate(population));

            chart.updateXYSeries(
                    seriesName,
                    xValues.get(seriesName),
                    yValues.get(seriesName),
                    null);
        }
        incrementIterationCounterFor(seriesName);
    }
}
