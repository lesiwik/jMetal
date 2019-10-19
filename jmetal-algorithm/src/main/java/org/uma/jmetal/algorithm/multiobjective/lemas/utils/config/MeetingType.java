package org.uma.jmetal.algorithm.multiobjective.lemas.utils.config;

public enum MeetingType {
    I_AM_BETTER, NEITHER_IS_BETTER;

    public boolean isIAmBetter() {
        return this.equals(I_AM_BETTER);
    }

    public boolean isNeitherIsBetter() {
        return this.equals(NEITHER_IS_BETTER);
    }
}
