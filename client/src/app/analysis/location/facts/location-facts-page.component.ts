import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {LocationFactsPageService} from "./location-facts-page.service";

@Component({
  selector: "kpn-location-facts-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@location-facts.title">
    </kpn-location-page-header>

    <div *ngIf="service.response | async as response">
      <kpn-location-response [response]="response">
        <kpn-location-facts [locationFacts]="response.result.locationFacts"></kpn-location-facts>
      </kpn-location-response>
    </div>
  `,
  providers: [
    LocationFactsPageService
  ]
})
export class LocationFactsPageComponent {

  constructor(public service: LocationFactsPageService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }
}
