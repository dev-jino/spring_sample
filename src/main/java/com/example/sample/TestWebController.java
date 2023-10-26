package com.example.sample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class TestWebController {
  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }

  @GetMapping("/hello2")
  @ResponseBody
  public String hello2(@RequestParam(value="msg", required = false) String msg) {
    return msg;
  }

  @PostMapping("/hello3/{id}")
  @ResponseBody
  public String hello3(@PathVariable("id") int id) {
    return "hello " + id;
  }

  @GetMapping("/hello4/{msg}")
  public String hello4(@PathVariable("msg") int msg, Model m) {
    m.addAttribute("msg", msg);
    return "hello";
  }

}
