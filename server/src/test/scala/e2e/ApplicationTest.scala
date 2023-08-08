package e2e

import com.microsoft.playwright.assertions.LocatorAssertions
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole
import e2e.pages.Application
import kpn.api.common.common.User
import kpn.database.base.Database
import kpn.database.base.DatabaseImpl
import kpn.database.util.Mongo
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import org.mongodb.scala.MongoClient

import java.nio.file.Paths
import java.util.regex.Pattern

class ApplicationTest extends EndToEndTest {

  private var mongoClient: MongoClient = _
  private var database: Database = _

  override def beforeEach(): Unit = {
    super.beforeEach();
    mongoClient = Mongo.client
    database = new DatabaseImpl(mongoClient.getDatabase("kpn-test").withCodecRegistry(Mongo.codecRegistry))
  }

  override def afterEach(): Unit = {
    mongoClient.close()
    super.afterEach()
  }

  test("home page") {
    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)
    assertThat(page).hasTitle("knooppuntnet")

    click(app.home.analysisLink)
    assertThat(page).hasURL(Pattern.compile(".*analysis$"))
    assertThat(page).hasTitle("Analysis | knooppuntnet")
    assertThat(app.h1).hasText("Analysis")

    click(app.analysis.overviewLink)
    assertThat(page).hasTitle("Overview | knooppuntnet")
    assertThat(app.h1).hasText("Overview")
    page.goBack()

    click(app.analysis.cyclingLink)
    assertThat(page).hasTitle("Cycling | knooppuntnet")
    assertThat(app.h1).hasText("Cycling")
    page.goBack()

    click(app.analysis.hikingLink)
    assertThat(page).hasTitle("Hiking | knooppuntnet")
    assertThat(app.h1).hasText("Hiking")
    page.goBack()

    click(app.analysis.horseRidingLink)
    assertThat(page).hasTitle("Horse riding | knooppuntnet")
    assertThat(app.h1).hasText("Horse riding")
    page.goBack()

    click(app.analysis.motorboatLink)
    assertThat(page).hasTitle("Motorboat | knooppuntnet")
    assertThat(app.h1).hasText("Motorboat")
    page.goBack()

    click(app.analysis.canoeLink)
    assertThat(page).hasTitle("Canoe | knooppuntnet")
    assertThat(app.h1).hasText("Canoe")
    page.goBack()

