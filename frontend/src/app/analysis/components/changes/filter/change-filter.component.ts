import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { ChangeFilterPeriodComponent } from './change-filter-period.component';

@Component({
  selector: 'ui-change-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (filterOptions() && filterOptions().length > 0) {
      <div class="filter">
        <div class="title" i18n="@@change-filter.title">Filter</div>
        <div class="row">
          <div class="count-links" i18n="@@change-filter.legend">impacted / all</div>
        </div>
        @for (option of filterOptions(); track $index) {
          <div>
            <ui-change-filter-period
              [option]="option"
              (optionSelected)="optionSelected.emit($event)"
            />
          </div>
        }
      </div>
    }
  `,
  styleUrl: '../../filter/filter.scss',
  imports: [ChangeFilterPeriodComponent],
})
export class ChangeFilterComponent {
  readonly filterOptions = input.required<ReadonlyArray<ChangesFilterOption>>();
  readonly optionSelected = output<ChangeOption>();
}
