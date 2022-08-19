package com.example.restfulwebservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//lombok - getter, setter, 생성자, toString 외 다수 기능
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldBean {

    private String message;

}