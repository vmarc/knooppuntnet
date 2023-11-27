import { NgFor } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { StatisticValues } from '@api/common/statistics';
import { Stat } from '../domain/stat';
import { OverviewConfigurationService } from './overview-configuration.service';
import { OverviewTableHeaderComponent } from './overview-table-header.component';
import { OverviewTableRowComponent } from './overview-table-row.component';

@Component({
  selector: 'kpn-overview-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table title="overview" class="kpn-table">
      <kpn-overview-table-header />
      <tbody>
        <ng-container *ngFor="let stat of stats">
          <kpn-overview-table-row [stat]="stat" />
        </ng-container>
      </tbody>
    </table>
  `,
  standalone: true,
  imports: [OverviewTableHeaderComponent, NgFor, OverviewTableRowComponent],
})
export class OverviewTableComponent implements OnInit {
  @Input() statistics: StatisticValues[];

  stats: Stat[];

  constructor(private overviewService: OverviewConfigurationService) {}

  ngOnInit(): void {
    this.stats = this.overviewService.statisticConfigurations.toArray().map((configuration) => {
      const statisticValues = this.statistics.find(
        (statisticValue) => statisticValue._id === configuration.id
      );
      return new Stat(statisticValues, configuration);
    });
  }
}
