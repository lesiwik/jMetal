package org.uma.jmetal.algorithm.multiobjective.lemas.visualization;


import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;


public class PausableChartWrapper extends ChartWrapper {

    private IterationSlider iterationSlider;
    private final int NUMBER_OF_CHARTS_TO_UPDATE = 14;

    public PausableChartWrapper() {
        super();
    }


    public IterationSlider getIterationSlider() {
        return iterationSlider;
    }

    public PausableChartWrapper(List<Algorithm> algorithmToShow, int numberOFDecisionVariablesToShow) {
        super(algorithmToShow, numberOFDecisionVariablesToShow);
        iterationSlider = new IterationSlider(algorithmToShow);
        iterationSlider.showForm();
        new Thread(() -> {
            while (true) {
                if (iterationSlider.isPaused.get() && iterationSlider.sliderChanged.get()) {
                    iterationSlider.sliderChanged.getAndSet(false);
                    for (String seriesName : iterationSlider.getIterationCounter().keySet()) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            for (int i = 0; i < NUMBER_OF_CHARTS_TO_UPDATE; i++) {
                                IterationSlider.SavedState savedState = iterationSlider.getSavedState(seriesName);
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


    public void updateChart(List<DoubleSolution> data, String seriesName, Algorithm emas, int seriesNumber) {
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
}
