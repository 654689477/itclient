package network.task;


import network.compuer.ComputerInfo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeTask {

    /**
     * 每俩小时更新天气信息
     */
    @Scheduled(cron="0 */10 * * * ?")    // 24小时执行一次
    public void scheduler() {
        ComputerInfo.uploadComputerInfo();
    }
}
