package com.example.sample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class TestRestController {
  @GetMapping("/hello")
  public String sayHello() {
    return "hello";
  }

  @GetMapping("/hello2")
  public String hello2(@RequestParam(value="msg",
        required = false, defaultValue = "default hello") String msg) {
    return msg;
  }

  @PostMapping("/hello3/{id}")
  public String hello3(@PathVariable("id") int id) {
    return "hello " + id;
  }

  @GetMapping("/hello4")
  public HashMap<String, String> hello4() {
    HashMap<String, String> map = new HashMap<>();
    map.put("id", "hong");
    map.put("name", "홍길동");
    map.put("nationality", "Korea");

    return map;
  }

}
