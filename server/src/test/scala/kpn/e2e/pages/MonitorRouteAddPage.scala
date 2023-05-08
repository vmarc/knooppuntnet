package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteAddPage(page: Page) extends TestPage(page) {

  def step1: MonitorRouteAddStep1 = new MonitorRouteAddStep1(page)

  def step2: MonitorRouteAddStep2 = new MonitorRouteAddStep2(page)

  def step3: MonitorRouteAddStep3 = new MonitorRouteAddStep3(page)

  def step4: MonitorRouteAddStep4 = new MonitorRouteAddStep4(page)

  def step5: MonitorRouteAddStep5 = new MonitorRouteAddStep5(page)

  def step6: MonitorRouteAddStep6 = new MonitorRouteAddStep6(page)

  def saveButton: Locator = page.locator("#save")

  def cancelButton: Locator = page.locator("#cancel")
}
