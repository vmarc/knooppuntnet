import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { AnalysisStrategy } from '@app/core';
import { ChangeOption } from '@app/kpn/common';
import { ChangesPageService } from '../changes-page.service';

@Component({
  selector: 'kpn-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
      <kpn-change-filter
        [filterOptions]="service.filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [AnalysisStrategyComponent, ChangeFilterComponent, SidebarComponent],
})
export class ChangesSidebarComponent {
  protected readonly service = inject(ChangesPageService);

  onOptionSelected(option: ChangeOption): void {
    this.service.setFilterOption(option);
  }

  onStrategyChange(strategy: AnalysisStrategy): void {
    this.service.setStrategy(strategy);
  }
}
