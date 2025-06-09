import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FilterOptions } from '@app/shared/kpn/filter/filter-options';

@Component({
  selector: 'ui-filter-title',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="row">
      <div class="title" i18n="@@filter.title">Filter</div>
      <div class="total">{{ filterOptions().filteredCount }}/{{ filterOptions().totalCount }}</div>
    </div>
  `,
  styleUrl: './filter.scss',
})
export class FilterTitleComponent {
  readonly filterOptions = input.required<FilterOptions>();
}
