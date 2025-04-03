package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";
    private static final int PAGE_SIZE = 5;
    private static final long TOTAL_LOANS = 7L;
    private static final long EXISTS_GAME_ID = 1L;
    private static final long NOT_EXISTS_GAME_ID = 0L;
    private static final long EXISTS_CLIENT_ID = 2L;
    private static final long NOT_EXISTS_CLIENT_ID = 0L;
    private static final LocalDate EXISTS_DATE = LocalDate.of(2025, 4, 15);
    private static final LocalDate EXISTS_DATE_2 = LocalDate.of(2025, 4, 7);
    private static final LocalDate NOT_EXISTS_DATE = LocalDate.of(2025, 5, 15);
    private static final long DELETE_LOAN_ID = 1L;

    private static final String GAME_ID_PARAM = "gameId";
    private static final String CLIENT_ID_PARAM = "clientId";
    private static final String DATE_PARAM = "date";

    @LocalServerPort

    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_ID_PARAM, "{" + GAME_ID_PARAM + "}").queryParam(CLIENT_ID_PARAM, "{" + CLIENT_ID_PARAM + "}").queryParam(DATE_PARAM, "{" + DATE_PARAM + "}")
                .encode().toUriString();
    }

    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };
    ParameterizedTypeReference<List<LoanDto>> responseType = new ParameterizedTypeReference<List<LoanDto>>() {
    };

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {
        long elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void findWithoutFiltersShouldReturnAllLoansInDB() {
        int LOANS_WITH_FILTER = 7;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameShouldReturnLoans() {
        int LOANS_WITH_FILTER = 2;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    public void findNotExistsGameShouldReturnEmpty() {
        int LOANS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    public void findExistsClientShouldReturnLoans() {
        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), null);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsClientShouldReturnEmpty() {
        int LOANS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), null);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsDateShouldReturnLoans() {

        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), EXISTS_DATE);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsDateShouldReturnEmpty() {

        int LOANS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndClientShouldReturnLoans() {

        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), null);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    public void findExistsGameAndDateShouldReturnLoans() {
        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), EXISTS_DATE_2);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsClientAndDateShouldReturnLoans() {
        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE_2);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndClientAndDateShouldReturnLoans() {
        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE_2);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsGameOrClientOrDateShouldReturnEmpty() {

        int LOANS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    void saveWithoutIdShouldCreateNewLoan() {
        long NEW_LOAN_SIZE = TOTAL_LOANS + 1;
        long NEW_LOAN_ID = TOTAL_LOANS + 1;
        long GAME_ID_INSERTED = 1L;

        LoanDto dto = new LoanDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(GAME_ID_INSERTED);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(LocalDate.of(2025, 4, 20));
        dto.setEndDate(LocalDate.of(2025, 4, 30));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) NEW_LOAN_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(NEW_LOAN_SIZE, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(NEW_LOAN_ID)).findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(GAME_ID_INSERTED, loan.getGame().getId());

    }

    @Test
    void deleteWithExistsIdShouldDeleteLoan() {
        long NEW_LOAN_SIZE = TOTAL_LOANS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(NEW_LOAN_SIZE, response.getBody().size());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteLoanId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteLoanId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void findAllShouldReturnAllLoan() {

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().size());
    }
}
