package com.core.halpme;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 서버 작동 확인용 컨트롤러 추후 삭제 요망
@RestController
public class TestController {
    @GetMapping("/")
    public String test() {
        return "Halpme Backend is running";
    }
}
