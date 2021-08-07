import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { StatisticValues } from '@api/common/statistics/statistic-values';
import { Stat } from '../domain/stat';
import { OverviewService } from './overview.service';

@Component({
  selector: 'kpn-overview-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table title="overview" class="kpn-table">
      <kpn-overview-table-header></kpn-overview-table-header>
      <tbody>
        <ng-container *ngFor="let stat of stats">
          <kpn-overview-table-row [stat]="stat"></kpn-overview-table-row>
        </ng-container>
      </tbody>
    </table>
  `,
})
export class OverviewTableComponent implements OnInit {
  @Input() statistics: StatisticValues[];

  stats: Stat[];

  constructor(private overviewService: OverviewService) {}

  ngOnInit(): void {
    this.stats = this.overviewService.statisticConfigurations
      .toArray()
      .map((configuration) => {
        const statisticValues = this.statistics.find(
          (statisticValue) => statisticValue._id === configuration.id
        );
        return new Stat(statisticValues, configuration);
      });
  }
}
