package com.example.youtubeSummary.service;

import com.example.youtubeSummary.utils.PythonScriptExecutor;
import com.example.youtubeSummary.model.VideoSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YouTubeService {

    @Autowired
    private PythonScriptExecutor pythonScriptExecutor;

    public VideoSummary getVideoSummary(String url) {
        String summary = pythonScriptExecutor.executePythonScript(url);
        VideoSummary videoSummary = new VideoSummary();
        videoSummary.setSummary(summary);
        return videoSummary;
    }
}
