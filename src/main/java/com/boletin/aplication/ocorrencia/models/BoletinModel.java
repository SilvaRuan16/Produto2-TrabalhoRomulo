package com.boletin.aplication.ocorrencia.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "boletin")
@Getter
@Setter
public class BoletinModel {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "title", nullable = false, length = 100)
   private String title;

   @Column(name = "description", nullable = false, length = 1000)
   private String description;

   @Column(name = "dateRegistry", nullable = false)
   private LocalDateTime dateRegistry;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "usuario_id", nullable = false)
   private UserModel usuarioModel;

   public BoletinModel () {
      dateRegistry = LocalDateTime.now();
   }

   public BoletinModel (String title, String description, UserModel usuarioModel) {
      this.title = title;
      this.description = description;
      this.usuarioModel = usuarioModel;
      this.dateRegistry = LocalDateTime.now();
   }
}
