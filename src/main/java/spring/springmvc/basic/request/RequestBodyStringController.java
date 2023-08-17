package spring.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messagebody={}", messageBody);

        response.getWriter().write("OK! Request body string v1");
    }

    //서블릿을통해 받아오는대신 inputStream, Writer를 직접받아올 수 있음
    //InputStream(Reader): HTTP 요청 메세지 바디의 내용을 직접 조회
    //OutputStream(Writer): HTTP 응답 메세지의 바디에 직접 결과 출력
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);


        log.info("messagebody={}", messageBody);
        responseWriter.write("OK! Request body string v2");
    }

    //HttpEntity: HTTP header, body 정보를 직접 조회 (HttpMessageConverter를 통해 변한되어 제공)
    //쿼리 파라미터를 조회하는 기능과 관계없음(@RequestParam, @ModelAttribute X)
    //return으로도 body, header 직접 반환가능 (view 조회X)
    //HttpEntity를 상속받은 RequestEntity, ResponseEntity도 같은 기능을 제공
    //RequestEntity: httpMethod, URL정보가 추가, 요청에서 사용
    //ResponseEntity: HTTP 상태코드 설정 가능, 응답에서 사용
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        String messageBody = httpEntity.getBody();

        log.info("messagebody={}", messageBody);

        return new ResponseEntity<>("OK! Request body string v3", HttpStatus.CREATED); // 201 Created return
//        return new HttpEntity<>("OK! Request body string v3"); // 200 OK return
    }


    //헤더가 필요한경우 @RequestHeader
    //여기서 사용된 @RequestBody, @RequestHeader는 @RequestParam, @ModelAttribute와 전혀 관계가 없음.
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV3(@RequestBody String messageBody, @RequestHeader Map<String, String> header) {
        header.keySet().iterator().forEachRemaining(
                key -> log.info("{} --> {}", key, header.get(key))
        );

        log.info("messagebody={}", messageBody);

        return "OK! Request body string v3";
    }
}
