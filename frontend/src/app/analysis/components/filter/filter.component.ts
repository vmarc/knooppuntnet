import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { FilterOptions } from '@app/kpn/filter';
import { FilterCheckboxGroupComponent } from './filter-checkbox-group.component';
import { FilterRadioGroupComponent } from './filter-radio-group.component';
import { FilterTitleComponent } from './filter-title.component';

@Component({
  selector: 'kpn-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!filterOptions.isEmpty()) {
      <div class="filter">
        <kpn-filter-title [filterOptions]="filterOptions" />
        @for (group of filterOptions.groups) {
          <div>
            @if (group.name === 'role') {
              <kpn-filter-checkbox-group />
            } @else {
              <kpn-filter-radio-group [group]="group" />
            }
          </div>
        }
      </div>
    }
  `,
  styleUrl: './filter.scss',
  standalone: true,
  imports: [FilterCheckboxGroupComponent, FilterRadioGroupComponent, FilterTitleComponent],
})
export class FilterComponent {
  @Input() filterOptions: FilterOptions;
}
