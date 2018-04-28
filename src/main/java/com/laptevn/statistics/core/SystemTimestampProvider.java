package com.laptevn.statistics.core;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Retrieves a timestamp basing on current time in UTC.
 */
@Component
public class SystemTimestampProvider implements TimestampProvider {
    @Override
    public long getCurrentTimestamp() {
        return Timestamp.from(Instant.now()).getTime();
    }
}