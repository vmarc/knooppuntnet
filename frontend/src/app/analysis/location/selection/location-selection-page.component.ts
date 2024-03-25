import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { CountryNameComponent } from '@app/components/shared';
import { NetworkTypeNameComponent } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { LocationSelectionSidebarComponent } from './components/location-selection-sidebar.component';
import { LocationSelectorComponent } from './components/location-selector.component';
import { LocationTreeComponent } from './components/location-tree.component';
import { LocationSelectionPageService } from './location-selection-page.service';

@Component({
  selector: 'kpn-location-selection-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-error />

      @if (service.locationNode(); as locationNode) {
        <div>
          <ul class="breadcrumb">
            <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
            <li>
              <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
            </li>
            <li>
              <a [routerLink]="'/analysis/' + service.networkType()">
                <kpn-network-type-name [networkType]="service.networkType()" />
              </a>
            </li>
            <li>
              <kpn-country-name [country]="service.country()" />
            </li>
          </ul>
          <kpn-page-header [pageTitle]="'Locations'" subject="network-page">
            <span class="header-network-type-icon">
              <mat-icon [svgIcon]="service.networkType()" />
            </span>
            <kpn-network-type-name [networkType]="service.networkType()" />
            <span i18n="@@subset.in" class="in">in</span>
            <kpn-country-name [country]="service.country()" />
          </kpn-page-header>
          @if (service.isModeName()) {
            <div>
              <kpn-location-selector
                [country]="service.country()"
                [locationNode]="locationNode"
                (selection)="selected($event)"
              />
            </div>
          }
          @if (service.isModeTree()) {
            <div>
              <kpn-location-tree
                [networkType]="service.networkType()"
                [country]="service.country()"
                [locationNode]="locationNode"
                (selection)="selected($event)"
              />
            </div>
          }
        </div>
      }
      <kpn-location-selection-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
    .in:before {
      content: ' ';
    }

    .in:after {
      content: ' ';
    }
  `,
  providers: [LocationSelectionPageService, RouterService],
  standalone: true,
  imports: [
    CountryNameComponent,
    ErrorComponent,
    LocationSelectionSidebarComponent,
    LocationSelectorComponent,
    LocationTreeComponent,
    MatIconModule,
    NetworkTypeNameComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class LocationSelectionPageComponent implements OnInit {
  protected readonly service = inject(LocationSelectionPageService);

  ngOnInit() {
    this.service.onInit();
  }

  selected(locationName: string): void {
    this.service.locationSelected(locationName);
  }
}
