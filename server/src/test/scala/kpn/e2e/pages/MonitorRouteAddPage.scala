package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole

class MonitorRouteAddPage(page: Page) extends TestPage(page) {

  def nameField: Locator = {
    page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name"))
  }

  def nameFieldError: Locator = {
    page.locator("kpn-monitor-group-name .kpn-form-error")
  }

  def descriptionField: Locator = {
    page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Description"))
  }

  def descriptionFieldError: Locator = {
    page.locator("kpn-monitor-group-description .kpn-form-error")
  }

  def step1NextButton: Locator = page.locator("#step1-next")

  def step2BackButton: Locator = page.locator("#step2-back")

  def step2NextButton: Locator = page.locator("#step2-next")

  def step3BackButton: Locator = page.locator("#step3-back")

  def step3NextButton: Locator = page.locator("#step3-next")

  def step4BackButton: Locator = page.locator("#step4-back")

  def step4NextButton: Locator = page.locator("#step4-next")

  def step5BackButton: Locator = page.locator("#step5-back")

  def step5NextButton: Locator = page.locator("#step5-next")

  def step6BackButton: Locator = page.locator("#step6-back")


  def saveButton: Locator = page.locator("#save")

  def cancelButton: Locator = page.locator("#cancel")

}
