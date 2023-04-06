import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionPreferencesAnalysisStrategy } from '@app/core/preferences/preferences.actions';
import { AnalysisStrategy } from '@app/core/preferences/preferences.state';

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
  constructor(private store: Store) {}

  onStrategyChange(strategy: AnalysisStrategy) {
    this.store.dispatch(actionPreferencesAnalysisStrategy({ strategy }));
  }
}
