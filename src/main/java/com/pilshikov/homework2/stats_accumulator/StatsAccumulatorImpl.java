package com.pilshikov.homework2.stats_accumulator;

public class StatsAccumulatorImpl implements StatsAccumulator{

    private int min;
    private int max;
    private int summ;
    private int count = 0;


    @Override
    public void add(int value) {
        if (count == 0) {
            this.min = value;
            this.max = value;
        } else {
            if (this.min > value) {
                this.min = value;
            }
            if (this.max < value) {
                this.max = value;
            }
        }

        count++;
        summ += value;
    }

    @Override
    public int getMin() {
        return this.min;
    }

    @Override
    public int getMax() {
        return this.max;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Double getAvg() {
        return (summ * 1.0) / count ;
    }
}
