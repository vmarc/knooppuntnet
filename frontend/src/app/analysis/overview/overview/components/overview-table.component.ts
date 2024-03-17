import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { StatisticValues } from '@api/common/statistics';
import { Stat } from '../../domain/stat';
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
        @for (stat of stats; track stat) {
          <kpn-overview-table-row [stat]="stat" />
        }
      </tbody>
    </table>
  `,
  standalone: true,
  imports: [OverviewTableHeaderComponent, OverviewTableRowComponent],
})
export class OverviewTableComponent implements OnInit {
  statistics = input.required<StatisticValues[]>();

  private readonly overviewService = inject(OverviewConfigurationService);

  protected stats: Stat[];

  ngOnInit(): void {
    this.stats = this.overviewService.statisticConfigurations.toArray().map((configuration) => {
      const statisticValues = this.statistics().find(
        (statisticValue) => statisticValue._id === configuration.id
      );
      return new Stat(statisticValues, configuration);
    });
  }
}
