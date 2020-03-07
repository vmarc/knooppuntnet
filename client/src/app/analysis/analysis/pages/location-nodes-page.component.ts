import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {Country} from "../../../kpn/api/custom/country";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Subscriptions} from "../../../util/Subscriptions";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-networks-page",
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li>
        <a [routerLink]="networkTypeLink()">
          <kpn-network-type-name [networkType]="networkType"></kpn-network-type-name>
        </a>
      </li>
      <li>
        <a [routerLink]="countryLink()">
          <kpn-country-name [country]="country"></kpn-country-name>
        </a>
      </li>
      <li>{{location}}</li>
    </ul>

    <h1>
      <kpn-network-type-name [networkType]="networkType"></kpn-network-type-name>
      in
      {{location}}
    </h1>

    <div>
      <b>Nodes</b> | <a>Routes</a> | <a>Facts</a> | <a>Map</a> | <a>Changes</a>
    </div>
  `
})
export class LocationNodesPageComponent implements OnInit, OnDestroy {

  networkType: NetworkType;
  country: Country;
  location = "";

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => {
          this.networkType = NetworkType.withName(params["networkType"]);
          this.country = new Country(params["country"]);
          this.location = params["location"];
          return params["networkType"];
        }),
        flatMap(networkType => this.appService.location(networkType))
      ).subscribe(response => { /* process result */
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  networkTypeLink(): string {
    return `/analysis/${this.networkType.name}`;
  }

  countryLink(): string {
    return `/analysis/${this.networkType.name}/${this.country.domain}`;
  }

}
