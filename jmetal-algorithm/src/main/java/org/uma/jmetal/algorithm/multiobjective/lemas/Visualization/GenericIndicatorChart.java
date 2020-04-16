package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.impl.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import java.util.List;

public class GenericIndicatorChart<S extends Solution<?>> extends ProgressBaseChart<Double, S>{

    private final GenericIndicator<S> currentMetric;
    private final Front referenceFront;
    private final String indicatorType;


    private GenericIndicator<PointSolution> normalizedMetric;
    private boolean isNormalizedMode;
    private double referenceIndicator;
    private FrontNormalizer frontNormalizer;

    public GenericIndicatorChart(List<Algorithm<List<S>>> algorithmsToShow, Front referenceFront, String indicatorType)
    {
        super(algorithmsToShow);
        this.referenceFront = referenceFront;
        this.isNormalizedMode = false;
        this.indicatorType = indicatorType;
        currentMetric = this.<S>withMetric(indicatorType);
    }

    public <T extends Solution<?>> GenericIndicator<T> withMetric(String indicatorType)
    {
        GenericIndicator<T> indicator;
        switch(indicatorType)
        {
            case Constants.SPREAD_INDICATOR_TYPE:
                indicator = new Spread<>(referenceFront);
                break;
            case Constants.EPISLON_INDICATOR_TYPE:
                indicator = new Epsilon<>(referenceFront);
                break;
            case Constants.INVERTED_GENERATIONAL_DISTANCE_INDICATOR_TYPE:
                indicator = new InvertedGenerationalDistance<>(referenceFront);
                break;
            case Constants.GENERATIONAL_DISTANCE_INDICATOR_TYPE:
                indicator = new GenerationalDistance<>(referenceFront);
                break;
            case Constants.INVERTED_GENERATIONAL_DISTANCE_PLUS_INDICATOR_TYPE:
                indicator = new InvertedGenerationalDistancePlus<>(referenceFront);
                break;
            case Constants.HYPERVOLUME_INDICATOR_TYPE:
                indicator = new PISAHypervolume<>(referenceFront);
                break;
            default:
                throw new IllegalArgumentException("Indicator type of " +  indicatorType + " is not supported");
        }
        chart.setTitle(indicatorType);
        return indicator;
    }

    public GenericIndicatorChart<S> withNormalizedIndicator()
    {
        this.isNormalizedMode = true;
        frontNormalizer = new FrontNormalizer(referenceFront);
        Front normalizedFront = frontNormalizer.normalize(referenceFront);
        normalizedMetric = this.<PointSolution>withMetric(indicatorType);
        referenceIndicator = normalizedMetric.evaluate(FrontUtils.convertFrontToSolutionList(normalizedFront));
        chart.setTitle(indicatorType + "Ratio (N)");
        return this;
    }

    @Override
    public void update(List<S> population) {
        update(population, DEFAULT_SERIES_NAME);
    }

    @Override
    public void update(List<S> population, String seriesName) {
        if (isItTimeForUpdate(seriesName, Constants.INDICATOR_FREQUENCY)) {

            if(isNormalizedMode)
            {
                Front normalizedCalculatedFront = frontNormalizer.normalize(new ArrayFront(population));
                List<PointSolution> pointSolutions = FrontUtils.convertFrontToSolutionList(normalizedCalculatedFront);
                yValues.get(seriesName).add(normalizedMetric.evaluate(pointSolutions) / referenceIndicator);
//                yValues.get(seriesName).add(currentMetric.evaluate(population) / referenceIndicator);
            }
            else
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
