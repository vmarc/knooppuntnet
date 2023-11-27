import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter';
import { ChangeOption } from '@app/kpn/common';
import { ChangeFilterPeriodComponent } from './change-filter-period.component';

@Component({
  selector: 'kpn-change-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="filterOptions && filterOptions.length > 0" class="filter">
      <div class="title" i18n="@@change-filter.title">Filter</div>
      <div class="row">
        <div class="count-links" i18n="@@change-filter.legend">impacted / all</div>
      </div>
      <div *ngFor="let option of filterOptions">
        <kpn-change-filter-period
          [option]="option"
          (optionSelected)="optionSelected.emit($event)"
        />
      </div>
    </div>
  `,
  styleUrl: '../../filter/filter.scss',
  standalone: true,
  imports: [NgIf, NgFor, ChangeFilterPeriodComponent],
})
export class ChangeFilterComponent {
  @Input() filterOptions: ChangesFilterOption[];
  @Output() optionSelected = new EventEmitter<ChangeOption>();
}
