import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetMapComponent } from './components/subset-map.component';
import { SubsetMapPageService } from './subset-map-page.service';
import { SubsetMapService } from './subset-map.service';

@Component({
  selector: 'ui-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-subset-page-header-block
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@subset-map.title"
    />

    <ui-error />

    @if (service.response(); as response) {
      <ui-subset-map />
    }
  `,
  providers: [SubsetMapService, SubsetMapPageService, AnalysisStrategyService],
  imports: [ErrorComponent, SubsetMapComponent, SubsetPageHeaderBlockComponent],
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
