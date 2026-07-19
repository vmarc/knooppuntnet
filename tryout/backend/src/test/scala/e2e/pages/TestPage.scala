package e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole

class TestPage(page: Page) {

  def link(name: String): Locator = {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(name))
  }

  def linkExact(name: String): Locator = {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(name).setExact(true))
  }

  def button(name: String): Locator = {
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name))
  }
}
