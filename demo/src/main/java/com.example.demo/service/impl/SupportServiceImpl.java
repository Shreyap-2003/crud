package com.example.demo.service.impl;

import com.example.demo.domain.Dispatch;
import com.example.demo.domain.Order;
import com.example.demo.domain.User;
import com.example.demo.dto.DispatchDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.DispatchMapper;
import com.example.demo.repository.DispatchRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SupportService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final DispatchRepository dispatchRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DispatchMapper dispatchMapper;

    @Override
    public List<DispatchDto> getDispatches(Long jobId,Double distance) {

        Order order = orderRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + jobId));

        User customer = order.getCustomer();

        List<Object[]> nearbyPartners =
                userRepository.findNearbyPartners(customer.getLatitude(),customer.getLongitude(),distance);

        List<Dispatch> dispatches = dispatchRepository.findByJobId(jobId);

        List<DispatchDto> result = new ArrayList<>();

        for (Object[] partner : nearbyPartners) {
            Long partnerId = ((Number) partner[0]).longValue();
            Double distanceInKms = ((Number) partner[3]).doubleValue();
            for (Dispatch dispatch : dispatches) {
                if (dispatch.getPartnerId().equals(partnerId)) {
                    DispatchDto dto = dispatchMapper.toDto(dispatch);
                    dto.setDistanceInKms(distanceInKms);
                    result.add(dto);
                }
            }
        }
        return result;
    }
//        List<Long> partnerIds = nearbyPartners.stream()
//                .map(partner ->
//                        ((Number) partner[0]).longValue())
//                .toList();
//
//
//
//        return (dispatches.stream()
//                .filter(dispatch ->
//                        partnerIds.contains(dispatch.getPartnerId()))
//                .map(dispatchMapper::toDto)
//                .toList());
        // above simplified
        //        List<DispatchDto> result = dispatches.stream()
        //                .filter(dispatch ->
        //                        partnerIds.contains(dispatch.getPartnerId()))
        //                .map(dispatchMapper::toDto)
        //                .toList();
        //
        //        return (result);

    @Override
    public Page<DispatchDto> getAllDispatches(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<DispatchDto> result = new ArrayList<>();

        List<Dispatch> allDispatches = dispatchRepository.findAll();

        for (Dispatch dispatch : allDispatches) {

            Order order = orderRepository.findById(dispatch.getJobId())
                    .orElse(null);

            if (order == null) {
                continue;
            }

            User customer = order.getCustomer();

            List<Object[]> nearbyPartners =
                    userRepository.findNearbyPartners(customer.getLatitude(),customer.getLongitude(),1000.0);

            Double distanceInKms = null;

            for (Object[] partner : nearbyPartners) {

                Long partnerId = ((Number) partner[0]).longValue();

                if (partnerId.equals(dispatch.getPartnerId())) {
                    distanceInKms = ((Number) partner[3]).doubleValue();
                    break;
                }
            }
            DispatchDto dto = dispatchMapper.toDto(dispatch);
            dto.setDistanceInKms(distanceInKms);

            result.add(dto);
        }

        int start = page * size;
        int end = Math.min(start + size, result.size());

        List<DispatchDto> paginatedList = result.subList(start, end);

        return new PageImpl<>(paginatedList,pageable,result.size());
    }
}
