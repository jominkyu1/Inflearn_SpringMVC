package spring.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.springmvc.basic.HelloData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("NAME: {} AGE : {}", username, age);

        response.getWriter().write("OK! V1");
    }

    // @Controller 어노테이션사용시 String 반환은 View를 찾기때문에 ResponseBody 어노테이션을통해 뷰로 가지않고 Body로 바로 응답
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam(value = "username") String memberName,
            @RequestParam(value = "age") int memberAge
            //Servlet의 HttpServletRequest request.getParameter와 동일
    ) {
        log.info("NAME: {} AGE : {}", memberName, memberAge);

        return "OK! V2";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age
            //쿼리 파라미터와 변수명이 동일하면 @RequestParam의 value값 생략가능
    ) {
        log.info("NAME: {} AGE : {}", username, age);

        return "OK! V3";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(
            String username,
            int age
            // String, int, Integer 등의 단순타입이면 @RequestParam또한 생략가능하나 너무 간소화하는것도 좋아보이진 않음
    ) {
        log.info("NAME: {} AGE : {}", username, age);

        return "OK! V4";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            // required는 기본값 true, 이 파라미터들이 넘어오지않으면 Client에서 Bad Request(400) 오류가남
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age
            // false로 설정후 요청파라미터가 넘어오지않으면 null값이 들어옴
            // 기본자료형(primitive type)일 경우 null값이 들어올 수 없어 서버오류(500)가 나므로 따로 처리를 해주어야함
    ) {
        log.info("NAME: {} AGE : {}", username, age);

        return "OK! Required";
    }

    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age
            //defaultValue값을 설정해놓으면 required는 사실상 필요가 없어서 생략이 가능함
            //defaultValue는 null이 아닌 빈 문자열도 defaultValue로 치환해줌.
            //ex) http://localhost:8080/request-param-default?username=
    ) {
        log.info("NAME: {} AGE : {}", username, age);

        return "OK! Default";
    }

    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(
            //하나의 키에 여러가지 밸류값이 들어가있는 경우 MultiValueMap으로 받으면 됨
            @RequestParam Map<String, Object> paramMap
            ) {
        log.info("NAME: {} AGE : {}", paramMap.get("username"), paramMap.get("age"));

        return "OK! Map";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-classic")
    public String modelAttributeClassic(@RequestParam String username, @RequestParam int age) {
        HelloData helloData = new HelloData();
        helloData.setUsername(username);
        helloData.setAge(age);

        log.info(helloData.toString()); //Lombok에 의해 override된 toString 호출 (선언된 멤버변수 출력)

        return "OK! Attribute Classic";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        //HelloData 객체 프로퍼티(멤버변수)의 setter를 호출해서 파타미러값을 입력(바인딩)함.
        //ex) 파라미터 이름이 username이면 setUsername() 메서드를 찾아서 호출함.

        log.info(helloData.toString());
        return "OK! Attribute V1";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) { //@ModelAttribute도 생략가능
        log.info(helloData.toString());
        return "OK! Attribute V2";
    }

    /*
    *   스프링은 @RequestParam과 @ModelAttribute 모두 생략이 가능하다.
    *   생략시 String, int, Integer같은 단순타입은 @RequestParam으로 인식
    *   나머지는 @ModelAttribute로 인식. (단, argument resolver로 지정해둔 타입은 예외) 
    * */
}
