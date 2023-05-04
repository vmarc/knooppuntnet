package kpn.e2e.pages

import com.microsoft.playwright.Page
import com.microsoft.playwright.Page.GoBackOptions
import com.microsoft.playwright.options.WaitUntilState
import com.microsoft.playwright.Locator
import com.microsoft.playwright.options.AriaRole

class Application(page: Page) {

  def home: HomePage = new HomePage(page)

  def mapMenu: MapMenuPage = new MapMenuPage(page)

  def planner: PlannerPage = new PlannerPage(page)

  def analysis: AnalysisPage = new AnalysisPage(page)

  def title: Locator = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(1))

  def goBack(): Unit = page.goBack(new GoBackOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED))
}
