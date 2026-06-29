package com.example.demo.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import com.example.demo.domain.Order;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.InvoiceService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final OrderRepository orderRepository;

    @Override
    public byte[] generateInvoice(Long orderId) throws Exception {

        // Fetch order details
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found: " + orderId));


        /*
         * Load JRXML template
         * Location:
         * src/main/resources/invoice.jrxml
         */
        InputStream reportStream =
                getClass().getResourceAsStream("/invoice.jrxml");

        if (reportStream == null) {
            throw new RuntimeException(
                    "invoice.jrxml NOT FOUND in src/main/resources");
        }

        JasperReport jasperReport;

        try {
            // Compile JRXML into JasperReport
            jasperReport =
                    JasperCompileManager.compileReport(reportStream);
            System.out.println("invoice.jrxml COMPILED SUCCESSFULLY");

        } catch (JRException e) {

            System.out.println("FAILED TO COMPILE invoice.jrxml");
            e.printStackTrace();
            throw new RuntimeException(e);

        } finally {
            reportStream.close();
        }

        // Parameters sent to Jasper
        Map<String, Object> params = new HashMap<>();

        params.put("orderId", order.getId());

        params.put("customerName", order.getCustomer().getFirstName()
                        + " "
                        + order.getCustomer().getLastName());

        params.put("customerEmail",
                order.getCustomer().getEmail() != null
                        && !order.getCustomer().getEmail().isEmpty()
                        ?
                        order.getCustomer().getEmail()
                        : "No Email ID");

        params.put("partnerName",
                order.getPartner() != null
                        ?
                        order.getPartner().getFirstName()
                        + " "
                        + order.getPartner().getLastName()
                        :
                        "Not Assigned");

        params.put("itemName", order.getItem().getName());


        params.put("itemPrice", order.getItem()
                        .getPrice()
                        .doubleValue());


        params.put("orderStatus", order.getOrderStatus().name());

        params.put("createdTime",
                order.getCreatedTime() != null
                        ?
                        order.getCreatedTime().toString()
                        :
                        "N/A");

        params.put("completedTime",
                order.getCompletedTime() != null
                        ?
                        order.getCompletedTime().toString()
                        :
                        "N/A");

        /*
         * Fill report
         * No database connection required
         */
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport, params, new JREmptyDataSource());


        // Export PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        exporter.exportReport();

        return outputStream.toByteArray();
    }
}