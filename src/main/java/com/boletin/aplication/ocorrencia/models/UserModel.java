package com.boletin.aplication.ocorrencia.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserModel {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "username", nullable = false, length = 50, unique = true)
   private String userName;

   @Column(name = "password", nullable = false, length = 100)
   private String password;

   @OneToMany(mappedBy = "usuarioModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   private List<BoletinModel> boletins = new ArrayList<>();

   public UserModel() {}

   public UserModel(String userName, String password) {
      this.userName = userName;
      this.password = password;
   }
}
