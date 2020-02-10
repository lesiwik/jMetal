package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

import java.util.List;

public class GenericIndicatorChart<S extends Solution<?>> extends ProgressBaseChart<Double, S>{

    private GenericIndicator<S> currentMetric;
    private Front referenceFront;

    public GenericIndicatorChart(List<Algorithm<S>> algorithmsToShow, Front referenceFront)
    {
        super(algorithmsToShow);
        this.referenceFront = referenceFront;
    }

    public GenericIndicatorChart<S> withIndicator(String indicatorType)
    {
        switch(indicatorType)
        {
            case Constants.SPREAD_INDICATOR_TYPE:
                currentMetric = new Spread<>(referenceFront);
                break;
            case Constants.EPISLON_INDICATOR_TYPE:
                currentMetric = new Epsilon<>(referenceFront);
                break;
            case Constants.INVERTED_GENERATIONAL_DISTANCE_INDICATOR_TYPE:
                currentMetric = new InvertedGenerationalDistance<>(referenceFront);
                break;
            case Constants.GENERATIONAL_DISTANCE_INDICATOR_TYPE:
                currentMetric = new GenerationalDistance<>(referenceFront);
                break;
            case Constants.INVERTED_GENERATIONAL_DISTANCE_PLUS_INDICATOR_TYPE:
                currentMetric = new InvertedGenerationalDistancePlus<>(referenceFront);
                break;
            case Constants.HYPERVOLUME_INDICATOR_TYPE:
                currentMetric = new PISAHypervolume<>(referenceFront);
                break;
            default:
                throw new IllegalArgumentException("Indicator type of " +  indicatorType + " is not supported");
        }
        chart.setTitle(indicatorType);
        return this;
    }

    @Override
    public void update(List<S> population) {
        update(population, DEFAULT_SERIES_NAME);
    }

    @Override
    public void update(List<S> population, String seriesName) {
        if (isItTimeForUpdate(seriesName, Constants.INDICATOR_FREQUENCY)) {
            yValues.get(seriesName).add(currentMetric.evaluate(population));
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
