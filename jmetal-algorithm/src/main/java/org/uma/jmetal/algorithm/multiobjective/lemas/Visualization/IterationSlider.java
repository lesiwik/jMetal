package org.uma.jmetal.algorithm.multiobjective.lemas.Visualization;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.impl.AbstractEMASAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class IterationSlider {

    private JFrame frame;
    private JSlider jSlider;
    //private JButton displayButton;
    private JButton pauseButton;
    private JButton saveIterationsButton;
    public static final int SLIDER_FREQUENCY = 1;
    protected Map<String, Integer> iterationCounter = new HashMap<>();
    public AtomicBoolean isPaused = new AtomicBoolean();
    private Map<String, SavedState> savedStates;
    private List<JMetal5BaseEMAS> currentIterationEmas;
    public AtomicBoolean sliderChanged = new AtomicBoolean();

    public class SavedState {
        List<DoubleSolution> data;
        List<Double> v0;
        List<Double> v1;
        String seriesName;
        Algorithm emas;
        int seriesNumber;

        public SavedState(List<DoubleSolution> data, String seriesName, Algorithm emas, int seriesNumber) {
            this.v0 = data.stream().map(solution -> solution.getObjective(0)).collect(Collectors.toList());
            this.v1 = data.stream().map(solution -> solution.getObjective(1)).collect(Collectors.toList());
            this.data = data;
            this.seriesName = seriesName;
            this.emas = emas;
            this.seriesNumber = seriesNumber;
        }

        public List<Double> getV0() {
            return v0;
        }

        public List<Double> getV1() {
            return v1;
        }

        public List<DoubleSolution> getData() {
            return data;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public Algorithm getEmas() {
            return emas;
        }

        public int getSeriesNumber() {
            return seriesNumber;
        }
    }

    public IterationSlider() {
        savedStates = new HashMap<>();
        initComponents();
    }

    public IterationSlider(Iterable<Algorithm> algorithmsToShow) {
        sliderChanged.getAndSet(false);
        isPaused.getAndSet(false);
        savedStates = new HashMap<>();
        currentIterationEmas = new ArrayList<>();
        //algorithmsToShow.forEach(algorithm -> currentIterationEmas.add((JMetal5BaseEMAS) algorithm)); TODO: Part of not including loading from same iteration.
        initComponents();
        for (Algorithm alg : algorithmsToShow)
            initSeries(alg.getName());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        frame = new JFrame();
        frame = new JFrame();
        jSlider = new JSlider();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jSlider.setMajorTickSpacing(SLIDER_FREQUENCY);
        jSlider.setSnapToTicks(true);
        //jSlider.setPaintLabels(true);
        jSlider.setPaintTicks(true);
        jSlider.setValue(0);
        jSlider.setMaximum(0);
        jSlider.setMinimum(0);
        jSlider.setEnabled(false);
        jSlider.setPreferredSize(new Dimension(1200, 50));

        jSlider.addChangeListener(this::jSlider1StateChanged);

        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        containerPanel.setLayout(new BorderLayout());

        pauseButton = new JButton("Pause algorithm");
        pauseButton.addActionListener(e -> {
            pause();
        });

//        displayButton = new JButton("Display selected");
//        displayButton.addActionListener(e -> {
//            display();
//        });
//        displayButton.setEnabled(false);

        saveIterationsButton = new JButton("Save iterations to SER file");
        saveIterationsButton.addActionListener(e ->
        {
            serializeIterations();
        });
        saveIterationsButton.setEnabled(false);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        controlPanel.add(pauseButton);
        //controlPanel.add(displayButton);
        controlPanel.add(saveIterationsButton);

        containerPanel.add(jSlider, BorderLayout.LINE_START);
        containerPanel.add(controlPanel, BorderLayout.PAGE_END);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(containerPanel, BorderLayout.CENTER);

        frame.pack();
    }// </editor-fold>


    private void serializeIterations() {
        if (!currentIterationEmas.get(0).isDebugMode() || !isPaused.get()) {
            JOptionPane.showMessageDialog(frame, "Debug mode disabled (-debug in parameters). \n Algorithms must also be paused.");
            return;
        }
        int startingIteration;
        int prevIterationsToSave;
        int [] userInput;
        try {
            userInput = userSaveIterationsInput();
            startingIteration = userInput[0];
            prevIterationsToSave = userInput[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(frame, "Integer has to be within the range of iterations");
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please input integer only.");
            return;
        } catch (Exception e) {
            return;
        }


        for (JMetal5BaseEMAS jMetal5BaseEMAS : currentIterationEmas) {
            try {
                jMetal5BaseEMAS.serializeGenotype(startingIteration, prevIterationsToSave);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error saving data");
                System.out.println(e);
                return;
            }
        }

        JOptionPane.showMessageDialog(frame, "Success saving data");


    }


    private String userIterationInputDialog(String message, int currentParameter)
    {
        String userInput = JOptionPane.showInputDialog(frame, message + currentParameter + "\n \t Current selected:" + (jSlider.getValue() + 1) , currentParameter);
        if(userInput == null)
            throw new NullPointerException();
        return userInput;
    }

    private int[] userSaveIterationsInput() throws ArrayIndexOutOfBoundsException, NumberFormatException {
        int currentMaxIteration = iterationCounter.get(currentIterationEmas.get(0).getName());
        int startingIteration = Integer.valueOf(userIterationInputDialog("Which iteration to save? \n \t Last iteration: ", currentMaxIteration));
        if (startingIteration > currentMaxIteration || startingIteration < 0)
            throw new ArrayIndexOutOfBoundsException(startingIteration);

        int prevIterationsToSave = Integer.valueOf(userIterationInputDialog("How many iterations from iteration " + startingIteration + " to save? \n \t Possible max: ", startingIteration + 1));
        if (prevIterationsToSave > startingIteration + 1 || prevIterationsToSave < 0)
            throw new ArrayIndexOutOfBoundsException(prevIterationsToSave);

        return new int[] {startingIteration, prevIterationsToSave};
    }

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {
        sliderChanged.getAndSet(true);
    }

    public void showForm() {
        EventQueue.invokeLater(() -> frame.setVisible(true));
    }

//    public void display() {
//        displayButton.setText(displayButton.getText().equals("Display selected") ? "Show current" : "Display selected");
//        //isPaused = !isPaused;
//        isPaused.getAndSet(!isPaused.get());
//    }

    public void pause() {
        //isPaused = !isPaused;
        isPaused.getAndSet(!isPaused.get());
        currentIterationEmas.forEach(AbstractEMASAlgorithm::pauseIteration);
        if (isPaused.get()) {
            pauseButton.setText("Play algorithm");
            //displayButton.setEnabled(true);
            saveIterationsButton.setEnabled(true);
            jSlider.setEnabled(true);
            sliderChanged.getAndSet(true);
        } else {
            pauseButton.setText("Pause algorithm");
            //displayButton.setEnabled(false);
            saveIterationsButton.setEnabled(false);
            jSlider.setEnabled(false);
            jSlider.setValue(jSlider.getMaximum());
        }
    }


    public void update(List<DoubleSolution> data, String seriesName, Algorithm emas, int seriesNumber) {
        if (isItTimeForUpdate(seriesName, SLIDER_FREQUENCY)) {
            int min = Collections.min(iterationCounter.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
            min = min - min % SLIDER_FREQUENCY;
            if (min != jSlider.getMaximum()) {
                jSlider.setMaximum(min);
                jSlider.setValue(min);
                jSlider.revalidate();
                jSlider.repaint();
            }
            savedStates.put(seriesName + iterationCounter.get(seriesName), new SavedState(data, seriesName, emas, seriesNumber));
        }
        incrementIterationCounterFor(seriesName);
    }

    public int getIteration(){
        return jSlider.getValue();
    }

    public SavedState getSavedState(String seriesName) {
        return savedStates.get(seriesName + String.valueOf(jSlider.getValue()));
    }

    public Map<String, Integer> getIterationCounter(){
        return iterationCounter;
    }

    protected boolean isItTimeForUpdate(String seriesName, int relatedConstant) {
        return (iterationCounter.get(seriesName) % relatedConstant == 0);
    }

    protected void incrementIterationCounterFor(String seriesName) {
        iterationCounter.put(seriesName, iterationCounter.get(seriesName) + 1);
    }

    protected void initSeries(String seriesName) {
        iterationCounter.put(seriesName, 0);
    }
}

