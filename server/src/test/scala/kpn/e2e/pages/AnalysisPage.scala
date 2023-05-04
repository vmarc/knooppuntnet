package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class AnalysisPage(page: Page) extends TestPage(page) {

  def overviewLink: Locator = link("Overview")

  def changesLink: Locator = link("Changes")

  def cyclingLink: Locator = link("Cycling")

  def hikingLink: Locator = link("Hiking")

  def horseRidingLink: Locator = link("Horse riding")

  def motorboatLink: Locator = link("Motorboat")

  def canoeLink: Locator = link("Canoe")

  def inlineSkatingLink: Locator = link("Inline skating")
}
