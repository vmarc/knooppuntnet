import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { FilterOptions } from '../../../kpn/filter/filter-options';

@Component({
  selector: 'kpn-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="filter" *ngIf="!filterOptions.isEmpty()">
      <kpn-filter-title [filterOptions]="filterOptions" />
      <div *ngFor="let group of filterOptions.groups">
        <kpn-filter-checkbox-group *ngIf="group.name === 'role'" />
        <kpn-filter-radio-group *ngIf="group.name !== 'role'" [group]="group" />
      </div>
    </div>
  `,
  styleUrls: ['./filter.scss'],
})
export class FilterComponent {
  @Input() filterOptions: FilterOptions;
}
