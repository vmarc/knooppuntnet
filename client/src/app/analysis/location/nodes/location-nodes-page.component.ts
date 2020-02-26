import {Component} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {LocationNodesPage} from "../../../kpn/api/common/location/location-nodes-page";
import {LocationNodesParameters} from "../../../kpn/api/common/location/location-nodes-parameters";
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
          (page)="pageChanged($event)"
          [timeInfo]="page.timeInfo"
          [nodes]="page.nodes"
          [nodeCount]="page.summary.nodeCount">
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
  }

  private _parameters = new LocationNodesParameters(5, 0);

  get parameters(): LocationNodesParameters {
    return this._parameters;
  }

  set parameters(parameters: LocationNodesParameters) {
    this._parameters = parameters;
    this.reload();
  }

  get page(): LocationNodesPage {
    return this.response.result;
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        const networkType = NetworkType.withName(params["networkType"]);
        const country = Countries.withDomain(params["country"]);
        const name = params["location"];
        this.locationKey = new LocationKey(networkType, country, name);
        // TODO read parameters from localstorage instead
        this.parameters = new LocationNodesParameters(5, 0);
      })
    );
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this.parameters = {...this.parameters, pageIndex: event.pageIndex, itemsPerPage: event.pageSize};
  }

  private reload() {
    console.log("this.parameters=" + JSON.stringify(this.parameters));
    this.appService.locationNodes(this.locationKey, this.parameters).subscribe(response => {
      this.response = response;
    });
  }
}
