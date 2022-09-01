package com.example.restfulwebservice.member;

import com.example.restfulwebservice.exception.MemberNotFoundException;
import com.example.restfulwebservice.exception.PostNotFoundException;
import com.example.restfulwebservice.post.Post;
import com.example.restfulwebservice.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jpa")
public class MemberJPAController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/members")
    public List<Member> retrieveAllMember() {
        return memberRepository.findAll();
    }

    @GetMapping("/members/{id}")
    public Optional<Member> retrieveMember(@PathVariable int id) {
        Optional<Member> member = memberRepository.findById(id);

        if (!member.isPresent()) {
            throw new MemberNotFoundException(String.format("ID[%s] not found", id));
        }

        return member;
    }

    @DeleteMapping("/members/{id}")
    public void deleteMember(@PathVariable int id) {
        memberRepository.deleteById(id);
    }

    @PostMapping("/members")
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        Member savedMember = memberRepository.save(member);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMember.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/members/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id) {
        Optional<Member> member = memberRepository.findById(id);

        if (!member.isPresent()) {
            throw new MemberNotFoundException(String.format("ID[%s] not found", id));
        }

        List<Post> posts = member.get().getPosts();

        if (posts.isEmpty()) {
            throw new PostNotFoundException("Not posted yet.");
        }

        return posts;
    }

    @PostMapping("/members/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<Member> member = memberRepository.findById(id);

        if (!member.isPresent()) {
            throw new MemberNotFoundException(String.format("ID[%s] not found", id));
        }

        post.setMember(member.get());
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
