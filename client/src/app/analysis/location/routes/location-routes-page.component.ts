import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {LocationRoutesPageService} from "./location-routes-page.service";

@Component({
  selector: "kpn-location-routes-page",
  template: `
    <kpn-location-page-header
      [locationKey]="service.locationKey | async"
      pageTitle="Routes"
      i18n-pageTitle="@@location-routes.title">
    </kpn-location-page-header>

    <div *ngIf="service.response | async as response">
      <div *ngIf="!response.result">
        <p i18n="@@location-routes.location-not-found">Location not found</p>
      </div>
      <div *ngIf="response.result">
        <kpn-location-routes [page]="response.result"></kpn-location-routes>
      </div>
      <kpn-json [object]="response"></kpn-json>
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
