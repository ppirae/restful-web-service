package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value = {"password", "ssn"})
@JsonFilter("UserInfo")
public class User {

    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요. ")
    private String name;

    @Past   //과거 데이터만 올 수 있음
    private LocalDateTime joinDate;

    private String password;

    private String ssn;
}
