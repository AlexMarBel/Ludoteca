package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    Page<Loan> findAll(Specification<Loan> spec, Pageable pageable);

    //List<Loan> findAllNotFiltered();

    /**
     @EntityGraph(attributePaths = { "game", "client" })
     Page<Loan> findAll(Pageable pageable, Specification<Loan> spec);
     */
}
