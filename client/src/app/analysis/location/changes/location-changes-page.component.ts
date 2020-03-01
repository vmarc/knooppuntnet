import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {LocationChangesPageService} from "./location-changes-page.service";

@Component({
  selector: "kpn-location-changes-page",
  template: `
    <kpn-location-page-header
      pageTitle="Changes"
      i18n-pageTitle="@@location-changes.title">
    </kpn-location-page-header>

    <div *ngIf="service.response | async as response">
      <kpn-location-response [response]="response">
        <kpn-location-changes></kpn-location-changes>
      </kpn-location-response>
    </div>
  `,
  providers: [
    LocationChangesPageService
  ]
})
export class LocationChangesPageComponent {

  constructor(public service: LocationChangesPageService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }

}
