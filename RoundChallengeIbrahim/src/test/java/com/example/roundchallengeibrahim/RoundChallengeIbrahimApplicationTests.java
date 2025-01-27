package com.example.roundchallengeibrahim;

import com.example.roundchallengeibrahim.controller.RoundUpController;
import com.example.roundchallengeibrahim.service.RoundUpService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RoundChallengeIbrahimApplicationTests {

    @Test
    void contextLoads() {
    }
}

// --- MockMvc Approach: Testing HTTP Endpoint ---
@WebMvcTest(RoundUpController.class)
class RoundUpControllerMockMvcTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RoundUpService roundUpService;
//
//    @Test
//    void testGetRoundUp() throws Exception {
//        // Mock service behavior
//        Mockito.when(roundUpService.fetchWeekTransactions())
//               .thenReturn(Collections.emptyList());
//        Mockito.when(roundUpService.calculateRoundUp(anyList()))
//               .thenReturn(new BigDecimal("5.65"));
//
//        // Test endpoint using MockMvc
//        mockMvc.perform(get("/round-up"))
//               .andExpect(status().isOk())
//               .andExpect(content().string("5.65"));
//
//        // Verify service calls
//        Mockito.verify(roundUpService, Mockito.times(1)).fetchWeekTransactions();
//        Mockito.verify(roundUpService, Mockito.times(1)).calculateRoundUp(anyList());
//    }
}

// --- Direct Controller Approach ---
@SpringBootTest
class RoundUpControllerDirectTest {

//    @Autowired
//    private RoundUpController roundUpController;
//
//    @MockBean
//    private RoundUpService roundUpService;
//
//    @Test
//    public void testGetRoundUpAmount() {
//        // Mock service behavior
//        Mockito.when(roundUpService.fetchWeekTransactions())
//               .thenReturn(Collections.emptyList());
//        Mockito.when(roundUpService.calculateRoundUp(anyList()))
//               .thenReturn(new BigDecimal("5.65"));
//
//        // Call the controller method directly
//        ResponseEntity<BigDecimal> response = roundUpController.getRoundUpAmount();
//        BigDecimal result = response.getBody(); // Extract BigDecimal
//
//        assert result != null; // Ensure result is not null
//        assertEquals(new BigDecimal("5.65"), result, "RoundUp amount should be 5.65");
//    }
}
