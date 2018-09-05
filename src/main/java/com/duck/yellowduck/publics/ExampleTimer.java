package com.duck.yellowduck.publics;

import com.duck.yellowduck.domain.service.DirectPrizeService;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExampleTimer
{
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private DirectPrizeService directPrizeService;

    @Scheduled(cron="00 28 15 * * ?")
    public void timerCron() throws Exception {
        System.out.println("current time : " + this.dateFormat.format(new Date()));

        //this.directPrizeService.timedTask();
    }


}
