package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;


import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class PopulationChart<S extends Solution<?>> extends BaseChart<S> {
    private final Problem<?> DEFAULT_PROBLEM = Constants.PROBLEM;
    private boolean invisiblePareto = false; /* So that both charts are always lined up in the same way. */

    public PopulationChart(List<Algorithm<List<S>>> algorithmToShow, boolean invisiblePareto)
    {
        super();
        this.invisiblePareto = invisiblePareto;
        initializeChart(algorithmToShow);

    }

    public PopulationChart() {
        super();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.addSeries("population", new double[]{0}, new double[]{0});
        this.drawReferenceFront(DEFAULT_PROBLEM);
    }

    public PopulationChart(List<Algorithm<List<S>>> algorithmToShow) {
        super();
        initializeChart(algorithmToShow);
    }

    private void initializeChart(List<Algorithm<List<S>>> algorithmToShow)
    {
        chart.getStyler().setLegendVisible(true);
        chart.setTitle("Population");
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        for (Algorithm<List<S>> alg : algorithmToShow) {

            String name = alg.getName();
            chart.addSeries(name, new double[]{0}, new double[]{0});
        }

        drawReferenceFrontOrDefault(algorithmToShow.get(0));
    }

    private void drawReferenceFrontOrDefault(Algorithm<List<S>> algorithm) {
        Problem<?> problemConsidered = DEFAULT_PROBLEM;
        if (algorithm instanceof JMetal5BaseEMAS) {
            JMetal5BaseEMAS<?> exemplaryAlgorithmCast = (JMetal5BaseEMAS<S>) algorithm;
            problemConsidered = exemplaryAlgorithmCast.getProblem();
        }

        this.drawReferenceFront(problemConsidered);
    }

    private void drawReferenceFront(Problem<?> problem) {
        Front front = null;
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        try {
            String fileName = Constants.REF_FRONT_DIR + problem.getName() + ".pf";

            front = new ArrayFront(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<PointSolution> normalizedFront = FrontUtils.convertFrontToSolutionList(front);
        XYSeries series = chart.addSeries("reference front",
                normalizedFront.stream().mapToDouble(n -> n.getObjective(0)).toArray()
                , normalizedFront.stream().mapToDouble(n -> n.getObjective(1)).toArray());
        if(invisiblePareto)
            series.setMarker(SeriesMarkers.NONE);
    }


    @Override
    public void update(List<S> population) {
        chart.updateXYSeries(
                "population",
                population.stream().map(solution -> solution.getObjective(0)).collect(Collectors.toList()),
                population.stream().map(solution -> solution.getObjective(1)).collect(Collectors.toList()),
                null);
    }

    @Override
    public void update(List<S> population, String seriesName) {
        chart.updateXYSeries(
                seriesName,
                population.stream().map(solution -> solution.getObjective(0)).collect(Collectors.toList()),
                population.stream().map(solution -> solution.getObjective(1)).collect(Collectors.toList()),
                null);
    }

}

