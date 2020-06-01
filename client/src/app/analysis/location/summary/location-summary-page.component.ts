import { Component, OnInit, ChangeDetectionStrategy } from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {LocationNodesPageService} from "../nodes/location-nodes-page.service";
import {LocationSummaryPageService} from "./location-summary-page.service";

@Component({
  selector: "kpn-location-summary-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="summary"
      pageTitle="Summary"
      i18n-pageTitle="@@location-summary.title">
    </kpn-location-page-header>

    <div *ngIf="service.response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-summary [page]="response.result"></kpn-location-summary>
      </kpn-location-response>
    </div>
  `,
  providers: [
    LocationSummaryPageService
  ]
})
export class LocationSummaryPageComponent implements OnInit {

  constructor(public service: LocationSummaryPageService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }
}

