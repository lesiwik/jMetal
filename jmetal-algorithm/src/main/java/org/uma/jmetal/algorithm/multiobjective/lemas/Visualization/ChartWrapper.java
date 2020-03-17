package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import lombok.Getter;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.internal.chartpart.Chart;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.config.MeetingType;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChartWrapper<S extends Solution<?>> {
    protected final SwingWrapper<Chart<?,?>> wrapper;
    protected List<BaseChart<S>> charts;

    protected Front referenceFront;
    protected final String FRONT_FILE_NAME = Constants.REF_FRONT_DIR + Constants.PROBLEM.getName() + ".pf";


    @Getter
    protected List<AlgorithmStatistics<S>> algorithmStatistics;

    public ChartWrapper() {
        charts = new ArrayList<>();
        charts.add(new PopulationChart<>());
        charts.add(new HVRChart<>());
        wrapper = new SwingWrapper<>(charts.stream().map(BaseChart::getChart).collect(Collectors.toList()));
        wrapper.displayChartMatrix();
    }

    public ChartWrapper(List<Algorithm<List<S>>> algorithmToShow, int numberOFDecisionVariablesToShow) {
        initializeReferenceFronts();
        charts = new ArrayList<>();
        charts.add(new PopulationChart<S>(algorithmToShow));
        charts.add(new PopulationChart<S>(algorithmToShow, true));

        charts.add(new GenericIndicatorChart<S>(algorithmToShow, referenceFront,
                Constants.HYPERVOLUME_INDICATOR_TYPE));

        charts.add(new GenericIndicatorChart<S>(algorithmToShow, referenceFront,
                Constants.HYPERVOLUME_INDICATOR_TYPE).withNormalizedIndicator());

        charts.add(new GenericIndicatorChart<S>(algorithmToShow, referenceFront,
                Constants.INVERTED_GENERATIONAL_DISTANCE_PLUS_INDICATOR_TYPE));

        charts.add(new GenericIndicatorChart<S>(algorithmToShow, referenceFront,
                Constants.INVERTED_GENERATIONAL_DISTANCE_INDICATOR_TYPE));

//        charts.add(new GenericIndicatorChart<S>(algorithmToShow, referenceFront,
//                Constants.GENERATIONAL_DISTANCE_INDICATOR_TYPE));
//
//        charts.add(new GenericIndicatorChart<S>(algorithmToShow, referenceFront,
//                Constants.EPISLON_INDICATOR_TYPE));
//
//        charts.add(new GenericIndicatorChart<S>(algorithmToShow, referenceFront,
//                Constants.SPREAD_INDICATOR_TYPE));

        charts.add(new MeetingsChart<S>(algorithmToShow, MeetingType.I_AM_BETTER));
        charts.add(new MeetingsChart<S>(algorithmToShow, MeetingType.NEITHER_IS_BETTER));
        charts.add(new PopulationSizeChart<S>(algorithmToShow));
        charts.add(new EvaluationsChart<S>(algorithmToShow));

        charts.forEach(chart -> chart.getChart().getStyler().setToolTipsEnabled(true));
        wrapper = new SwingWrapper<>(charts.stream().map(BaseChart::getChart).collect(Collectors.toList()), 3, 3);
        wrapper.displayChartMatrix();
//        List<Chart<?, ?>> test = charts.stream().map(BaseChart::getChart).collect(Collectors.toList());
//        test.add(new AlgorithmStatistics<S>(algorithmToShow, "t", AlgorithmStatistics.StatisticsType.DOMINATION_LEVEL).getChart());
//        wrapper = new SwingWrapper<>(test);
//        wrapper.displayChartMatrix();

        algorithmStatistics = new ArrayList<>();
        algorithmStatistics.add(new AlgorithmStatistics<S>(algorithmToShow,"Domination", AlgorithmStatistics.StatisticsType.DOMINATION_LEVEL));
    }

    private void initializeReferenceFronts()
    {
        try{
            referenceFront = new ArrayFront(FRONT_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateChart(List<S> data) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                    charts.get(i).update(data);
                    wrapper.repaintChart(i);
            }
        });
    }

    public void updateChart(List<S> data, String seriesName, JMetal5BaseEMAS<S> emas, int seriesNumber) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).update(data, seriesName, emas, seriesNumber);
                wrapper.repaintChart(i);
            }
        });
    }


    public void updateChart(List<S> data, String seriesName, Algorithm<List<S>> emas, int seriesNumber) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).update(data, seriesName, emas, seriesNumber);
                wrapper.repaintChart(i);
            }
        });

    }


    public void updateChart(List<S> data, String seriesName) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).update(data, seriesName);
                wrapper.repaintChart(i);
            }
        });
    }
}


