package com.laptevn.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptevn.statistics.core.SystemTimestampProvider;
import com.laptevn.statistics.entities.Statistic;
import com.laptevn.statistics.entities.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class StatisticsITest {
    private MockMvc restClient;
    private ObjectMapper objectMapper;

    @Autowired
    public void setRestClient(MockMvc restClient) {
        this.restClient = restClient;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Test
    public void getStatisticsWithoutTransactions() throws Exception {
        restClient.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addOutdatedTransaction() throws Exception {
        restClient.perform(post("/transactions")
                .content(objectMapper.writeValueAsString(new Transaction(1.2, 123131)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addFutureTransaction() throws Exception {
        restClient.perform(post("/transactions")
                .content(objectMapper.writeValueAsString(new Transaction(1.2, 9524946268210L)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getStatisticsForExistingTransactions() throws Exception {
        SystemTimestampProvider timestampProvider = new SystemTimestampProvider();

        restClient.perform(post("/transactions")
                .content(objectMapper.writeValueAsString(new Transaction(4.0, timestampProvider.getCurrentTimestamp())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        restClient.perform(post("/transactions")
                .content(objectMapper.writeValueAsString(new Transaction(2.0, timestampProvider.getCurrentTimestamp())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Statistic expectedStatistic = new Statistic()
                .setAvg(3.0)
                .setCount(2L)
                .setMax(4.0)
                .setMin(2.0)
                .setSum(6.0);
        restClient.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedStatistic)));
    }
}