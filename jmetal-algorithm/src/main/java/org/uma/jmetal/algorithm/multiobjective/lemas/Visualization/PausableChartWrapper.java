package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;


import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.solution.Solution;

import java.util.List;
import java.util.Optional;


public class PausableChartWrapper<S extends Solution<?>> extends ChartWrapper<S> {

    private IterationSlider<S> iterationSlider;
    private final int NUMBER_OF_CHARTS_TO_UPDATE = this.charts.size();

    public PausableChartWrapper() {
        super();
    }


    public IterationSlider<S> getIterationSlider() {
        return iterationSlider;
    }

    public PausableChartWrapper(List<Algorithm<List<S>>> algorithmToShow, int numberOFDecisionVariablesToShow) {
        super(algorithmToShow, numberOFDecisionVariablesToShow);
        iterationSlider = new IterationSlider<S>(algorithmToShow);
        iterationSlider.showForm();
        new Thread(() -> {
            while (true) {
                if (iterationSlider.isPaused.get() && iterationSlider.sliderChanged.get()) {
                    iterationSlider.sliderChanged.getAndSet(false);
                    for (Object seriesName : iterationSlider.getIterationCounter().keySet()) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            IterationSlider<S>.SavedState savedState = iterationSlider.getSavedState((String) seriesName);
                            for (int i = 0; i < NUMBER_OF_CHARTS_TO_UPDATE; i++) {
                                if(savedState != null) {
                                    charts.get(i).update(savedState.getData(), savedState.getSeriesName(), savedState.getEmas(), savedState.getSeriesNumber());

                                    if(charts.get(i) instanceof ProgressBaseChart) {
                                        ((ProgressBaseChart) charts.get(i)).setChosenIteration(iterationSlider.getIteration());
                                        ((ProgressBaseChart) charts.get(i)).decrementIterationCounterFor(savedState.getSeriesName());
                                        ((ProgressBaseChart) charts.get(i)).updateValues(savedState.getSeriesName());
                                    }
                                    wrapper.repaintChart(i);
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }


    public void updateChart(List<S> data, String seriesName, Algorithm<List<S>> emas, int seriesNumber) {
        if (!iterationSlider.isPaused.get()) {
            iterationSlider.update(data, seriesName, emas, seriesNumber);
            javax.swing.SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < charts.size(); i++) {
                    charts.get(i).update(data, seriesName, emas, seriesNumber);
                    if(charts.get(i) instanceof ProgressBaseChart) {
                        ((ProgressBaseChart) charts.get(i)).setChosenIteration(iterationSlider.getIteration());
                        ((ProgressBaseChart) charts.get(i)).updateValues(seriesName);
                    }
                    wrapper.repaintChart(i);
                }
                for (int j = 0; j < algorithmStatistics.size() ; j++)
                {
                    algorithmStatistics.get(j).updateStats((JMetal5BaseEMAS<S>) emas);
                }
            });
        }
//        } else {
//            javax.swing.SwingUtilities.invokeLater(() -> {
//                for (int i = 0; i < charts.size(); i++) {
//                    System.out.println("Update chart - for");
//                    charts.get(i).update(data, seriesName, emas, seriesNumber);
//                    wrapper.repaintChart(i);
//                }
//
//            });
//        }
    }


    public void displayAdditionalStatistics()
    {
        Optional<AlgorithmStatistics<S>> dominationStats = this.getAlgorithmStatistics()
                .stream()
                .filter(s-> s.getTitle().equals("Domination"))
                .findFirst();
        if(dominationStats.isPresent())
        {
            StatisticsWindow<S> dominationHistogram = new StatisticsWindow<>("Domination Levels");
            dominationHistogram.setXAxisTitle("Domination Level");
            dominationHistogram.setYAxisTitle("Number of times domination level was achieved");
            dominationHistogram.addData(dominationStats.get(), AlgorithmStatistics.StatisticsType.DOMINATION_LEVEL);
            dominationHistogram.drawBarGraph();
        }
    }
}
