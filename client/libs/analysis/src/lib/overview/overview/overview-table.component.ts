import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { StatisticValues } from '@api/common/statistics';
import { Stat } from '../domain/stat';
import { OverviewConfigurationService } from './overview-configuration.service';

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
})
export class OverviewTableComponent implements OnInit {
  @Input() statistics: StatisticValues[];

  stats: Stat[];

  constructor(private overviewService: OverviewConfigurationService) {}

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
