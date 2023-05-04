package kpn.e2e

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import kpn.e2e.pages.Application
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import java.util.regex.Pattern

class ApplicationTest extends AnyFunSuite with BeforeAndAfterEach {

  private val applicationUrl = "http://localhost:4000"
  private var playwright: Playwright = _
  private var browser: Browser = _
  private var context: BrowserContext = _

  override def beforeEach(): Unit = {
    playwright = Playwright.create
    val launchOptions = new BrowserType.LaunchOptions().setHeadless(false) // .setSlowMo(1000)
    browser = playwright.firefox.launch(launchOptions)
    context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1600, 900))
  }

  override def afterEach(): Unit = {
    playwright.close()
  }

  test("home page") {
    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)
    assertThat(page).hasTitle("knooppuntnet")

    app.home.analysisLink.click()
    assertThat(page).hasURL(Pattern.compile(".*analysis$"))
    assertThat(page).hasTitle("Analysis | knooppuntnet")
    assertThat(app.title).hasText("Analysis")

    app.analysis.overviewLink.click()
    assertThat(page).hasTitle("Overview | knooppuntnet")
    assertThat(app.title).hasText("Overview")
    page.goBack()

    app.analysis.cyclingLink.click()
    assertThat(page).hasTitle("Cycling | knooppuntnet")
    assertThat(app.title).hasText("Cycling")
    page.goBack()

    app.analysis.hikingLink.click()
    assertThat(page).hasTitle("Hiking | knooppuntnet")
    assertThat(app.title).hasText("Hiking")
    page.goBack()

    app.analysis.horseRidingLink.click()
    assertThat(page).hasTitle("Horse riding | knooppuntnet")
    assertThat(app.title).hasText("Horse riding")
    page.goBack()

    app.analysis.motorboatLink.click()
    assertThat(page).hasTitle("Motorboat | knooppuntnet")
    assertThat(app.title).hasText("Motorboat")
    page.goBack()

    app.analysis.canoeLink.click()
    assertThat(page).hasTitle("Canoe | knooppuntnet")
    assertThat(app.title).hasText("Canoe")
    page.goBack()

    app.analysis.inlineSkatingLink.click()
    assertThat(page).hasTitle("Inline skating | knooppuntnet")
    assertThat(app.title).hasText("Inline skating")
    page.goBack()
  }

  test("planner") {
    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    app.home.mapLink.click()

    app.mapMenu.hikingLink.click()

    assertThat(app.planner.undoButton).isDisabled()
    assertThat(app.planner.redoButton).isDisabled()
    assertThat(app.planner.resetButton).isDisabled()
    assertThat(app.planner.reverseButton).isDisabled()
    assertThat(app.planner.outputButton).isDisabled()

    app.planner.searchButton.click()
    app.planner.searchField.fill("essen")
    app.planner.searchField.press("Enter")
    app.planner.selectionEssen.click()

    app.planner.zoomInButton.click()
    app.planner.zoomInButton.click()

    //    PlannerPage(page).zoomOutButton.click()

    app.planner.modeDateLastSurvey.click()
    app.planner.modeQuality.click()
    app.planner.modeSurface.click()

    app.planner.layerButton.click()

    app.planner.layerPois.click()
    app.planner.layerPois.click()
    app.planner.poiLayerTourism.click()
    app.planner.poiLayerLandmarks.click()
    app.planner.poiLayerRestaurants.click()
    app.planner.poiLayerPlacesToStay.click()
  }
}
