package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import lombok.Getter;
import lombok.Setter;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5MeetingAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistogramChart<S extends Solution<?>> {

    @Setter
    @Getter
    private CategoryChart chart;

    @Getter
    private Map<String, Number> series;


    private HistogramType histogramType;

    public enum HistogramType {
        DOMINATION_LEVEL_TYPE,

    }


    public HistogramChart(String title, HistogramType histogramType) {
        series = new ConcurrentHashMap<>();
        this.histogramType = histogramType;
        this.chart = new CategoryChartBuilder().width(500).height(300).theme(Constants.CHART_THEME).build();
        this.chart.getStyler().setLegendVisible(true);
        this.chart.setTitle(title);
    }


    public void updateSeries(JMetal5BaseEMAS<S> EMAS)
    {
        switch(histogramType)
        {
            case DOMINATION_LEVEL_TYPE:
                updateDominationLevels(EMAS);
                break;
            default:
                throw new IllegalArgumentException("Enum type not specified");
        }

        List<Number> data = new ArrayList<>(series.values());
    }

    private void updateDominationLevels(JMetal5BaseEMAS<S> EMAS)
    {
        List<JMetal5Agent<S>> agents = EMAS.getAgents();
        agents.forEach(agent ->
        {
            JMetal5MeetingAgent<S> meetingAgentRef = (JMetal5MeetingAgent<S>) agent;
            int dominationLevel = meetingAgentRef.getDominationLevel();
            int oldValue = series.getOrDefault(Integer.toString(dominationLevel), 0).intValue();
            series.put(Integer.toString(dominationLevel), ++oldValue);
        });
    }
}
