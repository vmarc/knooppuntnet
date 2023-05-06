package kpn.e2e

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import kpn.api.common.common.User
import kpn.database.base.Database
import kpn.database.base.DatabaseImpl
import kpn.database.util.Mongo
import kpn.e2e.pages.Application
import org.mongodb.scala.MongoClient
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers

import java.util.regex.Pattern

class ApplicationTest extends AnyFunSuite with BeforeAndAfterEach with Matchers {

  private val applicationUrl = "http://localhost:4000"
  private var playwright: Playwright = _
  private var browser: Browser = _
  private var context: BrowserContext = _
  private var mongoClient: MongoClient = _
  private var database: Database = _

  override def beforeEach(): Unit = {
    playwright = Playwright.create
    val launchOptions = new BrowserType.LaunchOptions().setHeadless(false) // .setSlowMo(1000)
    browser = playwright.firefox.launch(launchOptions)
    context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1600, 900))
    mongoClient = Mongo.client
    database = new DatabaseImpl(mongoClient.getDatabase("kpn-test").withCodecRegistry(Mongo.codecRegistry))
  }

  override def afterEach(): Unit = {
    mongoClient.close()
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

    // http://localhost:4000/map/hiking?mode=surface&position=51.51541504,4.56389044,13&result=compact&layers=background,hiking,pois&poi-layers=hiking-biking,landmarks,restaurants,places-to-stay,tourism

    page.url() should include("?mode=surface")
    page.url() should include("&position=")
    page.url() should include("&result=compact")
    page.url() should include("&layers=background,hiking,pois")
    page.url() should include("&poi-layers=hiking-biking,landmarks,restaurants,places-to-stay,tourism")

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
    page.url() should include("&layers=background,hiking,pois")

    app.planner.layerPois.click()
    page.url() should include("&layers=background,hiking")

    app.planner.layerPois.click()
    page.url() should include("&layers=background,hiking,pois")

    app.planner.poiLayerTourism.click()
    page.url() should include("&poi-layers=hiking-biking,landmarks,restaurants,places-to-stay")

    app.planner.poiLayerLandmarks.click()
    page.url() should include("&poi-layers=hiking-biking,restaurants,places-to-stay")

    app.planner.poiLayerRestaurants.click()
    page.url() should include("&poi-layers=hiking-biking,places-to-stay")

    app.planner.poiLayerPlacesToStay.click()
    page.url() should include("&poi-layers=hiking-biking")
  }

  test("monitor") {

    resetDatabase()

    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    app.home.monitorLink.click()

    assertThat(app.monitor.routesInGroups).hasText("0 routes in 0 groups")
    assertThat(app.monitor.noGroups).hasText("No route groups")

    assertThat(app.monitor.adminToggle).isDisabled()
    assertThat(app.monitor.groupAddButton).isHidden()

    page.goBack()

    database.users.save(User("test-user"))

    app.home.monitorLink.click()

    assertThat(app.monitor.adminToggle).isEnabled()
    assertThat(app.monitor.groupAddButton).isHidden()

    app.monitor.adminToggle.click()
    assertThat(app.monitor.groupAddButton).isVisible()

    app.monitor.groupAddButton.click()

    assertThat(app.title).hasText("Monitor - add group")

    app.monitorAddGroup.groupAddButton.click()

    assertThat(app.monitorAddGroup.nameFieldError).hasText("Name is required.")
    assertThat(app.monitorAddGroup.descriptionFieldError).hasText("Description is required.")

    app.monitorAddGroup.nameField.fill("group1")
    app.monitorAddGroup.nameField.press("Enter")

    app.monitorAddGroup.descriptionField.fill("group one")
    app.monitorAddGroup.descriptionField.press("Enter")

    app.waitForFormValid()

    app.monitorAddGroup.groupAddButton.click()
    assertThat(app.monitor.routesInGroups).hasText("0 routes in 1 groups")

    app.monitor.link("group1").click()
  }

  private def resetDatabase(): Unit = {
    database.users.drop()
    database.monitorGroups.drop()
    database.monitorRoutes.drop()
    database.monitorRouteStates.drop()
    database.monitorRouteReferences.drop()
    database.monitorRouteChanges.drop()
    database.monitorRouteChangeGeometries.drop()
  }
}
