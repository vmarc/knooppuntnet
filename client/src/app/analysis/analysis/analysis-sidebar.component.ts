import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { actionPreferencesAnalysisStrategy } from '@app/core/preferences';
import { AnalysisStrategy } from '@app/core/preferences';
import { Store } from '@ngrx/store';

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
