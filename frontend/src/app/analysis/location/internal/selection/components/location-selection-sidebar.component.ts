import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AnalysisStrategyComponent } from '@app/analysis/strategy/analysis-strategy.component';
import { AnalysisStrategy } from '@app/core';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { LocationService } from '../../location.service';
import { LocationModeComponent } from './location-mode.component';

@Component({
  selector: 'kpn-location-selection-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
    <nz-divider />
    <kpn-location-mode />
  `,
  imports: [AnalysisStrategyComponent, LocationModeComponent, NzDividerComponent],
})
export class LocationSelectionSidebarComponent {
  private readonly locationService = inject(LocationService);
  private readonly router = inject(Router);

  onStrategyChange(strategy: AnalysisStrategy) {
    if (strategy === 'network') {
      const key = this.locationService.key();
      const url = `/analysis/${key.routeType}/${key.country}/networks`;
      this.router.navigate([url]);
    }
  }
}
