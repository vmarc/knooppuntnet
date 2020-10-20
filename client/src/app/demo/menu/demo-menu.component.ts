import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {PageService} from "../../components/shared/page.service";

@Component({
  selector: "kpn-demo-menu",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="header">
      <h1>
        Video demos
      </h1>
    </div>
    <ol>
      <li>
        <p>
          <a routerLink="plan-a-route">How to plan a route?</a>
        </p>
      </li>
      <li>
        <p>
          <a routerLink="zoom-in">How to zoom in to a selected area of the map?</a>
        </p>
      </li>
    </ol>
  `
})
export class DemoMenuComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
  }

}
