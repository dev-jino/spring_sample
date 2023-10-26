package com.example.news;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/news")
public class NewsWebController {
  final NewsDAO dao;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${news.imgdir}")
  String imgDir;
  
  @Autowired
  public NewsWebController(NewsDAO dao) {
    this.dao = dao;
  }
  
  // 등록, 목록보기, 삭제, 상세보기
  @GetMapping("/list")
  public String list(Model model) {
    List<News> newsList;
    try {
      newsList = dao.getAll();
      model.addAttribute("newsList", newsList);
    } catch (SQLException e) {
      e.printStackTrace();
      logger.info("뉴스 목록 가져오기에서 문제 발생");
      model.addAttribute("error", "뉴스 목록 보기가 정상정으로 처리되지 않았습니다.");
    }

    return "news/newsList";
  }

  @GetMapping("/{aid}")
  public String getNews(@PathVariable int aid, Model model) {
    try {
      News news = dao.getNews(aid);
      model.addAttribute("news", news);
    } catch (SQLException e) {
      e.printStackTrace();
      logger.warn("뉴스를 가져오는 과정에서 문제 발생");
      model.addAttribute("error", "뉴스를 정상적으로 가져오지 못했습니다.");
    }

    return "news/newsView";
  }

  @PostMapping("/add")
  public String addNews(@ModelAttribute News news, Model model, @RequestParam("file")MultipartFile file) {
    try {
      // 저장 파일 객체 생성
      File dest = new File(imgDir + "/" + file.getOriginalFilename());
      // 파일 저장
      file.transferTo(dest);
      // News 객체에 파일 이름 저장
      news.setImg("/img/" + dest.getName());
      dao.addNews(news);
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("뉴스 추가 과정에서 문제 발생!");
      model.addAttribute("error", "뉴스가 정상적으로 등록되지 않았습니다.");
    }

    return "redirect:/news/list";
  }

  @GetMapping("/delete/{aid}")
  public String delNews(@PathVariable int aid, Model model) {
    try {
      dao.delNews(aid);
    } catch (SQLException e) {
      e.printStackTrace();
      logger.info("뉴스 삭제 과정에서 문제 발생!");
      model.addAttribute("error", "뉴스가 정상적으로 등록되지 않았습니다.");
    }

    return "redirect:/news/list";
  }
}

