package com.laptevn.statistics.controllers;

import com.laptevn.statistics.core.TimedStatisticsCalculator;
import com.laptevn.statistics.entities.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Facade for operations over statistics for end users.
 */
@RestController
public class StatisticsController {
    private final TimedStatisticsCalculator statisticsCalculator;

    @Autowired
    public StatisticsController(TimedStatisticsCalculator statisticsCalculator) {
        this.statisticsCalculator = statisticsCalculator;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getStatistics() {
        Optional<Statistic> statistic = statisticsCalculator.getStatistic();
        return statistic.isPresent() ? new ResponseEntity(statistic.get(), HttpStatus.OK) : new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}