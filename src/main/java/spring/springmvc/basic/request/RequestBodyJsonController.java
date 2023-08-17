package spring.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.springmvc.basic.HelloData;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username": "홍길동", "age": 20}
 * content-type: application/json
 */
@Slf4j
@Controller
public class RequestBodyJsonController {
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messagebody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("OK! Request body json v1");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        //Requestbody 어노테이션을통해 받아와서 바이트->UTF-8형식 변환등의 작업을 하지않아도됨
        log.info("messagebody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "OK! Request body json v2";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    //@ModelAttribute는 생략하여 사용하기도했지만, 아래의 @RequestBody는 생략 불가능
    //생략하면 @ModelAttriute가 적용되어, Body가아닌 요청 파라미터를 처리하게됨
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {
        //MappingJackson2HttpMessageConverter가 아래문장 내부적으로 처리
        //HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "OK! Request body json v3";
    }

    //주의: content-type이 application/json이여야 이에맞는 컨버터가 호출이됨

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> data) {
        HelloData helloData = data.getBody();
        log.info("username: {} age: {}", helloData.getUsername(), helloData.getAge());

        return "OK! Request body json v4";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
        log.info("username: {} age: {}", helloData.getUsername(), helloData.getAge());
        return helloData;
        //반환도 들어온 자료형 그대로 반환가능 단, 클라이언트 Accept: application/json
    }
}
