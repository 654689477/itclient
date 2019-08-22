package network.demo;


import network.DemoApplication;
import network.compuer.ComputerInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


public class Test {
   // @GetMapping("/admin")
    public  String  say(){
        return "ssssssssssss";
    }
    public static void main(String[] args){

       System.out.println("sss");
    }

}
