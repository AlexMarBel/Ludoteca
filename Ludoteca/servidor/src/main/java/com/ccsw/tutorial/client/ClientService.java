package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

import java.util.List;

public interface ClientService {

    /**
     * Método para recuperar todos los clientes
     * @return{@link List} de {@link Client}
     */
    List<Client> findAll();

    /**
     * Método para actualizar o crear una categoria
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, ClientDto dto) throws Exception;

    /**
     * Método para borrar un cliente
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    Client get(Long id);

}
