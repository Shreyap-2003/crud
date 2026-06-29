package com.example.demo.repository;

import com.example.demo.domain.Dispatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispatchRepository
        extends JpaRepository<Dispatch, Long> {

    List<Dispatch> findByJobId(Long jobId);
}