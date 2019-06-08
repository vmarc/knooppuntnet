package com.knooppuntnet;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.knooppuntnet.controller.CyclingController;
import com.knooppuntnet.controller.GpxController;
import com.knooppuntnet.controller.HikingController;
import com.knooppuntnet.controller.PdfController;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=file:/kpn/conf/planner-prototype.properties")
public class SmokeTest {

	@Autowired
	private PdfController pdfController;

	@Autowired
	private GpxController gpxController;

	@Autowired
	private CyclingController cyclingController;

	@Autowired
	private HikingController hikingController;

	@Test
	public void contextLoads() {
		assertThat(pdfController).isNotNull();
		assertThat(gpxController).isNotNull();
		assertThat(cyclingController).isNotNull();
		assertThat(hikingController).isNotNull();
	}
}
