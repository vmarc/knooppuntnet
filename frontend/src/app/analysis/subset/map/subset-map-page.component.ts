import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { ErrorComponent } from '@app/components/shared/error';
import { OldPageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetSidebarComponent } from '../subset-sidebar.component';
import { SubsetMapComponent } from './components/subset-map.component';
import { SubsetMapPageService } from './subset-map-page.service';
import { SubsetMapService } from './subset-map.service';

@Component({
  selector: 'kpn-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page [showFooter]="false">
      <kpn-subset-page-header-block
        pageName="map"
        pageTitle="Map"
        i18n-pageTitle="@@subset-map.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <kpn-subset-map />
      }
      <kpn-subset-sidebar sidebar />
    </kpn-old-page>
  `,
  providers: [SubsetMapService, SubsetMapPageService, AnalysisStrategyService, RouterService],
  imports: [
    ErrorComponent,
    OldPageComponent,
    SubsetMapComponent,
    SubsetPageHeaderBlockComponent,
    SubsetSidebarComponent,
  ],
})
export class SubsetMapPageComponent implements OnInit, OnDestroy {
  protected readonly service = inject(SubsetMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  ngOnDestroy(): void {
    this.service.onDestroy();
  }
}
