package com.arthurmountain.personalwebsite.apis.v1.controllers.posts;

import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/posts")
public class Route {

  @GetMapping
  public Object getPosts() {
    return new HashMap<>(Map.of("posts", "all posts"));

  }

  @GetMapping("/{id}")
  public String getPost() {
    return "post with id";
  }
}
