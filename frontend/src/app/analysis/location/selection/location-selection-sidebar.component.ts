import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { AnalysisStrategy } from '@app/core';
import { Store } from '@ngrx/store';
import { actionLocationSelectionPageStrategy } from '../store/location.actions';
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
  private readonly store = inject(Store);

  onStrategyChange(strategy: AnalysisStrategy) {
    this.store.dispatch(actionLocationSelectionPageStrategy({ strategy }));
  }
}
