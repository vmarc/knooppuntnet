import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { Statistics } from '@api/custom/statistics';
import { List } from 'immutable';
import { Stat } from '../domain/stat';
import { OverviewService } from './overview.service';

@Component({
  selector: 'kpn-overview-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let stat of stats">
      <kpn-overview-list-stat [stat]="stat"></kpn-overview-list-stat>
    </div>
  `,
})
export class OverviewListComponent implements OnInit {
  @Input() statistics: Statistics;

  stats: List<Stat>;

  constructor(private overviewService: OverviewService) {}

  ngOnInit(): void {
    this.stats = this.overviewService.statisticConfigurations
      .map((configuration) => {
        const figures = this.statistics.get(configuration.id);
        if (figures !== null) {
          return new Stat(figures, configuration);
        }
        console.log(
          `DEBUG OverviewListComponent figures not found: ${configuration.id}`
        );
        return null;
      })
      .filter((stat) => stat !== null);
  }
}
