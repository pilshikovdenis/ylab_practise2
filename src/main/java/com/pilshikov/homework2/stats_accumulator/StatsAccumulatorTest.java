package com.pilshikov.homework2.stats_accumulator;

public class StatsAccumulatorTest {
    public static void main(String[] args) {
        StatsAccumulator statsAccumulator = new StatsAccumulatorImpl();
        statsAccumulator.add(2);
        statsAccumulator.add(18);
        statsAccumulator.add(-5);
        statsAccumulator.add(5);
        statsAccumulator.add(28);

        System.out.println("min " + statsAccumulator.getMin());
        System.out.println("max " + statsAccumulator.getMax());
        System.out.println("count " + statsAccumulator.getCount());
        System.out.println("avg " + statsAccumulator.getAvg());


    }
}
