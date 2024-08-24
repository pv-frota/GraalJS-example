package com.example.pvfrota.graalJS.repository;

import com.example.pvfrota.graalJS.model.Logic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 24/08/2024
 */
@Repository
public interface LogicRepository extends JpaRepository<Logic, String> {
}
