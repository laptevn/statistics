package com.laptevn.statistics.core;

import com.laptevn.statistics.entities.Statistic;
import com.laptevn.statistics.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Calculates statistics for transactions in configured interval.
 *
 * Implementation is thread safe.
 */
@Component
public class TimedStatisticsCalculator {
    private final StatisticsCalculator statisticsCalculator;
    private final long timeLimitInMilliseconds;
    private final TimestampProvider timestampProvider;
    private final Deque<Transaction> transactions = new LinkedList<>();

    @Autowired
    public TimedStatisticsCalculator(
            StatisticsCalculator statisticsCalculator, @Value("${timeLimit}") long timeLimitInMilliseconds, TimestampProvider timestampProvider) {
        this.statisticsCalculator = statisticsCalculator;
        this.timeLimitInMilliseconds = timeLimitInMilliseconds;
        this.timestampProvider = timestampProvider;
    }

    public void addTransaction(Transaction transaction) throws OutdatedTransactionException {
        validateTransaction(transaction);

        synchronized (this) {
            purgeTransactions();

            transactions.addLast(transaction);
            statisticsCalculator.add(transaction.getAmount());
        }
    }

    public Optional<Statistic> getStatistic() {
        synchronized (this) {
            purgeTransactions();
            return statisticsCalculator.getCount() > 0 ? Optional.of(buildStatistic()) : Optional.empty();
        }
    }

    private void validateTransaction(Transaction transaction) throws OutdatedTransactionException {
        if (isTransactionOutdated(transaction)) {
            throw new OutdatedTransactionException(String.format("%d is outdated transaction", transaction.getTimestamp()));
        }

        if (transaction.getTimestamp() > timestampProvider.getCurrentTimestamp()) {
            throw new IllegalArgumentException("Transaction cannot be from future");
        }
    }

    private boolean isTransactionOutdated(Transaction transaction) {
        return transaction.getTimestamp() < timestampProvider.getCurrentTimestamp() - timeLimitInMilliseconds;
    }

    private void purgeTransactions() {
        Iterator<Transaction> limitPosition = transactions.iterator();
        while (limitPosition.hasNext()) {
            Transaction transaction = limitPosition.next();
            if (!isTransactionOutdated(transaction)) {
                break;
            }

            limitPosition.remove();
            statisticsCalculator.remove(transaction.getAmount());
        }
    }

    private Statistic buildStatistic() {
        return new Statistic()
                .setAvg(statisticsCalculator.getAverage())
                .setCount(statisticsCalculator.getCount())
                .setMax(statisticsCalculator.getMaximum())
                .setMin(statisticsCalculator.getMinimum())
                .setSum(statisticsCalculator.getSum());
    }
}