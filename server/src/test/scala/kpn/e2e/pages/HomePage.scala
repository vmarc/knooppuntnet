package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class HomePage(page: Page) extends TestPage(page) {

  def mapLink: Locator = link("Map")

  def analysisLink: Locator = link("Analysis")

  def monitorLink: Locator = link("Monitor")

  def demoLink: Locator = link("Demo")
}
