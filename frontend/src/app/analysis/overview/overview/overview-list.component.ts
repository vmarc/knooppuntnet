import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
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
  statistics = input<StatisticValues[] | undefined>();

  private readonly overviewConfigurationService = inject(OverviewConfigurationService);

  protected stats: Stat[];

  ngOnInit(): void {
    this.stats = this.overviewConfigurationService.statisticConfigurations
      .toArray()
      .flatMap((configuration) => {
        return this.statistics()
          .filter((statisticValue) => {
            return statisticValue._id === configuration.id;
          })
          .map((statisticValues) => new Stat(statisticValues, configuration));
      });
  }
}
