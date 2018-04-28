package com.laptevn.statistics.entities;

public class Statistic {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public double getSum() {
        return sum;
    }

    public Statistic setSum(double sum) {
        this.sum = sum;
        return this;
    }

    public double getAvg() {
        return avg;
    }

    public Statistic setAvg(double avg) {
        this.avg = avg;
        return this;
    }

    public double getMax() {
        return max;
    }

    public Statistic setMax(double max) {
        this.max = max;
        return this;
    }

    public double getMin() {
        return min;
    }

    public Statistic setMin(double min) {
        this.min = min;
        return this;
    }

    public long getCount() {
        return count;
    }

    public Statistic setCount(long count) {
        this.count = count;
        return this;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "sum=" + sum +
                ", avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }
}