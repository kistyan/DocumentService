package org.example.documentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "documents")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Document {
  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "template_name", referencedColumnName = "name", nullable = false)
  private Template template;

  @Column(nullable = false)
  private String path;

  @Column(nullable = false)
  private LocalDate creationDate;
}
