package org.uma.jmetal.runner.multiobjective;/*
package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5ProgressiveEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.ParentToChildAreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.PausableChartWrapper;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.IF_BETTER_AND_COULD_NOT_KNOW;
import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.IF_NOT_WORSE;

public class JMetal5EMASLoadRunner {

    public static void main(String[] args) throws JMetalException {

        String informationMessage = "Choose all your genotypes at once for all the Algorithms you intend to run at once. \n" +
                "For example choose all .ser files whose names contain 'NotWorse' and 'randomSeed_NotWorse' etc. for every algorithm.\n" +
                "If you select auto chose last available iteration from selected folder will be loaded (for all Algorithms).";

        JOptionPane.showMessageDialog(null, informationMessage, "Instruction!", JOptionPane.INFORMATION_MESSAGE);

        ArrayList<File> genotypeFiles = null;
        String path;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(600, 400));
        fileChooser.setCurrentDirectory(new java.io.File(".\\generated_genotypes\\"));
        fileChooser.setMultiSelectionEnabled(true);
        for (Component c : fileChooser.getComponents()) changeButtonText(c, "Cancel", "Auto choose");
        int returnCode = fileChooser.showOpenDialog(null);
        if (returnCode == JFileChooser.APPROVE_OPTION)
            genotypeFiles = new ArrayList<>(Arrays.asList(fileChooser.getSelectedFiles()));
        path = fileChooser.getCurrentDirectory().getPath();
        File folder = new File(path);
        Map<String, File> randomSeedFiles = new HashMap<>();
        ArrayList<File> allFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(folder.listFiles())));

        if(genotypeFiles == null) {
            System.out.println("No files were chosen. Auto choose from " + path);
            genotypeFiles = allFiles;
            for (int i = genotypeFiles.size() - 1; i >=0; i--) {
                if ( genotypeFiles.get(i).isFile()) {
                    System.out.println("File " +  genotypeFiles.get(i).getName());
                } else if ( genotypeFiles.get(i).isDirectory()) {
                    System.out.println("Directory " + genotypeFiles.get(i).getName());
                    genotypeFiles.remove(i);
                }
            }
            if(genotypeFiles.size() == 0) {
                System.out.println("No files found.");
                return;
            }
        }
        List<File> itemsToRemove = new ArrayList<>();
        for (File file : allFiles) {
            String fileName = file.getName().replaceAll("_\\d+", "");
            switch (fileName) {
                case "randomAgentSeed_Better":
                case "randomAgentSeed_NotWorse":
                case "randomJMetalSeed_Better":
                case "randomJMetalSeed_NotWorse":
                    System.out.println("Random seed: " + file);
                    randomSeedFiles.put(fileName, file);
                    itemsToRemove.add(file);
                    break;
                default:
                    break;
            }
        }
        for (File item : itemsToRemove) {
            genotypeFiles.remove(item);
            allFiles.remove(item);
        }



        List<Algorithm> algorithmsToRun = new ArrayList<>();
        List<String> algorithmsNames = new ArrayList<>();
        int maxIteration = -1;
        for (int i = 0; i < genotypeFiles.size(); i++) {
            int iteration = Integer.parseInt(genotypeFiles.get(i).getName().split("iteration")[1]
                    .replaceAll("[^0-9]", ""));
            if(iteration>maxIteration)
                maxIteration = iteration;
        }
        if(maxIteration == -1){
            System.out.println("No iterations found.");
            return;
        }
        System.out.println("Load iteration with number: " + maxIteration);
        for (int i = 0; i < genotypeFiles.size(); i++) {
            int iteration = Integer.parseInt(genotypeFiles.get(i).getName().split("iteration")[1]
                    .replaceAll("[^0-9]", ""));
            if(iteration == maxIteration) {
                String algorithmName = genotypeFiles.get(i).getName().replaceAll("_iteration_\\d+.ser", "");
                JMetal5ProgressiveEMAS emas;
                if (!algorithmsNames.contains(algorithmName) && !algorithmName.contains("EMAS")) {
                    System.out.println("Load from: " + genotypeFiles.get(i).getName());

                    switch (algorithmName) {
                        case "Better":
                            emas = new JMetal5ProgressiveEMAS(algorithmName, IF_BETTER_AND_COULD_NOT_KNOW, false,
                                    new AreaUnderControlComparator(), new ParentToChildAreaUnderControlComparator());
                            emas.setLoadedState();
                            break;
                        case "NotWorse":
                            emas = new JMetal5ProgressiveEMAS(algorithmName,
                                    IF_NOT_WORSE, false,
                                    new AreaUnderControlComparator(), new ParentToChildAreaUnderControlComparator());
                            emas.setLoadedState();
                            break;
                        default:
                            throw new JMetalException("Error finding correct algorithm");
                    }
                    emas.loadSerializedRandomSeed(randomSeedFiles.get("randomAgentSeed_" + algorithmName), maxIteration);
                    emas.loadSerializedRandomSeed(randomSeedFiles.get("randomJMetalSeed_" + algorithmName), maxIteration);
                    emas.loadSerializedEMAS(new File(path + "\\" + algorithmName + "_EMAS_iteration_"+ maxIteration + ".ser"));
                    emas.loadSerializedGenotype(genotypeFiles.get(i));

                    algorithmsToRun.add(emas);
                    algorithmsNames.add(algorithmName);
                }
            }
        }


*/
/*        PausableChartWrapper chartWrapper = new PausableChartWrapper(algorithmsToRun, Constants.NUMBER_OF_DECISION_VARIABLES_TO_SHOW);
        algorithmsToRun.forEach(algorithm -> ((Measurable) algorithm).getMeasureManager()
                .getPushMeasure("currentPopulation")
                .register(population ->
                        chartWrapper.updateChart((List<DoubleSolution>) population, algorithm.getName(), algorithm, algorithmsToRun.indexOf(algorithm))));

        algorithmsToRun
                .parallelStream()
                .forEach(algorithm -> new AlgorithmRunner.Executor(algorithm)
                        .execute());*//*

    }


    public static void changeButtonText (Component c, String original, String change) {

        if (c instanceof JButton) {
            JButton b = (JButton) c;
            if (b.getText() != null && b.getText().equalsIgnoreCase(original))
                b.setText(change);
        } else if (c instanceof Container) {
            Container cont = (Container) c;
            for (int i = 0; i < cont.getComponents().length; i++) {
                changeButtonText (cont.getComponent(i), original, change);
            }
        }
    }
}

*/
