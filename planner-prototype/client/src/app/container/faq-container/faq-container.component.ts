import {Component} from "@angular/core";
import {Router} from "@angular/router";

@Component({
  selector: "app-faq-container",
  templateUrl: "./faq-container.component.html",
  styleUrls: ["./faq-container.component.scss"]
})
export class FaqContainerComponent {

  constructor(private router: Router) {
  }

  homePage() {
    this.router.navigate(["/"]);
  }
}
