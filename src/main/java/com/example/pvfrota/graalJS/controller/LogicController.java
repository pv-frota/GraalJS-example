package com.example.pvfrota.graalJS.controller;

import com.example.pvfrota.graalJS.model.Logic;
import com.example.pvfrota.graalJS.service.LogicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 26/08/2024
 */
@RestController
@RequestMapping("/api/logic")
public class LogicController {

    private final LogicService service;

    public LogicController(LogicService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Logic>> getAllLogic() {
        List<Logic> logicList = service.findAll();
        return ResponseEntity.ok(logicList);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Logic> getLogicById(@PathVariable String name) {
        Optional<Logic> logic = service.findById(name);
        return logic.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Logic> createLogic(@RequestBody Logic logic) {
        Logic savedLogic = service.save(logic);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLogic);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Logic> updateLogic(@PathVariable String name, @RequestBody Logic logic) {
        if (!logic.getName().equals(name)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Logic updatedLogic = service.update(logic);
            return ResponseEntity.ok(updatedLogic);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteLogic(@PathVariable String name) {
        service.deleteById(name);
        return ResponseEntity.noContent().build();
    }
}
