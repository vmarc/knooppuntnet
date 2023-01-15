import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/core.state';
import { actionPreferencesAnalysisStrategy } from '../../core/preferences/preferences.actions';
import { AnalysisStrategy } from '../../core/preferences/preferences.state';

@Component({
  selector: 'kpn-analysis-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
    </kpn-sidebar>
  `,
})
export class AnalysisSidebarComponent {
  constructor(private store: Store<AppState>) {}

  onStrategyChange(strategy: AnalysisStrategy) {
    this.store.dispatch(actionPreferencesAnalysisStrategy({ strategy }));
  }
}
