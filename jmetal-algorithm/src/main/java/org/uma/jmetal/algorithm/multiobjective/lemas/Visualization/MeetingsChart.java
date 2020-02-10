package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.config.MeetingType;
import org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.ProgressBaseChart;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.util.List;

public class MeetingsChart<S extends Solution<?>> extends ProgressBaseChart<Integer, S> {
    private final MeetingType meetingType;

    public MeetingsChart(List<Algorithm<S>> algorithmsToShow, MeetingType meetingType) {
        super(algorithmsToShow);
        this.meetingType = meetingType;
        this.setupTitle();
    }

    private void setupTitle() {
        StringBuilder title = new StringBuilder();
        title.append(meetingType.name().toLowerCase().replace("_", " "));
        title.append(" meetings");

        chart.setTitle(title.toString());
    }

    @Override
    public void update(List<S> population) {
        throw new UnsupportedOperationException("Requires reference to algorithm object");
    }

    @Override
    public void update(List<S> population, String seriesName) {
        throw new UnsupportedOperationException("Requires reference to algorithm object");
    }

    @Override
    public void update(List<S> population, String seriesName, JMetal5BaseEMAS emas){
        if (isItTimeForUpdate(seriesName, Constants.MEETINGS_FREQUENCY)) {
            xValues.get(seriesName).add(iterationCounter.get(seriesName));
            yValues.get(seriesName).add(getMeetingCounterValue(emas));

            getChart().updateXYSeries(
                    seriesName,
                    xValues.get(seriesName),
                    yValues.get(seriesName),
                    null);
        }
        incrementIterationCounterFor(seriesName);
    }

    private int getMeetingCounterValue(JMetal5BaseEMAS<S> emas) {
        if (meetingType.isIAmBetter())
            return emas.getImBetterMeetingTypeCounter();
        else if (meetingType.isNeitherIsBetter())
            return emas.getNeitherIsBetterMeetingTypeCounter();

        return -1;
    }
}
