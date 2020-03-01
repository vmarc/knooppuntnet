import {ChangeDetectorRef} from "@angular/core";
import {OnDestroy} from "@angular/core";
import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {ActivatedRoute} from "@angular/router";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {Subscriptions} from "../../../util/Subscriptions";
import {LocationParams} from "../components/location-params";
import {LocationRoutesPageService} from "./location-routes-page.service";

@Component({
  selector: "kpn-location-routes-page",
  template: `
    <kpn-location-page-header
      [locationKey]="locationKey"
      pageTitle="Routes"
      i18n-pageTitle="@@location-routes.title">
    </kpn-location-page-header>

    <div *ngIf="service.response | async as response">
      <div *ngIf="!response.result">
        <p i18n="@@location-routes.location-not-found">Location not found</p>
      </div>
      <div *ngIf="response.result">
        <div *ngIf="response.result.routes.isEmpty()" i18n="@@location-routes.no-routes">
          No routes
        </div>
        <kpn-location-route-table
          *ngIf="!response.result.routes.isEmpty()"
          (page)="pageChanged($event)"
          [timeInfo]="response.result.timeInfo"
          [routes]="response.result.routes"
          [routeCount]="response.result.summary.routeCount">
        </kpn-location-route-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `,
  providers: [
    LocationRoutesPageService
  ]
})
export class LocationRoutesPageComponent implements OnInit, OnDestroy {

  locationKey: LocationKey;

  private readonly subscriptions = new Subscriptions();

  constructor(public service: LocationRoutesPageService,
              private activatedRoute: ActivatedRoute,
              private cdr: ChangeDetectorRef) {
    console.log("LocationRoutesPageComponent constructor");
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        this.locationKey = LocationParams.toKey(params);
        this.service.setParams(this.locationKey);
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    // this.parameters = {...this.parameters, pageIndex: event.pageIndex, itemsPerPage: event.pageSize};
  }

}
