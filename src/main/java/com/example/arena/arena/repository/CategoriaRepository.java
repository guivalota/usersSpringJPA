package com.example.arena.arena.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.arena.arena.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria,Long>{
    
}
