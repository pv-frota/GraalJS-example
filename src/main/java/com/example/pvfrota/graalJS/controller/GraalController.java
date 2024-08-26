package com.example.pvfrota.graalJS.controller;

import com.example.pvfrota.graalJS.record.DynamicParameterValue;
import com.example.pvfrota.graalJS.service.GraalService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 22/08/2024
 */
@RestController
@RequestMapping("/api/graal")
public class GraalController {

    private final GraalService graalService;

    public GraalController(GraalService graalService) {
        this.graalService = graalService;
    }

    @PostMapping("/run-script")
    public Object executeScript(@RequestParam String name, @RequestBody DynamicParameterValue[] parameter) {
        return graalService.runScript(name, parameter);
    }
}
