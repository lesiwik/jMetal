//  OMOPSORunner.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.core.Algorithm;
import org.uma.jmetal.core.Problem;
import org.uma.jmetal.core.SolutionSet;
import org.uma.jmetal.metaheuristic.multiobjective.omopso.OMOPSO;
import org.uma.jmetal.operator.mutation.Mutation;
import org.uma.jmetal.operator.mutation.NonUniformMutation;
import org.uma.jmetal.operator.mutation.UniformMutation;
import org.uma.jmetal.problem.Kursawe;
import org.uma.jmetal.problem.ProblemFactory;
import org.uma.jmetal.qualityIndicator.QualityIndicator;
import org.uma.jmetal.util.Configuration;
import org.uma.jmetal.util.JMetalException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class for configuring and running the OMOPSO algorithm
 */
public class OMOPSORunner {
  public static Logger logger_;      
  public static FileHandler fileHandler_; 

  /**
   * @param args Command line arguments. The first (optional) argument specifies
   *             the problem to solve.
   * @throws org.uma.jmetal.util.JMetalException
   * @throws IOException
   * @throws SecurityException Usage: three options
   *                           - org.uma.jmetal.runner.MOCell_main
   *                           - org.uma.jmetal.runner.MOCell_main problemName
   *                           - org.uma.jmetal.runner.MOCell_main problemName ParetoFrontFile
   */
  public static void main(String[] args) throws JMetalException, IOException, ClassNotFoundException {
    Problem problem;
    Algorithm algorithm;
    Mutation uniformMutation;
    Mutation nonUniformMutation;

    QualityIndicator indicators;

    // Logger object and file to store log messages
    logger_ = Configuration.logger_;
    fileHandler_ = new FileHandler("OMOPSO_main.log");
    logger_.addHandler(fileHandler_);

    indicators = null;
    if (args.length == 1) {
      Object[] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0], params);
    } else if (args.length == 2) {
      Object[] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0], params);
      indicators = new QualityIndicator(problem, args[1]);
    } else {
      problem = new Kursawe("Real", 3);
      //problem = new Water("Real");
      //problem = new ZDT4("Real");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    }

    algorithm = new OMOPSO();
    algorithm.setProblem(problem);

    Integer maxIterations = 250;
    Double perturbationIndex = 0.5;
    Double mutationProbability = 1.0 / problem.getNumberOfVariables();

    // Algorithm parameters
    algorithm.setInputParameter("swarmSize", 100);
    algorithm.setInputParameter("archiveSize", 100);
    algorithm.setInputParameter("maxIterations", maxIterations);

    HashMap<String, Object> uniMutationParameters = new HashMap<String, Object>();
    uniMutationParameters.put("probability", mutationProbability);
    uniMutationParameters.put("perturbation", perturbationIndex);
    uniformMutation = new UniformMutation(uniMutationParameters);

    HashMap<String, Object> nonUniMutationParameters = new HashMap<String, Object>();
    nonUniMutationParameters.put("probability", mutationProbability);
    nonUniMutationParameters.put("perturbation", perturbationIndex);
    nonUniMutationParameters.put("maxIterations", maxIterations);
    nonUniformMutation = new NonUniformMutation(nonUniMutationParameters);

    // Add the operator to the algorithm
    algorithm.addOperator("uniformMutation", uniformMutation);
    algorithm.addOperator("nonUniformMutation", nonUniformMutation);

    // Execute the Algorithm 
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Print the results
    logger_.info("Total execution time: " + estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");

    if (indicators != null) {
      logger_.info("Quality indicators");
      logger_.info("Hypervolume: " + indicators.getHypervolume(population));
      logger_.info("GD         : " + indicators.getGD(population));
      logger_.info("IGD        : " + indicators.getIGD(population));
      logger_.info("Spread     : " + indicators.getSpread(population));
      logger_.info("Epsilon    : " + indicators.getEpsilon(population));
    }
  }
}