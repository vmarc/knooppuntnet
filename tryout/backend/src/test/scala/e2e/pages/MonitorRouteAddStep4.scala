package e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteAddStep4(page: Page) extends TestPage(page) {

  def backButton: Locator = page.locator("#step4-back")

  def nextButton: Locator = page.locator("#step4-next")

  def referenceTypeGpx: Locator = page.getByText("A GPX trace that you will upload now")

  def referenceTypeMultiGpx: Locator = page.getByText("Multiple GPX traces")

  def referenceTypeOsm: Locator = page.getByText("The current or a previous state of the OSM relation")

  def referenceTypeRequiredWarning: Locator = page.locator("#reference-type.required")

  def valid: Locator = page.locator("#step4-form-valid")

}
