package com.kaziamyr.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(nullable = false)
    private LocalDateTime orderDate;
    @Column(nullable = false)
    private String shippingAddress;
    @OneToMany
    @JoinTable(
            name = "orders_order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_item_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderItem> orderItems;
    @Column(nullable = false)
    private boolean isDeleted;

    public enum Status {

    }
}
