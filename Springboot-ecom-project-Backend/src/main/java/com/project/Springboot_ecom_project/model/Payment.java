package com.project.Springboot_ecom_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String paymentMethod;

    private String pgName;

    private String pgPaymentId;

    private String pgStatus;

    private String pgResponseMessage;

    @OneToOne(mappedBy = "payment", cascade = {CascadeType.ALL})
    private Order order;

    public Payment(String paymentMethod, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId,payment.paymentId );
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);  // 仅基于 orderId 来计算 hashCode
    }
}
