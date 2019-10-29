package org.uma.jmetal.algorithm.multiobjective.lemas.Agents.Utils;

@FunctionalInterface
public interface ReproCondition {
    boolean canReproduce(double resourceLevel);
}
