package com.laptevn.statistics;

import com.laptevn.statistics.core.OutdatedTransactionException;
import com.laptevn.statistics.core.StatisticsCalculator;
import com.laptevn.statistics.core.TimedStatisticsCalculator;
import com.laptevn.statistics.core.TimestampProvider;
import com.laptevn.statistics.entities.Statistic;
import com.laptevn.statistics.entities.Transaction;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class TimedStatisticsCalculatorTest {
    @Test(expected = OutdatedTransactionException.class)
    public void addOutdatedTransaction() throws OutdatedTransactionException {
        long timeLimit = 1;
        long currentTimestamp = 10;
        TimedStatisticsCalculator statisticsCalculator = new TimedStatisticsCalculator(
                new StatisticsCalculator(), timeLimit, () -> currentTimestamp);

        long transactionTimestamp = 8;
        statisticsCalculator.addTransaction(new Transaction(0, transactionTimestamp));
    }

    @Test
    public void statisticsWithOutdatedTransactions() throws OutdatedTransactionException {
        long timeLimit = 10;
        ConfigurableTimestampProvider timestampProvider = new ConfigurableTimestampProvider();
        TimedStatisticsCalculator statisticsCalculator = new TimedStatisticsCalculator(new StatisticsCalculator(), timeLimit, timestampProvider);

        timestampProvider.setCurrentTimestamp(100);
        statisticsCalculator.addTransaction(new Transaction(1, 90));
        statisticsCalculator.addTransaction(new Transaction(2, 91));
        statisticsCalculator.addTransaction(new Transaction(3, 93));

        timestampProvider.setCurrentTimestamp(102);
        statisticsCalculator.addTransaction(new Transaction(4, 102));

        Statistic statistic = statisticsCalculator.getStatistic().get();
        assertThat(statistic.getCount(), is(equalTo(2L)));
        assertThat(statistic.getAvg(), is(equalTo(3.5)));
        assertThat(statistic.getMax(), is(equalTo(4.0)));
        assertThat(statistic.getMin(), is(equalTo(3.0)));
        assertThat(statistic.getSum(), is(equalTo(7.0)));
    }

    @Test
    public void noStatistics() {
        TimedStatisticsCalculator statisticsCalculator = new TimedStatisticsCalculator(
                new StatisticsCalculator(), 1, () -> 2);
        assertFalse(statisticsCalculator.getStatistic().isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void transactionFromFuture() throws OutdatedTransactionException {
        long timeLimit = 1;
        long currentTimestamp = 10;
        TimedStatisticsCalculator statisticsCalculator = new TimedStatisticsCalculator(
                new StatisticsCalculator(), timeLimit, () -> currentTimestamp);

        long transactionTimestamp = 20;
        statisticsCalculator.addTransaction(new Transaction(0, transactionTimestamp));
    }

    private static class ConfigurableTimestampProvider implements TimestampProvider {
        private long currentTimestamp;

        @Override
        public long getCurrentTimestamp() {
            return currentTimestamp;
        }

        public void setCurrentTimestamp(long currentTimestamp) {
            this.currentTimestamp = currentTimestamp;
        }
    }
}