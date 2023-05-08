package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteSaveDialog(page: Page) extends TestPage(page) {

  def backToGroupButton: Locator = page.locator("#back-to-group-button")

  def gotoAnalysisResultButton: Locator = page.locator("#goto-analysis-result-button")

  def routeSaved: Locator = page.locator(s"#route-saved")

}