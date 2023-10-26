package com.example.news;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/news")
public class NewsAPIController {
  final NewsDAO dao;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${news.imgdir}")
  String imgDir;

  @Autowired
  public NewsAPIController(NewsDAO dao) {
    this.dao = dao;
  }

  @GetMapping("/")
  public List<News> list() {
    List<News> newsList = new ArrayList<>();

    try {
      newsList = dao.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
      logger.info("뉴스 목록 가져오기에서 문제 발생");
      // error 객체 추가
    }

    return newsList;
  }

  @GetMapping("/{aid}")
  public News getNews(@PathVariable int aid) {
    News news = new News();
    try {
      news = dao.getNews(aid);
    } catch (SQLException e) {
      e.printStackTrace();
      logger.warn("뉴스를 가져오는 과정에서 문제 발생");
    }

    return news;
  }

  @PostMapping("/add")
  public String addNews(@ModelAttribute News news, Model model, @RequestParam("file") MultipartFile file) {
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
    }

    return "redirect:/api/news";
  }

  @GetMapping("/delete/{aid}")
  public String delNews(@PathVariable int aid) {
    try {
      dao.delNews(aid);
    } catch (SQLException e) {
      e.printStackTrace();
      logger.info("뉴스 삭제 과정에서 문제 발생!");
    }

    return "redirect:/api/news";
  }
}
