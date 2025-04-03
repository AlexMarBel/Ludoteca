package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    GameService gameService;

    @Override
    public List<Loan> findAll() {
        return (List<Loan>) this.loanRepository.findAll();
    }

    @Override
    public void save(Long id, LoanDto dto) {
        Loan loan;

        loan = new Loan();
        BeanUtils.copyProperties(dto, loan, "id", "game", "client");

        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientService.get(dto.getClient().getId()));

        if (isGameOverlapping(dto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game is already loaned in these dates");
        } else if (isClientOverlapping(dto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client already has 2 loans in these dates");
        } else {
            this.loanRepository.save(loan);
        }
    }

    @Override
    public Page<Loan> findPage(Long gameId, Long clientId, LocalDate date, LoanSearchDto dto) {
        LoanSpecification idGameSpec = new LoanSpecification(new SearchCriteria("game.id", ":", gameId));
        LoanSpecification idClientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", clientId));
        LoanSpecification dateEndSpec = new LoanSpecification(new SearchCriteria("endDate", "lessThanOrEqualTo", date));
        LoanSpecification dateStartSpec = new LoanSpecification(new SearchCriteria("startDate", "greaterThanOrEqualTo", date));

        Specification<Loan> spec = Specification.where(idGameSpec).and(idClientSpec).and(dateEndSpec).and(dateStartSpec);

        return this.loanRepository.findAll(spec, dto.getPageable().getPageable());
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.loanRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        }

        this.loanRepository.deleteById(id);
    }

    @Override
    public boolean isGameOverlapping(LoanDto dto) {
        Long gameId = dto.getGame().getId();
        Long clientId = dto.getClient().getId();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        List<Loan> loans = this.findAll();

        for (Loan loan : loans) {
            if (loan.getGame().getId() == gameId) {
                LocalDate endDateLoan = loan.getEndDate();
                LocalDate startDateLoan = loan.getStartDate();

                boolean isOverLapping = (startDate.isBefore(endDateLoan) && endDate.isAfter(startDateLoan)) || (startDateLoan.isBefore(endDate) && endDateLoan.isAfter(startDate));

                if (isOverLapping) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isClientOverlapping(LoanDto dto) {
        Long clientId = dto.getClient().getId();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        List<Loan> loans = this.findAll();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int loanCount = 0;
            for (Loan loan : loans) {
                if (loan.getClient().getId().equals(clientId) && !loan.getEndDate().isBefore(date) && !loan.getStartDate().isAfter(date)) {
                    loanCount++;
                }
            }
            if (loanCount >= 2) {
                return true;
            }
        }
        return false;
    }
}