    click(app.analysis.inlineSkatingLink)
    assertThat(page).hasTitle("Inline skating | knooppuntnet")
    assertThat(app.h1).hasText("Inline skating")
    page.goBack()
  }

  test("planner") {
    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    click(app.home.mapLink)

    click(app.mapMenu.hikingLink)

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

    click(app.planner.searchButton)
    app.planner.searchField.fill("essen")
    app.planner.searchField.press("Enter")
    click(app.planner.selectionEssen)

    click(app.planner.zoomInButton)
    click(app.planner.zoomInButton)

    //    click(PlannerPage(page).zoomOutButton)

    click(app.planner.modeDateLastSurvey)
    click(app.planner.modeQuality)
    click(app.planner.modeSurface)

    click(app.planner.layerButton)
    page.url() should include("&layers=background,hiking,pois")

    click(app.planner.layerPois)
    page.url() should include("&layers=background,hiking")

    click(app.planner.layerPois)
    page.url() should include("&layers=background,hiking,pois")

    click(app.planner.poiLayerTourism)
    page.url() should include("&poi-layers=hiking-biking,landmarks,restaurants,places-to-stay")

    click(app.planner.poiLayerLandmarks)
    page.url() should include("&poi-layers=hiking-biking,restaurants,places-to-stay")

    click(app.planner.poiLayerRestaurants)
    page.url() should include("&poi-layers=hiking-biking,places-to-stay")

    click(app.planner.poiLayerPlacesToStay)
    page.url() should include("&poi-layers=hiking-biking")
  }

  test("monitor add group") {

    resetDatabase()

    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    click(app.home.monitorLink)

    assertThat(app.monitor.routesInGroups).hasText("0 routes in 0 groups")
    assertThat(app.monitor.noGroups).hasText("No route groups")

    assertThat(app.monitor.adminToggle).isDisabled() // not logged in
    assertThat(app.monitor.groupAddButton).isHidden() // admin not activated yet

    page.goBack()

    database.users.save(User("test-user")) // login

    click(app.home.monitorLink)

    assertThat(app.monitor.adminToggle).isEnabled() // logged in
    assertThat(app.monitor.groupAddButton).isHidden() // admin not activated yet

    click(app.monitor.adminToggle)
    assertThat(app.monitor.groupAddButton).isVisible()

    click(app.monitor.groupAddButton)

    assertThat(app.h1).hasText("Monitor - add group")

    click(app.monitorAddGroup.groupAddButton)

    assertThat(app.monitorAddGroup.nameFieldError).hasText("Name is required.")
    assertThat(app.monitorAddGroup.descriptionFieldError).hasText("Description is required.")

    app.monitorAddGroup.nameField.fill("group1")
    app.monitorAddGroup.descriptionField.fill("group one")

    waitForVisible(app.monitorAddGroup.valid)

    click(app.monitorAddGroup.groupAddButton)
    assertThat(app.monitor.routesInGroups).hasText("0 routes in 1 groups")

    click(app.monitor.link("group1"))

    assertThat(app.h1).hasText("group1group one")

    assertThat(app.group.adminToggle).isEnabled()
    assertThat(app.group.adminToggle).isChecked()

    assertThat(app.group.noRoutesInGroup).isVisible()
    assertThat(app.group.routeAddButton).isEnabled()

    click(app.group.adminToggle)

    assertThat(app.group.adminToggle).isChecked(new LocatorAssertions.IsCheckedOptions().setChecked(false))
    assertThat(app.group.routeAddButton).isHidden()
  }

  test("monitor add route with osm reference") {
    resetDatabase()

    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    database.users.save(User("test-user")) // login

    addGroup(app)

    click(app.group.routeAddButton)
    assertThat(app.h1).hasText("group one")
    assertThat(app.h2).hasText("Add route")

    click(app.routeAdd.cancelButton)
    assertThat(app.h1).hasText("group1group one")

    click(app.group.routeAddButton)
    assertThat(app.h1).hasText("group one")
    assertThat(app.h2).hasText("Add route")

    // step 1 not visible in route mode 'add':
    assertThat(app.routeAdd.step1.nextButton).isHidden()
    assertThat(app.routeAdd.step1.groupSelector).isHidden()

    // step 2:
    assertThat(app.routeAdd.step2.nameFieldError).isHidden()
    assertThat(app.routeAdd.step2.descriptionFieldError).isHidden()
    click(app.routeAdd.step2.nextButton)
    waitForVisible(app.routeAdd.step2.nameFieldError)
    assertThat(app.routeAdd.step2.nameFieldError).hasText("Name is required")
    assertThat(app.routeAdd.step2.descriptionFieldError).hasText("Description is required")
    app.routeAdd.step2.nameField.fill("route1")
    assertThat(app.routeAdd.step2.nameFieldError).isHidden()
    app.routeAdd.step2.descriptionField.fill("route one")
    assertThat(app.routeAdd.step2.descriptionFieldError).isHidden()
    waitForVisible(app.routeAdd.step2.valid)
    click(app.routeAdd.step2.nextButton)

    // step 3:
    assertThat(page.getByText("Do you know the OSM relation id for this route?")).isVisible()
    assertThat(app.routeAdd.step3.relationIdField).isHidden()
    assertThat(app.routeAdd.step3.verifyButton).isHidden()
    assertThat(app.routeAdd.step3.relationQuestionUnanswered).isHidden()
    assertThat(app.routeAdd.step3.relationIdMissingWarning).isHidden()
    assertThat(app.routeAdd.step3.relationIdUnknownComment).isHidden()
    click(app.routeAdd.step3.nextButton)
    assertThat(app.routeAdd.step3.relationQuestionUnanswered).isVisible()
    click(app.routeAdd.step3.relationIdKnownNoButton)
    assertThat(app.routeAdd.step3.relationQuestionUnanswered).isHidden()
    assertThat(app.routeAdd.step3.relationIdUnknownComment).isVisible()
    assertThat(app.routeAdd.step3.relationIdField).isHidden()
    click(app.routeAdd.step3.relationIdKnownNoButton)
    assertThat(app.routeAdd.step3.relationIdUnknownComment).isHidden()
    assertThat(app.routeAdd.step3.relationIdField).isVisible()
    assertThat(app.routeAdd.step3.verifyButton).isVisible()
    click(app.routeAdd.step3.nextButton)
    assertThat(app.routeAdd.step3.relationIdMissingWarning).isVisible()
    app.routeAdd.step3.relationIdField.fill("1")
    // TODO click(app.routeAdd.step3.verifyButton)
    click(app.routeAdd.step3.nextButton)

    // step 4:
    assertThat(page.getByText("What do you want to use as reference to compare the OSM relation to?")).isVisible()
    assertThat(app.routeAdd.step4.referenceTypeRequiredWarning).isHidden()
    click(app.routeAdd.step4.nextButton)
    assertThat(app.routeAdd.step4.referenceTypeRequiredWarning).isVisible()
    click(app.routeAdd.step4.referenceTypeOsm)
    assertThat(app.routeAdd.step4.referenceTypeRequiredWarning).isHidden()
    click(app.routeAdd.step4.nextButton)

    // step 5:
    assertThat(app.routeAdd.step5.osmReferenceDateField).isVisible()
    assertThat(app.routeAdd.step5.osmReferenceDateRequiredError).isHidden()
    app.routeAdd.step5.osmReferenceDateField.fill("")
    assertThat(app.routeAdd.step5.osmReferenceDateRequiredError).isVisible()
    app.routeAdd.step5.osmReferenceDateField.fill("1/5/2023")
    click(app.routeAdd.step5.nextButton)

    // step 6:
    assertThat(app.routeAdd.step6.commentField).isVisible()
    app.routeAdd.step6.commentField.fill("route comment")

    click(app.routeAdd.saveButton)

    waitForVisible(app.routeSaveDialog.routeSaved)
    click(app.routeSaveDialog.backToGroupButton)

    val groupRepository = new MonitorGroupRepositoryImpl(database)
    val routeRepository = new MonitorRouteRepositoryImpl(database)

    val group = groupRepository.groupByName("group1").get
    val route = routeRepository.routeByName(group._id, "route1").get
    val routeStates = routeRepository.routeStates(route._id)
    val routeReferences = routeRepository.routeReferences(route._id)

    println(route)

    println("done")

  }

  test("monitor add route with gpx reference") {
    resetDatabase()

    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    database.users.save(User("test-user")) // login

    addGroup(app)

    click(app.group.routeAddButton)

    // step 2:
    app.routeAdd.step2.nameField.fill("route1")
    app.routeAdd.step2.descriptionField.fill("route one")
    waitForVisible(app.routeAdd.step2.valid)
    click(app.routeAdd.step2.nextButton)

    // step 3:
    click(app.routeAdd.step3.relationIdKnownNoButton)
    click(app.routeAdd.step3.nextButton)

    // step 4:
    click(app.routeAdd.step4.referenceTypeGpx)
    click(app.routeAdd.step4.nextButton)

    // step 5:
    page.locator(".file-input").setInputFiles(Paths.get("/home/vmarc/wrk/osm/gpx/de-nieuwe-drenk-1.gpx"))
    click(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open calendar")))
    click(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("May 1, 2023")))
    click(app.routeAdd.step5.nextButton)

    // step 6:
    app.routeAdd.step6.commentField.fill("route comment")

    // save
    click(app.routeAdd.saveButton)

    waitForVisible(app.routeSaveDialog.routeSaved)
    click(app.routeSaveDialog.backToGroupButton)

    val groupRepository = new MonitorGroupRepositoryImpl(database)
    val routeRepository = new MonitorRouteRepositoryImpl(database)

    val group = groupRepository.groupByName("group1").get
    val route = routeRepository.routeByName(group._id, "route1").get
    val routeStates = routeRepository.routeStates(route._id)
    val routeReferences = routeRepository.routeReferences(route._id)

    println(route)

    println("done")
  }

  test("monitor add route with multi gpx references") {
    resetDatabase()

    val page = context.newPage
    val app = new Application(page)
    page.navigate(applicationUrl)

    database.users.save(User("test-user")) // login

    addGroup(app)

    click(app.group.routeAddButton)

    // step 2:
    app.routeAdd.step2.nameField.fill("route1")
    app.routeAdd.step2.descriptionField.fill("route one")
    waitForVisible(app.routeAdd.step2.valid)
    click(app.routeAdd.step2.nextButton)

    // step 3:
    click(app.routeAdd.step3.relationIdKnownNoButton)
    waitForVisible(app.routeAdd.step3.valid)
    click(app.routeAdd.step3.nextButton)

    // step 4:
    click(app.routeAdd.step4.referenceTypeMultiGpx)
    waitForVisible(app.routeAdd.step4.valid)
    click(app.routeAdd.step4.nextButton)

    // step 5:
    assertThat(app.routeAdd.step5.multiGpxComment).isVisible()
    assertThat(app.routeAdd.step5.osmReferenceDateField).isHidden()
    assertThat(app.routeAdd.step5.osmReferenceDateRequiredError).isHidden()
    assertThat(app.routeAdd.step5.gpxFileInput).isHidden()
    assertThat(app.routeAdd.step5.referenceFilenameRequired).isHidden()
    assertThat(app.routeAdd.step5.gpxReferenceDateField).isHidden()
    assertThat(app.routeAdd.step5.gpxReferenceDateError).isHidden()
    assertThat(app.routeAdd.step5.gpxFileName).isHidden()
    waitForVisible(app.routeAdd.step5.valid)
    click(app.routeAdd.step5.nextButton)

    // step 6:
    app.routeAdd.step6.commentField.fill("route comment")

    // save
    click(app.routeAdd.saveButton)

    waitForVisible(app.routeSaveDialog.routeSaved)
    click(app.routeSaveDialog.backToGroupButton)

    val groupRepository = new MonitorGroupRepositoryImpl(database)
    val routeRepository = new MonitorRouteRepositoryImpl(database)

    val group = groupRepository.groupByName("group1").get
    val route = routeRepository.routeByName(group._id, "route1").get
    val routeStates = routeRepository.routeStates(route._id)
    val routeReferences = routeRepository.routeReferences(route._id)

    route.name should equal("route1")
    route.description should equal("route one")
    route.comment should equal(Some("route comment"))
    route.relationId should equal(None)
    route.user should equal("test-user")
    route.referenceType should equal("multi-gpx")
    route.referenceTimestamp should equal(None)
    route.referenceFilename should equal(None)
    route.relation should equal(None)
    route.happy should equal(false)

    routeStates.size should equal(0)
    routeReferences.size should equal(0)
  }

  private def addGroup(app: Application): Unit = {
    click(app.home.monitorLink)
    click(app.monitor.adminToggle)
    click(app.monitor.groupAddButton)
    app.monitorAddGroup.nameField.fill("group1")
    app.monitorAddGroup.descriptionField.fill("group one")
    waitForVisible(app.monitorAddGroup.valid)
    click(app.monitorAddGroup.groupAddButton)
    click(app.monitor.link("group1"))
    assertThat(app.h1).hasText("group1group one")
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
