import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { actionPreferencesAnalysisStrategy } from '@app/core';
import { AnalysisStrategy } from '@app/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-analysis-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, AnalysisStrategyComponent],
})
export class AnalysisSidebarComponent {
  constructor(private store: Store) {}

  onStrategyChange(strategy: AnalysisStrategy) {
    this.store.dispatch(actionPreferencesAnalysisStrategy({ strategy }));
  }
}
