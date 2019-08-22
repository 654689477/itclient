package network;

import network.compuer.ComputerInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
      String license=  ComputerInfo.getLicense();
        //启动上传电脑信息和注册码，
        if(license!=null && !license.isEmpty()){
            ComputerInfo.uploadComputerInfo();
        }

    }

}
