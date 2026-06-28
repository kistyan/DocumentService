package org.example.documentservice.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "work_acceptance_acts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class WorkAcceptanceAct {
  @Id
  private UUID id;

  @OneToOne
  @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false)
  private Document document;

  @Column(nullable = false)
  private String number;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private String contractNumber;

  @Column(nullable = false)
  private String requestNumber;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "name", column = @Column(name = "customer_name")),
      @AttributeOverride(name = "tin", column = @Column(name = "customer_tin"))
  })
  private Organization customer;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "name", column = @Column(name = "contractor_name")),
      @AttributeOverride(name = "tin", column = @Column(name = "contractor_tin"))
  })
  private Organization contractor;

  @Column(nullable = false)
  private String product;

  @Column(nullable = false)
  private BigDecimal totalAmount;
}
