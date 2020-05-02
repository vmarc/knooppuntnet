import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {flatMap} from "rxjs/operators";
import {LocationNode} from "../../../kpn/api/common/location/location-node";
import {Country} from "../../../kpn/api/custom/country";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Subset} from "../../../kpn/api/custom/subset";
import {LocationModeService} from "./location-mode.service";
import {LocationSelectionService} from "./location-selection.service";

@Component({
  selector: "kpn-location-selection-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="locationNode$ | async as locationNode">

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
        <span i18n="@@subset.in" class="in">in</span>
        <kpn-country-name [country]="country"></kpn-country-name>
      </kpn-page-header>

      <kpn-location-mode></kpn-location-mode>

      <div *ngIf="isModeName() | async">
        <kpn-location-selector
          [country]="country"
          [locationNode]="locationNode"
          (selection)="selected($event)">
        </kpn-location-selector>
      </div>

      <div *ngIf="isModeTree() | async">
        <kpn-location-tree
          [networkType]="networkType"
          [country]="country"
          [locationNode]="locationNode"
          (selection)="selected($event)">
        </kpn-location-tree>
      </div>
    </div>
  `,
  styles: [`
    .in:before {
      content: ' ';
    }

    .in:after {
      content: ' ';
    }
  `]
})
export class LocationSelectionPageComponent implements OnInit {

  locationNode$: Observable<LocationNode>;

  networkType: NetworkType;
  country: Country;

  constructor(private activatedRoute: ActivatedRoute,
              private locationModeService: LocationModeService,
              private locationSelectionService: LocationSelectionService,
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
    this.locationNode$ = this.activatedRoute.params.pipe(
      map(params => {
        this.networkType = NetworkType.withName(params["networkType"]);
        this.country = new Country(params["country"]);
        return new Subset(this.country, this.networkType);
      }),
      flatMap(subset => {
        return this.locationSelectionService.locations(subset.networkType, subset.country);
      })
    );
  }

  networkTypeLink(): string {
    return `/analysis/${this.networkType.name}`;
  }

}
