package com.example.pvfrota.graalJS.service;

import com.example.pvfrota.graalJS.model.Logic;
import com.example.pvfrota.graalJS.repository.LogicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 26/08/2024
 */
@Service
public class LogicService {

    private final LogicRepository repository;

    public LogicService(LogicRepository repository) {
        this.repository = repository;
    }

    public List<Logic> findAll() {
        return repository.findAll();
    }

    public Optional<Logic> findById(String name) {
        return repository.findById(name);
    }

    public Logic save(Logic logic) {
        return repository.save(logic);
    }

    public Logic update(Logic logic) {
        if (repository.existsById(logic.getName())) return repository.save(logic);

        throw new RuntimeException("Logic not found");
    }

    public void deleteById(String name) {
        repository.deleteById(name);
    }
}
