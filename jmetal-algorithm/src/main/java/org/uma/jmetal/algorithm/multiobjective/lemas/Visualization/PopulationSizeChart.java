package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

public class PopulationSizeChart extends ProgressBaseChart<Integer>{


    public PopulationSizeChart(List<Algorithm> algorithmsToShow) {
        super(algorithmsToShow);
        chart.setTitle("Population size");
    }

    @Override
    public void update(List<DoubleSolution> population) {
        throw new UnsupportedOperationException("Requires reference to algorithm object");
    }

    @Override
    public void update(List<DoubleSolution> population, String seriesName) {
        throw new UnsupportedOperationException("Requires reference to algorithm object");
    }

    @Override
    public void update(List<DoubleSolution> population, String seriesName, JMetal5BaseEMAS emas){
        if (isItTimeForUpdate(seriesName, Constants.POPULATION_SIZE_FREQUENCY)){
            xValues.get(seriesName).add(iterationCounter.get(seriesName));
            yValues.get(seriesName).add(emas.getPopulation().size());

            getChart().updateXYSeries(
                    seriesName,
                    xValues.get(seriesName),
                    yValues.get(seriesName),
                    null);
        }
        incrementIterationCounterFor(seriesName);
    }
}
