package e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole

class PlannerPage(page: Page) extends TestPage(page) {

  def undoButton: Locator = button("Undo")

  def redoButton: Locator = button("Redo")

  def resetButton: Locator = button("Reset")

  def reverseButton: Locator = button("Reverse")

  def outputButton: Locator = button("Output")

  def zoomInButton: Locator = button("+")

  def zoomOutButton: Locator = button("-")

  def searchButton: Locator = {
    page.locator("#gcd-button-control")
  }

  def searchField: Locator = page.getByPlaceholder("Search for...")

  def selectionEssen: Locator = linkExact("Essen, Antwerp, Flanders, 2910, Belgium")

  def modeSurface: Locator = page.getByText("Surface")

  def modeDateLastSurvey: Locator = page.getByText("Date last survey")

  def modeQuality: Locator = page.getByText("Node and route quality status")

  def layerButton: Locator = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("select the layers displayed in the map"))

  def layerPois: Locator = page.getByText("Points of interest")

  def poiLayerTourism: Locator = page.getByText("Tourism")

  def poiLayerPlacesToStay: Locator = page.getByText("Places to stay")

  def poiLayerRestaurants: Locator = page.getByText("Restaurants")

  def poiLayerLandmarks: Locator = page.getByText("Landmarks")
}
