package com.example.demo.dto;

import com.example.demo.enums.DispatchStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatchDto {

    private Long id;

    private Long jobId;

    private Long partnerId;

    private DispatchStatus dispatchStatus;

    private LocalDateTime createdTime;

    private LocalDateTime lastModifiedTime;
}