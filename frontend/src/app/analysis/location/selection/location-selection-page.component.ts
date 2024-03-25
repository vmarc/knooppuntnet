import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { LocationSelectionPageBreadcrumbComponent } from './components/location-selection-page-breadcrumb.component';
import { LocationSelectionPageHeaderComponent } from './components/location-selection-page-header.component';
import { LocationSelectionSidebarComponent } from './components/location-selection-sidebar.component';
import { LocationSelectorComponent } from './components/location-selector.component';
import { LocationTreeComponent } from './components/location-tree.component';
import { LocationSelectionPageService } from './location-selection-page.service';

@Component({
  selector: 'kpn-location-selection-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-selection-page-breadcrumb [key]="service.key()" />
      <kpn-location-selection-page-header [key]="service.key()" />
      <kpn-error />

      @if (service.locationNode(); as locationNode) {
        @if (service.isModeName()) {
          <kpn-location-selector
            [country]="service.country()"
            [locationNode]="locationNode"
            (selection)="selected($event)"
          />
        }
        @if (service.isModeTree()) {
          <kpn-location-tree
            [networkType]="service.networkType()"
            [country]="service.country()"
            [locationNode]="locationNode"
            (selection)="selected($event)"
          />
        }
      }
      <kpn-location-selection-sidebar sidebar />
    </kpn-page>
  `,
  providers: [LocationSelectionPageService, RouterService],
  standalone: true,
  imports: [
    ErrorComponent,
    LocationSelectionSidebarComponent,
    LocationSelectorComponent,
    LocationTreeComponent,
    PageComponent,
    LocationSelectionPageBreadcrumbComponent,
    LocationSelectionPageHeaderComponent,
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
