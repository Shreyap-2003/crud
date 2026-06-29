package com.example.demo.rest;

import com.example.demo.dto.DispatchDto;
import com.example.demo.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/application/support")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class SupportResource {

    private final SupportService supportService;

    @GetMapping("/{jobId}")
    public List<DispatchDto> getDispatches(@PathVariable Long jobId, @RequestParam Double distance) {
        return supportService.getDispatches(jobId, distance);
    }

    @GetMapping("/dispatches")
    public ResponseEntity<Page<DispatchDto>> getAllDispatches(
            @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(supportService.getAllDispatches(page, size));
    }
}