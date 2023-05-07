package kpn.e2e

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.assertions.LocatorAssertions
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole
import com.microsoft.playwright.Locator
import com.microsoft.playwright.options.WaitForSelectorState
import kpn.api.common.common.User
import kpn.database.base.Database
import kpn.database.base.DatabaseImpl
import kpn.database.util.Mongo
import kpn.e2e.pages.Application
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import org.mongodb.scala.MongoClient
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers

import java.nio.file.Paths
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
    assertThat(app.h1).hasText("Analysis")

    app.analysis.overviewLink.click()
    assertThat(page).hasTitle("Overview | knooppuntnet")
    assertThat(app.h1).hasText("Overview")
    page.goBack()

    app.analysis.cyclingLink.click()
    assertThat(page).hasTitle("Cycling | knooppuntnet")
    assertThat(app.h1).hasText("Cycling")
    page.goBack()

    app.analysis.hikingLink.click()
    assertThat(page).hasTitle("Hiking | knooppuntnet")
    assertThat(app.h1).hasText("Hiking")
    page.goBack()

    app.analysis.horseRidingLink.click()
    assertThat(page).hasTitle("Horse riding | knooppuntnet")
    assertThat(app.h1).hasText("Horse riding")
    page.goBack()

    app.analysis.motorboatLink.click()
    assertThat(page).hasTitle("Motorboat | knooppuntnet")
    assertThat(app.h1).hasText("Motorboat")
    page.goBack()

    app.analysis.canoeLink.click()
    assertThat(page).hasTitle("Canoe | knooppuntnet")
    assertThat(app.h1).hasText("Canoe")
    page.goBack()

    app.analysis.inlineSkatingLink.click()
    assertThat(page).hasTitle("Inline skating | knooppuntnet")
    assertThat(app.h1).hasText("Inline skating")
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

  test("monitor add group") {

    resetDatabase()

    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    app.home.monitorLink.click()

    assertThat(app.monitor.routesInGroups).hasText("0 routes in 0 groups")
    assertThat(app.monitor.noGroups).hasText("No route groups")

    assertThat(app.monitor.adminToggle).isDisabled() // not logged in
    assertThat(app.monitor.groupAddButton).isHidden() // admin not activated yet

    page.goBack()

    database.users.save(User("test-user")) // login

    app.home.monitorLink.click()

    assertThat(app.monitor.adminToggle).isEnabled() // logged in
    assertThat(app.monitor.groupAddButton).isHidden() // admin not activated yet

    app.monitor.adminToggle.click()
    assertThat(app.monitor.groupAddButton).isVisible()

    app.monitor.groupAddButton.click()

    assertThat(app.h1).hasText("Monitor - add group")

    app.monitorAddGroup.groupAddButton.click()

    assertThat(app.monitorAddGroup.nameFieldError).hasText("Name is required.")
    assertThat(app.monitorAddGroup.descriptionFieldError).hasText("Description is required.")

    app.monitorAddGroup.nameField.fill("group1")
    app.monitorAddGroup.descriptionField.fill("group one")

    app.waitForFormValid("group")

    app.monitorAddGroup.groupAddButton.click()
    assertThat(app.monitor.routesInGroups).hasText("0 routes in 1 groups")

    app.monitor.link("group1").click()

    assertThat(app.h1).hasText("group1group one")

    assertThat(app.group.adminToggle).isEnabled()
    assertThat(app.group.adminToggle).isChecked()

    assertThat(app.group.noRoutesInGroup).isVisible()
    assertThat(app.group.routeAddButton).isEnabled()

    app.group.adminToggle.click()

    assertThat(app.group.adminToggle).isChecked(new LocatorAssertions.IsCheckedOptions().setChecked(false))
    assertThat(app.group.routeAddButton).isHidden()
  }

  test("monitor add route") {

    resetDatabase()

    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    database.users.save(User("test-user")) // login

    app.home.monitorLink.click()
    app.monitor.adminToggle.click()
    app.monitor.groupAddButton.click()
    app.monitorAddGroup.nameField.fill("group1")
    app.monitorAddGroup.descriptionField.fill("group one")
    app.waitForFormValid("group")
    app.monitorAddGroup.groupAddButton.click()
    app.monitor.link("group1").click()
    assertThat(app.h1).hasText("group1group one")

    app.group.routeAddButton.click()
    assertThat(app.h1).hasText("group one")
    assertThat(app.h2).hasText("Add route")

    app.routeAdd.cancelButton.click()
    assertThat(app.h1).hasText("group1group one")

    app.group.routeAddButton.click()
    assertThat(app.h1).hasText("group one")
    assertThat(app.h2).hasText("Add route")

    // step 2 (group step 1 is not visible in route mode 'add'):
    app.routeAdd.nameField.fill("route1")
    app.routeAdd.descriptionField.fill("route one")
    app.waitForFormValid("step2")
    app.routeAdd.step2NextButton.click()

    // step 3:
    assertThat(page.getByText("Do you know the OSM relation id for this route?")).isVisible()
    page.getByText("No", new Page.GetByTextOptions().setExact(true)).click()
    app.routeAdd.step3NextButton.click()

    // step 4:
    assertThat(page.getByText("What do you want to use as reference to compare the OSM relation to?")).isVisible()
    page.getByText("A GPX trace that you will upload now").click()
    app.routeAdd.step4NextButton.click()

    // step 5:
    page.locator(".file-input").setInputFiles(Paths.get("/home/vmarc/wrk/osm/gpx/de-nieuwe-drenk-1.gpx"))
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open calendar")).click()
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("May 1, 2023")).click()
    app.routeAdd.step5NextButton.click()

    // step 6:
    page.getByLabel("Additional information about the route (optional):").fill("dddd")

    app.routeAdd.saveButton.click()


    val visible = new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
    page.locator(s"#route-saved").waitFor(visible)

    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Back to group")).click()

    val groupRepository = new MonitorGroupRepositoryImpl(database)
    val routeRepository = new MonitorRouteRepositoryImpl(database)

    val group = groupRepository.groupByName("group1").get
    val route = routeRepository.routeByName(group._id, "route1")

    println(route)

    println("done")
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
