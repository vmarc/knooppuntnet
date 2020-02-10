import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationNodesPage} from "../../../kpn/api/common/location/location-nodes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Countries} from "../../../kpn/common/countries";
import {Subscriptions} from "../../../util/Subscriptions";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-nodes-page",
  template: `
    <kpn-location-page-header
      [locationKey]="locationKey"
      pageTitle="Nodes"
      i18n-pageTitle="@@location-nodes.title">
    </kpn-location-page-header>

    <div *ngIf="response">
      <div *ngIf="!page">
        <p i18n="@@location-nodes.location-not-found">Location not found</p>
      </div>
      <div *ngIf="page">
        <div *ngIf="page.nodes.isEmpty()" i18n="@@location-nodes.no-nodes">
          No nodes
        </div>
        <kpn-location-node-table
          *ngIf="!page.nodes.isEmpty()"
          [timeInfo]="page.timeInfo"
          [nodes]="page.nodes">
        </kpn-location-node-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class LocationNodesPageComponent {

  locationKey: LocationKey;
  response: ApiResponse<LocationNodesPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => {
          const networkType = NetworkType.withName(params["networkType"]);
          const country = Countries.withDomain(params["country"]);
          const name = params["location"];
          return new LocationKey(networkType, country, name);
        }),
        tap(locationKey => this.locationKey = locationKey),
        flatMap(locationKey => this.appService.locationNodes(locationKey))
      ).subscribe(response => this.response = response)
    );
  }

  get page(): LocationNodesPage {
    return this.response.result;
  }
}
