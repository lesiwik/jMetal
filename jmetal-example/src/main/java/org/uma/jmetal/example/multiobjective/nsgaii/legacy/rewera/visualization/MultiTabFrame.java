package org.uma.jmetal.runner.multiobjective.rewera.visualization;

import org.uma.jmetal.algorithm.Algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiTabFrame {
    private String windowTitle = "Multi-problem Runner";
    private JFrame mainFrame;
    private JTabbedPane tabbedPane;
    private Map<String, SingleProblemChartWrapper<?>> wrappers = new HashMap<>();
    
    public MultiTabFrame(Map<String, List<Algorithm>> algorithmsToShow,
                         int numberOFDecisionVariablesToShow) {
        mainFrame = new JFrame(windowTitle);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        tabbedPane = createTabbedPane(algorithmsToShow, numberOFDecisionVariablesToShow);
        mainFrame.add(tabbedPane, BorderLayout.CENTER);

        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setUndecorated(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private JTabbedPane createTabbedPane(Map<String, List<Algorithm>> algorithms,
                                         int numberOFDecisionVariablesToShow) {
        JTabbedPane pane = new JTabbedPane();
        for (Map.Entry<String, List<Algorithm>> entry : algorithms.entrySet()) {
            SingleProblemChartWrapper<?> wrapper = new SingleProblemChartWrapper<>(entry.getValue(), numberOFDecisionVariablesToShow);
            wrappers.put(entry.getKey(), wrapper);
            String tabTitle = entry.getKey();
            pane.addTab(tabTitle, wrapper.createPanel());
        }
        return pane;
    }

    public SingleProblemChartWrapper<?> getWrapper(String problem) {
        return this.wrappers.get(problem);
    }

    
}
