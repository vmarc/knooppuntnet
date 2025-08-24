import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { SubsetMapComponent } from './components/subset-map.component';
import { SubsetMapPageService } from './subset-map-page.service';
import { SubsetMapService } from './subset-map.service';

@Component({
  selector: 'ui-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response()) {
      <ui-subset-map />
    }
  `,
  providers: [SubsetMapService, SubsetMapPageService, AnalysisStrategyService],
  imports: [SubsetMapComponent],
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
