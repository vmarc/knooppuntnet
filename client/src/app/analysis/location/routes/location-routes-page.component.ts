import {ChangeDetectorRef} from "@angular/core";
import {OnDestroy} from "@angular/core";
import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {ActivatedRoute} from "@angular/router";
import {LocationRoutesPage} from "../../../kpn/api/common/location/location-routes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
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

    <div *ngIf="response">
      <div *ngIf="!page">
        <p i18n="@@location-routes.location-not-found">Location not found</p>
      </div>
      <div *ngIf="page">
        <div *ngIf="page.routes.isEmpty()" i18n="@@location-routes.no-routes">
          No routes
        </div>
        <kpn-location-route-table
          *ngIf="!page.routes.isEmpty()"
          (page)="pageChanged($event)"
          [timeInfo]="page.timeInfo"
          [routes]="page.routes"
          [routeCount]="page.summary.routeCount">
        </kpn-location-route-table>
      </div>
      <kpn-json [object]="page"></kpn-json>
    </div>
  `,
  providers: [
    LocationRoutesPageService
  ]
})
export class LocationRoutesPageComponent implements OnInit, OnDestroy {

  json = JSON;

  response: ApiResponse<LocationRoutesPage> = null;
  locationKey: LocationKey;
  private readonly subscriptions = new Subscriptions();

  constructor(public service: LocationRoutesPageService,
              private activatedRoute: ActivatedRoute,
              private cdr: ChangeDetectorRef) {
    console.log("LocationRoutesPageComponent constructor");
  }

  get page(): LocationRoutesPage {
    return this.response.result;
  }

  ngOnInit(): void {
    console.log("LocationRoutesPageComponent ngOnInit()");
    this.service.response.subscribe(response => {
      this.response = response;
    });
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        this.locationKey = LocationParams.toKey(params);
        this.service.setParams(params);
        this.cdr.detectChanges();
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
