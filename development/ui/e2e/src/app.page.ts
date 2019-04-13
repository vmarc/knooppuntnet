import { browser, by, element } from "protractor";

export class AppPage {

  navigateTo() {
    return browser.get("/");
  }

  get title() {
    return element(by.css("app-root p")).getText();
  }
}
