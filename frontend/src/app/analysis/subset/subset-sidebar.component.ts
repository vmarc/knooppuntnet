import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { AnalysisStrategy } from '@app/core';
import { SubsetService } from './subset.service';

@Component({
  selector: 'kpn-subset-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-sidebar>
    <kpn-analysis-strategy (strategyChange)="strategyChanged($event)" />
  </kpn-sidebar>`,
  imports: [SidebarComponent, AnalysisStrategyComponent],
})
export class SubsetSidebarComponent {
  private readonly router = inject(Router);
  private readonly subsetService = inject(SubsetService);

  strategyChanged(strategy: AnalysisStrategy) {
    if (strategy === AnalysisStrategy.location) {
      const subset = this.subsetService.subset();
      const url = `/analysis/${subset.networkType}/${subset.country}`;
      this.router.navigate([url]);
    }
  }
}
