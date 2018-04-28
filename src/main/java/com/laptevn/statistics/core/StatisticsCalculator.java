package com.laptevn.statistics.core;

import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Calculates the following parameters for incoming data:
 * - Sum of existing values
 * - Average value
 * - Count of existing values
 * - Maximum value
 * - Minimum value
 *
 * Implementation is not thread safe.
 */
@Component
public class StatisticsCalculator {
    private double sum;
    private double average;
    private long count;
    private final Deque<Double> minimums = new LinkedList<>();
    private final Deque<Double> maximums = new LinkedList<>();

    public void add(double value) {
        sum += value;
        count++;
        average += (value - average) / count;

        updateMaximumAfterAdd(value);
        updateMinimumAfterAdd(value);
    }

    private void updateMaximumAfterAdd(double value) {
        while (!maximums.isEmpty() && maximums.getLast() < value) {
            maximums.removeLast();
        }
        maximums.addLast(value);
    }

    private void updateMinimumAfterAdd(double value) {
        while (!minimums.isEmpty() && minimums.getLast() > value) {
            minimums.removeLast();
        }
        minimums.addLast(value);
    }

    public void remove(double value) {
        if (count <= 0) {
            throw new IllegalStateException("Nothing to remove");
        }

        average = count != 1 ? (count * average - value) / (count - 1) : 0;
        sum -= value;
        count--;

        updateMaximumAfterRemove(value);
        updateMinimumAfterRemove(value);
    }

    private void updateMaximumAfterRemove(double value) {
        if (maximums.getFirst() == value) {
            maximums.removeFirst();
        }
    }

    private void updateMinimumAfterRemove(double value) {
        if (minimums.getFirst() == value) {
            minimums.removeFirst();
        }
    }

    public double getSum() {
        return sum;
    }

    public double getAverage() {
        return average;
    }

    public long getCount() {
        return count;
    }

    public double getMaximum() {
        if (maximums.isEmpty()) {
            throw new IllegalStateException("No data to find maximum");
        }

        return maximums.getFirst();
    }

    public double getMinimum() {
        if (minimums.isEmpty()) {
            throw new IllegalStateException("No data to find minimum");
        }

        return minimums.getFirst();
    }
}