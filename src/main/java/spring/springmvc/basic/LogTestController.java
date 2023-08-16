package spring.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//RestController로 선언시 Controller와 다르게 String 리턴시 메세지바디에 직접입력가능 (Controller는 뷰반환)
@RestController 
public class LogTestController {
    
    //롬복이 제공하는 @Slf4j 어노테이션 사용시 log를 선언하지않아도 됨
    private final Logger log = LoggerFactory.getLogger(getClass()); //내 클래스

    @RequestMapping("/log-test")
    public String logTest(){
        String name = "Spring";

        log.trace("trace log={}", name); //위험도 증가
        log.debug("debug log={}", name);
        log.info("info log={}", name);
        log.warn("warn log={}", name);
        log.error("error log={}", name);


        return "ok";
    }
}
