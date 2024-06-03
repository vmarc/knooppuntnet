import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';

@Component({
  selector: 'kpn-analysis-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [AnalysisStrategyComponent, SidebarComponent],
})
export class AnalysisSidebarComponent {}
