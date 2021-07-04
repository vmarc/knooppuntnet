import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { FilterOptions } from '../../../kpn/filter/filter-options';

@Component({
  selector: 'kpn-filter-title',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="row">
      <div class="title" i18n="@@filter.title">Filter</div>
      <div class="total">
        {{ filterOptions.filteredCount }}/{{ filterOptions.totalCount }}
      </div>
    </div>
  `,
  styleUrls: ['./filter.scss'],
})
export class FilterTitleComponent {
  @Input() filterOptions: FilterOptions;
}
