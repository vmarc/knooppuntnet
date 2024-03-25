import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { AnalysisStrategy } from '@app/core';
import { LocationService } from '../../location.service';
import { LocationModeComponent } from './location-mode.component';

@Component({
  selector: 'kpn-location-selection-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
      <kpn-location-mode />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, AnalysisStrategyComponent, LocationModeComponent],
})
export class LocationSelectionSidebarComponent {
  private readonly locationService = inject(LocationService);
  private readonly router = inject(Router);

  onStrategyChange(strategy: AnalysisStrategy) {
    const key = this.locationService.key();
    const url = `/analysis/${key.networkType}/${key.country}/networks`;
    this.router.navigate([url]);
  }
}
