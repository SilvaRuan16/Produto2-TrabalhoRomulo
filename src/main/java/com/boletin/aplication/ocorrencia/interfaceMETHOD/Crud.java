package com.boletin.aplication.ocorrencia.interfaceMETHOD;

import java.util.List;

public interface Crud<T> {
   public void Inserir(T entity);
   public void Update(Long id, T entity);
   public void Deletar(Long id);
   List<T> Read();
}