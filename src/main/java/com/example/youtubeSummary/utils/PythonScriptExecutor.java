package com.example.youtubeSummary.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class PythonScriptExecutor {
    private static final Logger logger = Logger.getLogger(PythonScriptExecutor.class.getName());

    public PythonScriptExecutor() {
    }

    public String executePythonScript(String url) {
        try {
            // URL 인코딩
            String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());

            // Python 스크립트 경로
            String scriptPath = "src/main/resources/scripts/youtube_summary.py";

            // ProcessBuilder를 사용하여 Python 스크립트를 실행합니다.
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, encodedUrl);
            processBuilder.redirectErrorStream(true); // 표준 오류와 표준 출력을 통합합니다.
            Process process = processBuilder.start();

            // 프로세스의 출력을 읽어들이기 위한 BufferedReader 설정
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.severe("Python Script Error: Exit code " + exitCode);
                return "Error occurred while executing the script: Exit code " + exitCode;
            } else {
                // 요약된 문장만 반환
                String result = output.toString().trim();
                return result.startsWith("Error:") ? result : result;
            }
        } catch (Exception e) {
            logger.severe("Error occurred while executing the script: " + e.getMessage());
            e.printStackTrace();
            return "Error occurred while executing the script: " + e.getMessage();
        }
    }
}

