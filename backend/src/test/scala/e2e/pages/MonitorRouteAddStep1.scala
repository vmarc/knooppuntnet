package e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteAddStep1(page: Page) extends TestPage(page) {

  def groupSelector: Locator = page.locator("#page-selector")

  def nextButton: Locator = page.locator("#step1-next")

}
