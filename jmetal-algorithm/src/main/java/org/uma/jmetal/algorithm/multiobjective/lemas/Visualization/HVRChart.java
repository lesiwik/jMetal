package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;

import java.io.FileNotFoundException;
import java.util.List;


public class HVRChart<S extends Solution<?>> extends ProgressBaseChart<Double, S> {
    Front normalizedReferenceFront = getNormalizedReferenceFront();//  frontNormalizer.normalize(referenceFront);
    PISAHypervolume hv = new PISAHypervolume(normalizedReferenceFront);
    double referenceHV = hv.evaluate(FrontUtils.convertFrontToSolutionList(normalizedReferenceFront));

    private Front getNormalizedReferenceFront() {
        try {
            Problem<DoubleSolution> problem = Constants.PROBLEM;
            String proName = problem.getName();
            String referenceFrontFileName = Constants.REF_FRONT_DIR + proName + ".pf";
            Front referenceFront = new ArrayFront(referenceFrontFileName);
            FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
            return frontNormalizer.normalize(referenceFront);
        } catch (FileNotFoundException e) {

            //            LOGGER.log(Level.INFO, "Reference front not found in given directory: " + Constants.REF_FRONT_DIR);
            return null;
        }
    }

    public HVRChart() {
        super();
        chart.setTitle("HVR (iteracja % "+Constants.HV_FREQUENCY+")");
    }

    public HVRChart(List<Algorithm<List<S>>> algorithmsToShow) {
        super(algorithmsToShow);
        chart.setTitle("HVR (iteracja % "+Constants.HV_FREQUENCY+")");
    }

    @Override
    public void update(List<S> population) {
        update(population, DEFAULT_SERIES_NAME);
    }

    @Override
    public void update(List<S> population, String seriesName) {
        if (isItTimeForUpdate(seriesName, Constants.HV_FREQUENCY)) {
            xValues.get(seriesName).add(iterationCounter.get(seriesName));
            yValues.get(seriesName).add(hv.evaluate(
                    getNonDominatedSolutions(population)) / referenceHV);

            chart.updateXYSeries(
                    seriesName,
                    xValues.get(seriesName),
                    yValues.get(seriesName),
                    null);
        }
        incrementIterationCounterFor(seriesName);
    }
}
