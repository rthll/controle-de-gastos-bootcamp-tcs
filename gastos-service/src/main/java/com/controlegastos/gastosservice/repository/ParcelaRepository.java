package com.controlegastos.gastosservice.repository;

import com.controlegastos.gastosservice.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParcelaRepository extends JpaRepository<Parcela, UUID> {
}
