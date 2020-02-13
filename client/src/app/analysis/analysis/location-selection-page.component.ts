import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {flatMap, map} from "rxjs/operators";
import {AppService} from "../../app.service";
import {Country} from "../../kpn/api/custom/country";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {Subscriptions} from "../../util/Subscriptions";
import {LocationModeService} from "./location-mode.service";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-selection-page",
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
        <kpn-country-name [country]="country"></kpn-country-name>
      </li>
    </ul>

    <kpn-page-header [pageTitle]="'Locations'" subject="network-page">
      <kpn-network-type-name [networkType]="networkType"></kpn-network-type-name>
      <span i18n="@@subset.in">in</span>
      <kpn-country-name [country]="country"></kpn-country-name>
    </kpn-page-header>

    <kpn-location-mode></kpn-location-mode>

    <div *ngIf="isModeName() | async">
      <kpn-location-selector [country]="country" (selection)="selected($event)"></kpn-location-selector>
    </div>

    <div *ngIf="isModeTree() | async">
      <kpn-location-tree [country]="country" (selection)="selected($event)"></kpn-location-tree>
    </div>
  `
})
export class LocationSelectionPageComponent implements OnInit {

  networkType: NetworkType;
  country: Country;

  private readonly subscriptions = new Subscriptions();

  constructor(private locationModeService: LocationModeService,
              private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private router: Router) {
  }

  isModeName() {
    return this.locationModeService.isModeName;
  }

  isModeTree() {
    return this.locationModeService.isModeTree;
  }

  selected(locationName: string): void {
    const url = `/analysis/${this.networkType.name}/${this.country.domain}/${locationName}/nodes`;
    this.router.navigateByUrl(url);
  }

  ngOnInit() {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => {
          this.networkType = NetworkType.withName(params["networkType"]);
          this.country = new Country(params["country"]);
          return params["networkType"];
        }),
        flatMap(networkType => this.appService.location(networkType))
      ).subscribe(response => { /* process result */
      })
    );
  }

  networkTypeLink(): string {
    return `/analysis/${this.networkType.name}`;
  }

}
