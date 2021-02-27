package com.sda.auction.config;

import com.sda.auction.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    // == fields ==
    private final BidService bidService;

    // == constructor ==
    @Autowired
    public SchedulerConfig(BidService bidService) {
        this.bidService = bidService;
    }

    @Scheduled(fixedDelay = 5000)
    public void regularJob() {
        bidService.assignWinners();
    }
}
