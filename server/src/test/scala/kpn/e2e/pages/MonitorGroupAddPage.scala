package kpn.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole

class MonitorGroupAddPage(page: Page) extends TestPage(page) {

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

  def groupAddButton: Locator = page.locator("#add-group")

  def cancelButton: Locator = page.locator("#cancel")

  def valid: Locator = page.locator("#group-form-valid")

}
