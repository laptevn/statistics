package com.laptevn.statistics.controllers;

import com.laptevn.statistics.core.OutdatedTransactionException;
import com.laptevn.statistics.core.TimedStatisticsCalculator;
import com.laptevn.statistics.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Facade for operations over transactions for end users.
 */
@RestController
public class TransactionsController {
    private final Logger logger = LoggerFactory.getLogger(TransactionsController.class);
    private final TimedStatisticsCalculator statisticsCalculator;

    @Autowired
    public TransactionsController(TimedStatisticsCalculator statisticsCalculator) {
        this.statisticsCalculator = statisticsCalculator;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTransaction(@RequestBody Transaction transaction) {
        logger.info("Adding transaction {}", transaction);

        try {
            statisticsCalculator.addTransaction(transaction);
        } catch (OutdatedTransactionException e) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}