package network.contorller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
public class InfoContorller {

        @RequestMapping("/pc/message")
        public  String senPcMessage(String info){
            return "sss";
        }

}
