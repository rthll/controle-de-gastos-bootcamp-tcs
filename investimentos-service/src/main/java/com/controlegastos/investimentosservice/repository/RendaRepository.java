package com.controlegastos.investimentosservice.repository;
import com.controlegastos.investimentosservice.entity.Renda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RendaRepository extends JpaRepository<Renda, UUID> {

}