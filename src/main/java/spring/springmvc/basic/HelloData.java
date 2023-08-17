package spring.springmvc.basic;

import lombok.Data;

//롬복 @Data = @Getter , @Setter , @ToString , @EqualsAndHashCode , @RequiredArgsConstructor 를
//자동으로 적용해준다.

// @RequiredArgsConstructor는 final로 선언된 멤버변수 생성자 자동으로 만들어줌
@Data
public class HelloData {
    private String username;
    private int age;
}
