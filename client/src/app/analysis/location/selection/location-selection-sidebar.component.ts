import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategy } from '@app/core';
import { Store } from '@ngrx/store';
import { actionLocationSelectionPageStrategy } from '../store/location.actions';

@Component({
  selector: 'kpn-location-selection-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
      <kpn-location-mode />
    </kpn-sidebar>
  `,
})
export class LocationSelectionSidebarComponent {
  constructor(private store: Store) {}

  onStrategyChange(strategy: AnalysisStrategy) {
    this.store.dispatch(actionLocationSelectionPageStrategy({ strategy }));
  }
}
