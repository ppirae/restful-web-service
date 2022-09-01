package com.example.restfulwebservice.member;

import com.example.restfulwebservice.exception.MemberNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminMemberController {

    private MemberDaoService service;

    public AdminMemberController(MemberDaoService service) {
        this.service = service;
    }

    @GetMapping("/members")
    public MappingJacksonValue retrieveAllMembers() {
        List<Member> members = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(members);
        mapping.setFilters(filters);

        return mapping;
    }

    // GET /admin/members/1 or /admin/v1/members/10
//    @GetMapping("/v1/members/{id}")  -> URI를 이용한 버전 관리
//    @GetMapping(value = "/members/{id}/", params = "version=1")  -> Request Parameter를 이용한 버전 관리
//    @GetMapping(value = "/members/{id}", headers="X-API-VERSION=1")    -> Header를 이용한 버전 관리
    @GetMapping(value = "/members/{id}", produces = "application/vnd.company.appv1+json")  // -> MIME 타입을 이용한 버전 관리
    public MappingJacksonValue retrieveMemberV1(@PathVariable int id) {
        Member member = service.findOne(id);

        if (member == null) {
            throw new MemberNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(member);
        mapping.setFilters(filters);

        return mapping;
    }

//    @GetMapping("/v2/members/{id}")   -> URI를 이용한 버전 관리
//    @GetMapping(value = "/members/{id}/", params = "version=2")   -> Request Parameter를 이용한 버전 관리
//    @GetMapping(value = "/members/{id}", headers="X-API-VERSION=2")    -> Header를 이용한 버전 관리
    @GetMapping(value = "/members/{id}", produces = "application/vnd.company.appv2+json")  // -> MIME 타입을 이용한 버전 관리
    public MappingJacksonValue retrieveMemberV2(@PathVariable int id) {
        Member member = service.findOne(id);

        if (member == null) {
            throw new MemberNotFoundException(String.format("ID[%s] not found", id));
        }

        // User -> User2
        MemberV2 userV2 = new MemberV2();
        BeanUtils.copyProperties(member, userV2); // id, name, joinDate, password, ssn
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }
}
