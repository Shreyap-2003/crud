package com.example.demo.service;

import com.example.demo.dto.DispatchDto;

import java.util.List;

public interface DispatchService {

    void createDispatch( Long jobId, List<Long> partnerIds );

    List<DispatchDto> acceptDispatch(Long jobId, Long partnerId );
}