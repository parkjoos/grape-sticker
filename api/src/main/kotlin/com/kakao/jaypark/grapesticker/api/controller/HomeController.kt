package com.kakao.jaypark.grapesticker.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/alive")
    fun foo() = "OK"
}