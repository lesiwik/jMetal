package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;


import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.util.List;


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
                            for (int i = 0; i < NUMBER_OF_CHARTS_TO_UPDATE; i++) {
                                IterationSlider<S>.SavedState savedState = iterationSlider.getSavedState((String) seriesName);
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
