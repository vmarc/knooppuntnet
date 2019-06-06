package com.knooppuntnet.controller;

import com.knooppuntnet.service.PoiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PoiControllerTest {

	@Mock
	private PoiService poiService;

	@InjectMocks
	private PoiController poiController;

	private MockMvc mockMvc;

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(poiController)
				.build();
	}

	@Test
	public void testGetPoiInformation() throws Exception {
		this.mockMvc.perform(get("/poi/way/48306139"))
				.andDo(print())
				.andExpect(status().isOk());
	}
}