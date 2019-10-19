package org.uma.jmetal.algorithm.multiobjective.lemas.visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.config.MeetingType;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

public class MeetingsChart extends ProgressBaseChart<Integer> {
    private final MeetingType meetingType;

    public MeetingsChart(List<Algorithm> algorithmsToShow, MeetingType meetingType) {
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
    public void update(List<DoubleSolution> population) {
        throw new UnsupportedOperationException("Requires reference to algorithm object");
    }

    @Override
    public void update(List<DoubleSolution> population, String seriesName) {
        throw new UnsupportedOperationException("Requires reference to algorithm object");
    }

    @Override
    public void update(List<DoubleSolution> population, String seriesName, BaseEMAS emas){
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

    private int getMeetingCounterValue(BaseEMAS emas) {
        if (meetingType.isIAmBetter())
            return emas.getImBetterMeetingTypeCounter();
        else if (meetingType.isNeitherIsBetter())
            return emas.getNeitherIsBetterMeetingTypeCounter();

        return -1;
    }
}
