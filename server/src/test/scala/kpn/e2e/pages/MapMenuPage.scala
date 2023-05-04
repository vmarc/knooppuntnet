package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MapMenuPage(page: Page) extends TestPage(page) {

  def cyclingLink: Locator = link("Cycling")

  def hikingLink: Locator = link("Hiking")

  def horseRidingLink: Locator = link("Horse riding")

  def motorboatLink: Locator = link("Motorboat")

  def canoeLink: Locator = link("Canoe")

  def inlineSkatingLink: Locator = link("Inline skating")
}
