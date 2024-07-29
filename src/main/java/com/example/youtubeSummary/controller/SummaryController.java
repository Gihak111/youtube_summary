package com.example.youtubeSummary.controller;

import com.example.youtubeSummary.model.VideoSummary;
import com.example.youtubeSummary.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SummaryController {

    @Autowired
    private YouTubeService youTubeService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("videoSummary", new VideoSummary());
        return "index";
    }

    @PostMapping("/summarize")
    public String summarize(@RequestParam("url") String url, Model model) {
        System.out.println("Received URL: " + url); // 로그 추가
        VideoSummary videoSummary = youTubeService.getVideoSummary(url);
        System.out.println("Video Summary: " + videoSummary.getSummary()); // 로그 추가
        model.addAttribute("videoSummary", videoSummary);
        return "index";
    }

}
