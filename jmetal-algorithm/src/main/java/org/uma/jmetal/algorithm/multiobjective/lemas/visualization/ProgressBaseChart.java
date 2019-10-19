package org.uma.jmetal.algorithm.multiobjective.lemas.visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.SolutionListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ProgressBaseChart<E extends Number> extends BaseChart {
    protected final String DEFAULT_SERIES_NAME = "Default Algorithm Name";
    protected Map<String, Integer> iterationCounter = new HashMap<>();
    protected Map<String, List<Integer>> xValues = new HashMap<>();
    protected Map<String, List<E>> yValues = new HashMap<>();
    protected int chosenIteration;

    public ProgressBaseChart() {
        super();
        initSeries(DEFAULT_SERIES_NAME);
    }

    public ProgressBaseChart(List<Algorithm> algorithmsToShow) {
        super();
        for (Algorithm alg: algorithmsToShow)
            initSeries(alg.getName());
    }

    protected void initSeries(String seriesName) {
        this.getChart().addSeries(seriesName, new double[]{0}, new double[]{0});
        xValues.put(seriesName, new ArrayList<>());
        yValues.put(seriesName, new ArrayList<>());
        iterationCounter.put(seriesName, 0);
    }

    protected boolean isItTimeForUpdate(String seriesName, int relatedConstant) {
        return (iterationCounter.get(seriesName) % relatedConstant == 0);
    }

    protected List<DoubleSolution> getNonDominatedSolutions(List<DoubleSolution> solutionList) {
        return SolutionListUtils.getNondominatedSolutions(solutionList);
    }

    protected void incrementIterationCounterFor(String seriesName) {
        iterationCounter.put(seriesName, iterationCounter.get(seriesName) + 1);
    }

    public void decrementIterationCounterFor(String seriesName) {
        iterationCounter.put(seriesName, iterationCounter.get(seriesName) - 1);
    }

    public void setChosenIteration(int chosenIteration){
        this.chosenIteration = chosenIteration;
    }

    public void updateValues(String seriesName){
        int lastVisible = (int)Math.ceil(chosenIteration/Constants.HV_FREQUENCY) + 1;
        try {
            if (lastVisible > 0) {
                chart.updateXYSeries(
                        seriesName,
                        xValues.get(seriesName).subList(0, lastVisible),
                        yValues.get(seriesName).subList(0, lastVisible),
                        null);
            }
        }catch(Exception ignored){}
    }
}
