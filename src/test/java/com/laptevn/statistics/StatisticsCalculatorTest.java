package com.laptevn.statistics;

import com.laptevn.statistics.core.StatisticsCalculator;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class StatisticsCalculatorTest {
    @Test(expected = IllegalStateException.class)
    public void removeFromEmptyStatistics() {
        new StatisticsCalculator().remove(1.0);
    }

    @Test
    public void forOneValue() {
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        double value = 2.0;
        statisticsCalculator.add(value);
        assertThat(statisticsCalculator.getCount(), is(equalTo(1L)));
        assertThat(statisticsCalculator.getSum(), is(equalTo(value)));
        assertThat(statisticsCalculator.getAverage(), is(equalTo(value)));
    }

    @Test
    public void noValue() {
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        double value = 2.0;
        statisticsCalculator.add(value);
        statisticsCalculator.remove(value);

        assertThat(statisticsCalculator.getCount(), is(equalTo(0L)));
        assertThat(statisticsCalculator.getSum(), is(equalTo(0.0)));
        assertThat(statisticsCalculator.getAverage(), is(equalTo(0.0)));
    }

    @Test
    public void twoValues() {
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        double value1 = 2.0;
        statisticsCalculator.add(value1);

        double value2 = 3.0;
        statisticsCalculator.add(value2);

        assertThat(statisticsCalculator.getCount(), is(equalTo(2L)));
        assertThat(statisticsCalculator.getSum(), is(equalTo(5.0)));
        assertThat(statisticsCalculator.getAverage(), is(equalTo(2.5)));

        statisticsCalculator.remove(value1);
        assertThat(statisticsCalculator.getCount(), is(equalTo(1L)));
        assertThat(statisticsCalculator.getSum(), is(equalTo(value2)));
        assertThat(statisticsCalculator.getAverage(), is(equalTo(value2)));
    }

    @Test
    public void manyValues() {
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        statisticsCalculator.add(1.1);
        statisticsCalculator.add(5.0);
        statisticsCalculator.add(13.6);

        assertThat(statisticsCalculator.getCount(), is(equalTo(3L)));
        assertThat(statisticsCalculator.getSum(), is(equalTo(19.7)));
        assertThat(String.format("%.2f", statisticsCalculator.getAverage()), is(equalTo("6.57")));
    }

    @Test(expected = IllegalStateException.class)
    public void maximumWithoutData() {
        new StatisticsCalculator().getMaximum();
    }

    @Test(expected = IllegalStateException.class)
    public void minimumWithoutData() {
        new StatisticsCalculator().getMinimum();
    }

    @Test
    public void maxMinWithElementsAddingAndRemoval() {
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        statisticsCalculator.add(8.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(8.0)));
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(8.0)));

        statisticsCalculator.add(5.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(8.0)));
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(5.0)));

        statisticsCalculator.remove(8.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(5.0)));
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(5.0)));

        statisticsCalculator.add(0.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(5.0)));
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(0.0)));

        statisticsCalculator.add(6.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(6.0)));
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(0.0)));

        statisticsCalculator.remove(5.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(6.0)));

        statisticsCalculator.remove(0.0);
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(6.0)));

        statisticsCalculator.add(1.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(6.0)));
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(1.0)));

        statisticsCalculator.add(9.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(9.0)));
    }

    @Test
    public void maxMinWithDuplicatedElements() {
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        statisticsCalculator.add(8.0);
        statisticsCalculator.add(8.0);
        statisticsCalculator.add(1.5);
        statisticsCalculator.add(1.5);

        assertThat(statisticsCalculator.getMaximum(), is(equalTo(8.0)));
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(1.5)));

        statisticsCalculator.remove(8.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(8.0)));

        statisticsCalculator.remove(8.0);
        assertThat(statisticsCalculator.getMaximum(), is(equalTo(1.5)));

        statisticsCalculator.remove(1.5);
        assertThat(statisticsCalculator.getMinimum(), is(equalTo(1.5)));

        statisticsCalculator.remove(1.5);
        assertThat(statisticsCalculator.getCount(), is(equalTo(0L)));
    }
}