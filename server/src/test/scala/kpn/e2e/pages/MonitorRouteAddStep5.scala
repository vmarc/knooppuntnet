package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteAddStep5(page: Page) extends TestPage(page) {

  def osmReferenceDateField: Locator = page.locator("#osm-reference-date input")

  def osmReferenceDateRequiredError: Locator = page.locator("#osm-reference-date-required-error")

  def gpxFileInput: Locator = page.locator("#gpx-file-input")

  def referenceFilenameRequired: Locator = page.locator("#reference-filename.required")

  def gpxReferenceDateField: Locator = page.locator("#gpx-reference-date input")

  def gpxReferenceDateError: Locator = page.locator("#reference-day.required")

  def multiGpxComment: Locator = page.getByText("Further reference details can be provided later.")

  def gpxFileName: Locator = page.locator("#gpx-file-name")

  def backButton: Locator = page.locator("#step5-back")

  def nextButton: Locator = page.locator("#step5-next")

  def valid: Locator = page.locator("#step5-form-valid")

}
