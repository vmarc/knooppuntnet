package e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteAddStep6(page: Page) extends TestPage(page) {

  def commentField: Locator = page.locator("#comment")

  def backButton: Locator = page.locator("#step6-back")
}
