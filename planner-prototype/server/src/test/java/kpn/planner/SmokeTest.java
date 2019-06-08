package kpn.planner;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kpn.planner.controller.GpxController;
import kpn.planner.controller.PdfController;
import kpn.planner.controller.PlannerController;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=file:/kpn/conf/planner.properties")
public class SmokeTest {

	@Autowired
	private PdfController pdfController;

	@Autowired
	private GpxController gpxController;

	@Autowired
	private PlannerController plannerController;

	@Test
	public void contextLoads() {
		assertNotNull(pdfController);
		assertNotNull(gpxController);
		assertNotNull(plannerController);
	}
}
