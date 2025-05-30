import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FilterOptions } from '@app/shared/kpn/filter/filter-options';
import { FilterCheckboxGroupComponent } from './filter-checkbox-group.component';
import { FilterRadioGroupComponent } from './filter-radio-group.component';
import { FilterTitleComponent } from './filter-title.component';

@Component({
  selector: 'ui-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (filterOptions().isEmpty()) {
      <p style="margin: 2em">No filter options</p>
    } @else {
      <div class="filter">
        <ui-filter-title [filterOptions]="filterOptions()" />
        @for (group of filterOptions().groups; track $index) {
          <div>
            @if (group.name === 'role') {
              <ui-filter-checkbox-group />
            } @else {
              <ui-filter-radio-group [group]="group" />
            }
          </div>
        }
      </div>
    }
  `,
  styleUrl: './filter.scss',
  imports: [FilterCheckboxGroupComponent, FilterRadioGroupComponent, FilterTitleComponent],
})
export class FilterComponent {
  filterOptions = input.required<FilterOptions>();
}
