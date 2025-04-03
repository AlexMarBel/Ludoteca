package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    /**
     * Recupera la lista de prestamos
     * @return {@link List} de {@link Loan}
     */
    List<Loan> findAll();

    /**
     * Guarda un prestamo
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, LoanDto dto);

    /**
     * Método para recuperar un listado paginado de {@link Loan}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link Loan}
     */
    Page<Loan> findPage(Long gameId, Long clientId, LocalDate date, LoanSearchDto dto);

    /**
     *
     * @param id
     * @throws Exception
     */
    void delete(Long id) throws Exception;

    boolean isGameOverlapping(LoanDto dto);

    boolean isClientOverlapping(LoanDto dto);
}
