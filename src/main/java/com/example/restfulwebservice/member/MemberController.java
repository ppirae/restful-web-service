package com.example.restfulwebservice.member;

import com.example.restfulwebservice.exception.MemberNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class MemberController {

    private MemberDaoService service;

    public MemberController(MemberDaoService service) {
        this.service = service;
    }

    @GetMapping("/members")
    public List<Member> retrieveAllMembers() {
        return service.findAll();
    }

    // GET /members/1 or /members/10
    @GetMapping("/members/{id}")
    public Member retrieveMember(@PathVariable int id) {
        Member member = service.findOne(id);

        if (member == null) {
            throw new MemberNotFoundException(String.format("ID[%s] not found", id));
        }

        return member;
    }

    @PostMapping("/members")
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        Member savedMember = service.save(member);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(savedMember.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/members/{id}")
    public void deleteUser(@PathVariable int id) {
        Member member = service.deleteById(id);

        if (member == null) {
            throw new MemberNotFoundException(String.format("ID[%s] not found", id));
        }
    }
}
