import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { AnalysisStrategyComponent } from '@app/analysis/strategy/analysis-strategy.component';
import { ChangeOption } from '@app/kpn/common';
import { ChangesPageService } from '../changes-page.service';

@Component({
  selector: 'kpn-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-analysis-strategy (strategyChange)="onStrategyChange()" />
    <kpn-change-filter
      [filterOptions]="service.filterOptions()"
      (optionSelected)="onOptionSelected($event)"
    />
  `,
  imports: [AnalysisStrategyComponent, ChangeFilterComponent],
})
export class ChangesSidebarComponent {
  protected readonly service = inject(ChangesPageService);

  onOptionSelected(option: ChangeOption): void {
    this.service.updateFilterOption(option);
  }

  onStrategyChange(): void {
    this.service.strategyUpdated();
  }
}
