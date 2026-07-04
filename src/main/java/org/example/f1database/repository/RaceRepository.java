package org.example.f1database.repository;

import org.example.f1database.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceRepository extends JpaRepository<Race, Long> {

    List<Race> findByYear(int year);
}