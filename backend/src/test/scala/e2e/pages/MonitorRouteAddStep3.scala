package e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MonitorRouteAddStep3(page: Page) extends TestPage(page) {

  def relationIdKnownYesButton: Locator = page.locator("#relation-id-known-yes input")

  def relationIdKnownNoButton: Locator = page.locator("#relation-id-known-no input")

  def relationQuestionUnanswered: Locator = page.locator("#relation.question-unanswered")

  def relationIdUnknownComment: Locator = page.locator("#relation-id-unknown-comment")

  def relationIdField: Locator = page.locator("#relation-id")

  def verifyButton: Locator = page.locator("#verify")

  def relationIdMissingWarning: Locator = page.locator("#relation-id-missing-warning")

  def backButton: Locator = page.locator("#step3-back")

  def nextButton: Locator = page.locator("#step3-next")

  def valid: Locator = page.locator("#step3-form-valid")

}
