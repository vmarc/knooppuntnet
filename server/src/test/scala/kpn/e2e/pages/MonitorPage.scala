package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorPage(page: Page) extends TestPage(page) {

  def adminToggle: Locator = page.locator("#admin-toggle-button")

  def groupAddButton: Locator = button("Add group")

  def routesInGroups: Locator = page.locator("#routes-in-groups")

  def noGroups: Locator = page.locator("#no-groups")

}
