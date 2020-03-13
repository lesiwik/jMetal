package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import lombok.Setter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.uma.jmetal.solution.Solution;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Map;


public class StatisticsWindow<S extends Solution<?>> extends ApplicationFrame {


    private DefaultCategoryDataset dataCategorySet;

    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;

    @Setter
    public String xAxisTitle;

    @Setter
    public String yAxisTitle;

    public StatisticsWindow(String title) {
        super(title);
    }

    private void addDominationData(AlgorithmStatistics<S> data) {
        Map<String, Map<String, Number>> dataset = data.getSeries();
        dataCategorySet = new DefaultCategoryDataset();
        dataset.forEach((algorithmName, algorithmData) -> {
            if (algorithmData.size() == 0)
                return;
            algorithmData.forEach((key, value) ->
                dataCategorySet.addValue(value, key, algorithmName));
        });

    }


    public void addData(AlgorithmStatistics<S> data, AlgorithmStatistics.StatisticsType statisticsType) {
        switch (statisticsType) {
            case DOMINATION_LEVEL:
                addDominationData(data);
                break;
            default:
                throw new IllegalArgumentException("Enum type not specified");
        }
    }

    public void drawBarGraph() {
        JFreeChart barChart = ChartFactory.createBarChart(
                getTitle(),
                xAxisTitle,
                yAxisTitle,
                dataCategorySet,
                PlotOrientation.VERTICAL,
                true, true, true
        );


        CategoryItemRenderer renderer = barChart.getCategoryPlot().getRenderer();
        renderer.setBaseToolTipGenerator(
                        new StandardCategoryToolTipGenerator(xAxisTitle + ": {0} -" + yAxisTitle + ": {2}", NumberFormat.getInstance()));
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        ChartPanel chartPanel = new ChartPanel(barChart);

        setContentPane(chartPanel);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }
}