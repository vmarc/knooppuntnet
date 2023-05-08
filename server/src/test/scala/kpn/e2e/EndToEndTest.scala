package kpn.e2e

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.WaitForSelectorState
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers

class EndToEndTest extends AnyFunSuite with BeforeAndAfterEach with Matchers {

  private var playwright: Playwright = _
  private var browser: Browser = _
  protected var context: BrowserContext = _
  protected val applicationUrl = "http://localhost:4000"

  override def beforeEach(): Unit = {
    playwright = Playwright.create
    val launchOptions = new BrowserType.LaunchOptions().setHeadless(false) // .setSlowMo(1000)
    browser = playwright.firefox.launch(launchOptions)
    context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1600, 900))
  }

  override def afterEach(): Unit = {
    playwright.close()
  }

  def click(locator: Locator): Unit = {
    waitForVisible(locator)
    locator.click()
  }

  def waitForVisible(locator: Locator): Unit = {
    val visible = new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
    locator.waitFor(visible)
  }
}
