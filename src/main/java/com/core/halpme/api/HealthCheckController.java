package com.core.halpme.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "AWS 헬스체크", description = "AWS EC2 헬스체크용")
@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @Operation(
            summary = "AWS EC2 헬스체크용",
            description = "AWS EC2 헬스체크용 엔드포인트 입니다."
    )
    @GetMapping("health")
    public String healthCheck() {
        return "OK";
    }
}
