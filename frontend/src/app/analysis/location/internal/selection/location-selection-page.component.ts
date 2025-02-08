import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { RouterService } from '../../../../shared/services/router.service';
import { LocationSelectorComponent } from '../../location-selector.component';
import { LocationSelectionPageBreadcrumbComponent } from './components/location-selection-page-breadcrumb.component';
import { LocationSelectionPageHeaderComponent } from './components/location-selection-page-header.component';
import { LocationSelectionSidebarComponent } from './components/location-selection-sidebar.component';
import { LocationTreeComponent } from './components/location-tree.component';
import { LocationSelectionPageService } from './location-selection-page.service';

@Component({
  selector: 'kpn-location-selection-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-selection-sidebar />
      <nz-divider />
      <kpn-location-selection-page-breadcrumb [key]="service.key()" />
      <kpn-location-selection-page-header [key]="service.key()" />
      <kpn-error />

      @if (service.locationNode(); as locationNode) {
        @if (service.isModeName()) {
          <kpn-location-selector
            [country]="service.country()"
            [locationNode]="locationNode"
            [all]="true"
            (selection)="selected($event)"
          />
        }
        @if (service.isModeTree()) {
          <kpn-location-tree
            [routeType]="service.routeType()"
            [country]="service.country()"
            [locationNode]="locationNode"
            (selection)="selected($event)"
          />
        }
      }
    </kpn-page>
  `,
  providers: [LocationSelectionPageService, AnalysisStrategyService, RouterService],
  imports: [
    ErrorComponent,
    LocationSelectionPageBreadcrumbComponent,
    LocationSelectionPageHeaderComponent,
    LocationSelectionSidebarComponent,
    LocationSelectorComponent,
    LocationTreeComponent,
    NzDividerComponent,
    PageComponent,
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
