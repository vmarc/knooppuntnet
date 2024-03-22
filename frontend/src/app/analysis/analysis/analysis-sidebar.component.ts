import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { PreferencesService } from '@app/core';
import { AnalysisStrategy } from '@app/core';

@Component({
  selector: 'kpn-analysis-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [AnalysisStrategyComponent, SidebarComponent],
})
export class AnalysisSidebarComponent {
  private readonly preferencesService = inject(PreferencesService);

  onStrategyChange(strategy: AnalysisStrategy) {
    this.preferencesService.setStrategy(strategy);
  }
}
