import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LocationNode } from '@api/common/location/location-node';
import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { Countries } from '../../../kpn/common/countries';
import { NetworkTypes } from '../../../kpn/common/network-types';
import { actionLocationSelectionPageDestroy } from '../store/location.actions';
import { actionLocationSelectionPageInit } from '../store/location.actions';
import { LocalLocationNode } from './local-location-node';
import { LocationModeService } from './location-mode.service';
import { LocationSelectionService } from './location-selection.service';

@Component({
  selector: 'kpn-location-selection-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-error />

    <div *ngIf="locationNode$ | async as locationNode">
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li>
          <a [routerLink]="networkTypeLink()">
            <kpn-network-type-name [networkType]="networkType" />
          </a>
        </li>
        <li>
          <kpn-country-name [country]="country" />
        </li>
      </ul>

      <kpn-page-header [pageTitle]="'Locations'" subject="network-page">
        <span class="header-network-type-icon">
          <mat-icon [svgIcon]="networkType" />
        </span>
        <kpn-network-type-name [networkType]="networkType" />
        <span i18n="@@subset.in" class="in">in</span>
        <kpn-country-name [country]="country" />
      </kpn-page-header>

      <div *ngIf="isModeName() | async">
        <kpn-location-selector
          [country]="country"
          [locationNode]="locationNode"
          (selection)="selected($event)"
        />
      </div>

      <div *ngIf="isModeTree() | async">
        <kpn-location-tree
          [networkType]="networkType"
          [country]="country"
          [locationNode]="locationNode"
          (selection)="selected($event)"
        />
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
export class LocationSelectionPageComponent implements OnInit, OnDestroy {
  locationNode$: Observable<LocalLocationNode>;

  networkType: NetworkType;
  country: Country;

  constructor(
    private activatedRoute: ActivatedRoute,
    private locationModeService: LocationModeService,
    private locationSelectionService: LocationSelectionService,
    private router: Router,
    private store: Store
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
    this.store.dispatch(actionLocationSelectionPageInit());

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
      ),
      map((locationNode) => this.toLocalLocationNode([], locationNode))
    );
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationSelectionPageDestroy());
  }

  private toLocalLocationNode(
    parents: LocationNode[],
    locationNode: LocationNode
  ): LocalLocationNode {
    const localPath = parents.map((ln) => ln.name).join(':');
    const childParents: LocationNode[] = [];
    parents.forEach((parent) => childParents.push(parent));
    childParents.push(locationNode);

    let localChildren: LocalLocationNode[] = [];
    if (locationNode.children) {
      localChildren = locationNode.children.map((child) =>
        this.toLocalLocationNode(childParents, child)
      );
    }

    return {
      path: localPath,
      name: locationNode.name,
      nodeCount: locationNode.nodeCount,
      children: localChildren,
    };
  }

  networkTypeLink(): string {
    return `/analysis/${this.networkType}`;
  }
}
