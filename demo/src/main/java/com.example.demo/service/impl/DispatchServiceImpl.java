package com.example.demo.service.impl;

import com.example.demo.domain.Dispatch;
import com.example.demo.domain.Order;
import com.example.demo.dto.DispatchDto;
import com.example.demo.enums.DispatchStatus;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.DispatchMapper;
import com.example.demo.repository.DispatchRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.DispatchService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class DispatchServiceImpl implements DispatchService {

    private final DispatchRepository dispatchRepository;
    private final OrderRepository orderRepository;
    private final DispatchMapper dispatchMapper;
    private static final Double RADIUS = 5.0;
    private static final Long WAIT_TIME_IN_MINUTES = 1L;

    @Override
    @Transactional
    public void createDispatch(Long jobId, List<Long> partnerIds) {

        List<Dispatch> dispatches = partnerIds.stream()
            .map(partnerId -> {
                Dispatch dispatch = new Dispatch();
                dispatch.setJobId(jobId);
                dispatch.setPartnerId(partnerId);
                dispatch.setDispatchStatus(DispatchStatus.OPEN);
                dispatch.setCreatedTime(LocalDateTime.now());
                return dispatch;
            })
            .toList();
        dispatchRepository.saveAll(dispatches);
        // RUN IN BACKGROUND
        CompletableFuture.runAsync(() -> {
        try {

            Thread.sleep(WAIT_TIME_IN_MINUTES * 60 * 1000);

            List<Dispatch> updatedDispatches = dispatchRepository.findByJobId(jobId);

            boolean accepted = updatedDispatches.stream()
                    .anyMatch(dispatch ->
                            dispatch.getDispatchStatus() == DispatchStatus.IN_PROGRESS);

            if (!accepted) {
                log.error("No partner accepted dispatch for jobId: {} within 1 minute",jobId);
            }
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            log.error("Dispatch wait interrupted for jobId: {}", jobId);
        }
        });
    }

    @Override
    @Transactional
    public List<DispatchDto> acceptDispatch( Long jobId, Long partnerId ) {

        List<Dispatch> dispatches = dispatchRepository.findByJobId(jobId);

        boolean alreadyAccepted = dispatches.stream()
                .anyMatch(dispatch ->
                        dispatch.getDispatchStatus() == DispatchStatus.IN_PROGRESS);

        if (alreadyAccepted) {
            throw new RuntimeException(
                    "Dispatch already accepted by another partner"
            );
        }
        for (Dispatch dispatch : dispatches) {
            if (dispatch.getPartnerId().equals(partnerId)) {
                dispatch.setDispatchStatus( DispatchStatus.IN_PROGRESS );
            } else {
                dispatch.setDispatchStatus( DispatchStatus.CLOSED );
            }
            dispatch.setLastModifiedTime( LocalDateTime.now() );
        }
        List<Dispatch> updatedDispatches = dispatchRepository.saveAll(dispatches);
        // UPDATE ORDER STATUS
        Order order = orderRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + jobId ));

        order.setOrderStatus(OrderStatus.IN_PROGRESS);

        orderRepository.save(order);
        return dispatchMapper.toDtoList( updatedDispatches );
    }
}