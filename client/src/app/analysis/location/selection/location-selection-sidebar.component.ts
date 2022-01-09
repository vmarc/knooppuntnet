import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { AnalysisStrategy } from '../../../core/preferences/preferences.state';
import { actionLocationSelectionPageStrategy } from '../store/location.actions';

@Component({
  selector: 'kpn-location-selection-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy
        (strategyChange)="onStrategyChange($event)"
      ></kpn-analysis-strategy>

      <kpn-location-mode></kpn-location-mode>
    </kpn-sidebar>
  `,
})
export class LocationSelectionSidebarComponent {
  constructor(private store: Store<AppState>) {}

  onStrategyChange(strategy: AnalysisStrategy) {
    this.store.dispatch(actionLocationSelectionPageStrategy({ strategy }));
  }
}