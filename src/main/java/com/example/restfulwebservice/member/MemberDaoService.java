package com.example.restfulwebservice.member;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MemberDaoService {
    private static List<Member> members = new ArrayList<>();

    private static Integer memberCount = 3;

    static {
        members.add(new Member(1, "Kenneth", LocalDateTime.now(), "pass1", "701010-1111111"));
        members.add(new Member(2, "Alice", LocalDateTime.now(), "pass2", "801010-2222222"));
        members.add(new Member(3, "Elena", LocalDateTime.now(), "pass3", "901010-3333333"));
    }

    public List<Member> findAll() {
        return members;
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            member.setId(++memberCount);
        }
        members.add(member);
        return member;
    }

    public Member findOne(int id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    public Member deleteById(int id) {
        Iterator<Member> iterator = members.iterator();

        while (iterator.hasNext()) {
            Member member = iterator.next();

            if (member.getId() == id) {
                iterator.remove();
                return member;
            }
        }

        return null;
    }
}
