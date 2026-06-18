package com.example.demo.service;

public interface InvoiceService {
    byte[] generateInvoice(Long orderId) throws Exception;
}
