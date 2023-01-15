import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { StatisticValues } from '@api/common/statistics/statistic-values';
import { Stat } from '../domain/stat';
import { OverviewConfigurationService } from './overview-configuration.service';

@Component({
  selector: 'kpn-overview-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let stat of stats">
      <kpn-overview-list-stat [stat]="stat"/>
    </div>
  `,
})
export class OverviewListComponent implements OnInit {
  @Input() statistics: StatisticValues[];

  stats: Stat[];

  constructor(
    private overviewConfigurationService: OverviewConfigurationService
  ) {}

  ngOnInit(): void {
    this.stats = this.overviewConfigurationService.statisticConfigurations
      .toArray()
      .flatMap((configuration) => {
        return this.statistics
          .filter((statisticValue) => {
            return statisticValue._id === configuration.id;
          })
          .map((statisticValues) => new Stat(statisticValues, configuration));
      });
  }
}
