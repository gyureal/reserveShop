package com.example.reserveshop.store.domain.repository;

import com.example.reserveshop.store.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByNameStartsWithOrderByName(String name);
}
