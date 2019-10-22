package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class PopulationChart extends BaseChart {
    private final Problem<?> DEFAULT_PROBLEM = Constants.PROBLEM.getProblem();
    private boolean invisiblePareto = false; /* So that both charts are always lined up in the same way. */

    public PopulationChart(List<Algorithm> algorithmToShow, boolean invisiblePareto)
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

    public PopulationChart(List<Algorithm> algorithmToShow) {
        super();
        initializeChart(algorithmToShow);
    }

    private void initializeChart(List<Algorithm> algorithmToShow)
    {
        this.chart.getStyler().setLegendVisible(false);
        this.getChart().setTitle("Populacja");
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        for (Algorithm alg : algorithmToShow) {

            String name = alg.getName();
            chart.addSeries(name, new double[]{0}, new double[]{0});
        }

        drawReferenceFrontOrDefault(algorithmToShow.get(0));
    }

    private void drawReferenceFrontOrDefault(Algorithm algorithm) {
        Problem<?> problemConsidered = DEFAULT_PROBLEM;
        if (algorithm instanceof JMetal5BaseEMAS<?>) {
            JMetal5BaseEMAS<?> exemplaryAlgorithmCast = (JMetal5BaseEMAS<?>) algorithm;
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
    public void update(List<DoubleSolution> population) {
        chart.updateXYSeries(
                "population",
                population.stream().map(solution -> solution.getObjective(0)).collect(Collectors.toList()),
                population.stream().map(solution -> solution.getObjective(1)).collect(Collectors.toList()),
                null);
    }

    @Override
    public void update(List<DoubleSolution> population, String seriesName) {
        chart.updateXYSeries(
                seriesName,
                population.stream().map(solution -> solution.getObjective(0)).collect(Collectors.toList()),
                population.stream().map(solution -> solution.getObjective(1)).collect(Collectors.toList()),
                null);
    }

}

