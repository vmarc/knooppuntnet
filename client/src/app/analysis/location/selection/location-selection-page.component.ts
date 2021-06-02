import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LocationNode } from '@api/common/location/location-node';
import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { Countries } from '../../../kpn/common/countries';
import { NetworkTypes } from '../../../kpn/common/network-types';
import { LocationModeService } from './location-mode.service';
import { LocationSelectionService } from './location-selection.service';

@Component({
  selector: 'kpn-location-selection-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-error></kpn-error>

    <div *ngIf="locationNode$ | async as locationNode">
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li>
          <a [routerLink]="networkTypeLink()">
            <kpn-network-type-name
              [networkType]="networkType"
            ></kpn-network-type-name>
          </a>
        </li>
        <li>
          <kpn-country-name [country]="country"></kpn-country-name>
        </li>
      </ul>

      <kpn-page-header [pageTitle]="'Locations'" subject="network-page">
        <kpn-network-type-name
          [networkType]="networkType"
        ></kpn-network-type-name>
        <span i18n="@@subset.in" class="in">in</span>
        <kpn-country-name [country]="country"></kpn-country-name>
      </kpn-page-header>

      <kpn-location-mode></kpn-location-mode>

      <div *ngIf="isModeName() | async">
        <kpn-location-selector
          [country]="country"
          [locationNode]="locationNode"
          (selection)="selected($event)"
        >
        </kpn-location-selector>
      </div>

      <div *ngIf="isModeTree() | async">
        <kpn-location-tree
          [networkType]="networkType"
          [country]="country"
          [locationNode]="locationNode"
          (selection)="selected($event)"
        >
        </kpn-location-tree>
      </div>
    </div>
  `,
  styles: [
    `
      .in:before {
        content: ' ';
      }

      .in:after {
        content: ' ';
      }
    `,
  ],
})
export class LocationSelectionPageComponent implements OnInit {
  locationNode$: Observable<LocationNode>;

  networkType: NetworkType;
  country: Country;

  constructor(
    private activatedRoute: ActivatedRoute,
    private locationModeService: LocationModeService,
    private locationSelectionService: LocationSelectionService,
    private router: Router
  ) {}

  isModeName() {
    return this.locationModeService.isModeName;
  }

  isModeTree() {
    return this.locationModeService.isModeTree;
  }

  selected(locationName: string): void {
    const url = `/analysis/${this.networkType}/${this.country}/${locationName}/nodes`;
    this.router.navigateByUrl(url);
  }

  ngOnInit() {
    this.locationNode$ = this.activatedRoute.params.pipe(
      map((params) => {
        this.networkType = NetworkTypes.withName(params['networkType']);
        this.country = Countries.withDomain(params['country']);
        return { country: this.country, networkType: this.networkType };
      }),
      mergeMap((subset) =>
        this.locationSelectionService.locations(
          subset.networkType,
          subset.country
        )
      )
    );
  }

  networkTypeLink(): string {
    return `/analysis/${this.networkType}`;
  }
}
