import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {LocationRoutesPageService} from "./location-routes-page.service";

@Component({
  selector: "kpn-location-routes-page",
  template: `
    <kpn-location-page-header
      pageTitle="Routes"
      i18n-pageTitle="@@location-routes.title">
    </kpn-location-page-header>

    <div *ngIf="service.response | async as response">
      <kpn-location-response [response]="response">
        <kpn-location-routes [page]="response.result"></kpn-location-routes>
      </kpn-location-response>
    </div>
  `,
  providers: [
    LocationRoutesPageService
  ]
})
export class LocationRoutesPageComponent implements OnInit {

  constructor(public service: LocationRoutesPageService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }

}
