package kpn.planner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import kpn.planner.service.HikingService;

@RunWith(MockitoJUnitRunner.class)
public class HikingControllerTest {

	@Mock
	private HikingService hikingService;

	@InjectMocks
	private HikingController hikingController;

	private MockMvc mockMvc;

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(hikingController).build();
	}

	@Test
	public void testCalculateRouteFromMultiline() throws Exception {
		this.mockMvc.perform(get("/api/hiking/10017/48306139")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testCalculateRouteFromMultilineWithNoNodeId() throws Exception {
		this.mockMvc.perform(get("/api/hiking/10017/-1")).andDo(print()).andExpect(status().isOk());
	}
}
