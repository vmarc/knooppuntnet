import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { StatisticValues } from '@api/common/statistics';
import { Stat } from '../domain/stat';
import { OverviewConfigurationService } from './overview-configuration.service';
import { OverviewListStatComponent } from './overview-list-stat.component';

@Component({
  selector: 'kpn-overview-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (stat of stats; track stat) {
      <kpn-overview-list-stat [stat]="stat" />
    }
  `,
  standalone: true,
  imports: [OverviewListStatComponent],
})
export class OverviewListComponent implements OnInit {
  @Input() statistics: StatisticValues[];

  stats: Stat[];

  constructor(private overviewConfigurationService: OverviewConfigurationService) {}

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
