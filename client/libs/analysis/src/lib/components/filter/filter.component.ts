import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
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
    <div class="filter" *ngIf="!filterOptions.isEmpty()">
      <kpn-filter-title [filterOptions]="filterOptions" />
      <div *ngFor="let group of filterOptions.groups">
        <kpn-filter-checkbox-group *ngIf="group.name === 'role'" />
        <kpn-filter-radio-group *ngIf="group.name !== 'role'" [group]="group" />
      </div>
    </div>
  `,
  styleUrls: ['./filter.scss'],
  standalone: true,
  imports: [
    FilterCheckboxGroupComponent,
    FilterRadioGroupComponent,
    FilterTitleComponent,
    NgFor,
    NgIf,
  ],
})
export class FilterComponent {
  @Input() filterOptions: FilterOptions;
}
