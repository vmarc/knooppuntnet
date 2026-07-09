package e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorGroupPage(page: Page) extends TestPage(page) {

  def adminToggle: Locator = page.locator("#admin-toggle-button")

  def routeAddButton: Locator = page.locator("#add-route")

  def noRoutesInGroup: Locator = page.locator("#no-routes")

}
