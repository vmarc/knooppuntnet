package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteAddStep2(page: Page) extends TestPage(page) {

  def nameField: Locator = page.locator("#name")

  def nameFieldError: Locator = page.locator("kpn-monitor-group-name .kpn-form-error")

  def descriptionField: Locator = page.locator("#description")

  def descriptionFieldError: Locator = page.locator("kpn-monitor-group-description .kpn-form-error")

  def backButton: Locator = page.locator("#step2-back")

  def nextButton: Locator = page.locator("#step2-next")

  def valid: Locator = page.locator("#step2-form-valid")

}
