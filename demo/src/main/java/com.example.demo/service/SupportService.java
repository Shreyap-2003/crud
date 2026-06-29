package com.example.demo.service;

import com.example.demo.dto.DispatchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupportService {

    List<DispatchDto> getDispatches(Long jobId, Double distance);

    Page<DispatchDto> getAllDispatches(int page, int size);
}
