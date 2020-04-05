package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import lombok.Getter;
import lombok.Setter;
import org.knowm.xchart.CategoryChart;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5MeetingAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.solution.Solution;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class AlgorithmStatistics<S extends Solution<?>> {

    @Setter
    @Getter
    private CategoryChart chart;

    @Getter
    private Map<String, Map<String, Number>> series;

    private StatisticsType statisticsType;

    @Getter
    @Setter
    private String title;

    public enum StatisticsType {
        DOMINATION_LEVEL,

    }

    public AlgorithmStatistics(List<Algorithm<List<S>>> algorithms, String title, StatisticsType statisticsType) {
        series = new ConcurrentHashMap<>();
        this.statisticsType = statisticsType;
//        this.chart = new CategoryChartBuilder().width(500).height(300).theme(Constants.CHART_THEME).build();
//        this.chart.getStyler().setLegendVisible(true);
//        this.chart.setTitle(title);
        this.title = title;
        algorithms.forEach(algorithm -> {
            series.putIfAbsent(algorithm.getName(), new HashMap<>());
        });
    }


    public void updateStats(JMetal5BaseEMAS<S> EMAS)
    {
        switch(statisticsType)
        {
            case DOMINATION_LEVEL:
                updateDominationLevels(EMAS);
                break;
            default:
                throw new IllegalArgumentException("Enum type not specified");
        }

    }

    private void updateDominationLevels(JMetal5BaseEMAS<S> EMAS)
    {
        List<JMetal5Agent<S>> agents = EMAS.getAgents();
        /* Avoids conversion errors for different EMAS algorithms. */
        if(!agents.isEmpty() && !(agents.get(0) instanceof JMetal5MeetingAgent))
            return;

        Map<String, Number> dataRef = series.get(EMAS.getName());
        for(int index = 0; index < agents.size(); index++)
        {
            JMetal5MeetingAgent<S> meetingAgentRef = (JMetal5MeetingAgent<S>) agents.get(index);
            int dominationLevel = meetingAgentRef.getDominationLevel();
            int oldValue = dataRef.getOrDefault(Integer.toString(dominationLevel), 0).intValue();
            dataRef.put(Integer.toString(dominationLevel), ++oldValue);
        }
    }
}
