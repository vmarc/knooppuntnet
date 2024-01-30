import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter';
import { ChangeOption } from '@app/kpn/common';
import { ChangeFilterPeriodComponent } from './change-filter-period.component';

@Component({
  selector: 'kpn-change-filter',
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
            <kpn-change-filter-period
              [option]="option"
              (optionSelected)="optionSelected.emit($event)"
            />
          </div>
        }
      </div>
    }
  `,
  styleUrl: '../../filter/filter.scss',
  standalone: true,
  imports: [ChangeFilterPeriodComponent],
})
export class ChangeFilterComponent {
  filterOptions = input.required<ChangesFilterOption[]>();
  @Output() optionSelected = new EventEmitter<ChangeOption>();
}
