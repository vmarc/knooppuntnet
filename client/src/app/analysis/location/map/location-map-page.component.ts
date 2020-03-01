import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {LocationMapPageService} from "./location-map-page.service";

@Component({
  selector: "kpn-location-map-page",
  template: `
    <kpn-location-page-header
      pageTitle="Map"
      i18n-pageTitle="@@location-map.title">
    </kpn-location-page-header>

    <div *ngIf="service.response | async as response">
      <kpn-location-response [response]="response">
        <kpn-location-map></kpn-location-map>
      </kpn-location-response>
    </div>
  `,
  providers: [
    LocationMapPageService
  ]
})
export class LocationMapPageComponent {

  constructor(public service: LocationMapPageService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }
}
