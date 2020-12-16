import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {Statistics} from '@api/custom/statistics';
import {List} from 'immutable';
import {Stat} from '../domain/stat';
import {OverviewService} from './overview.service';

@Component({
  selector: 'kpn-overview-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table title="overview" class="kpn-table">
      <kpn-overview-table-header></kpn-overview-table-header>
      <tbody>
      <ng-container *ngFor="let stat of stats()">
        <kpn-overview-table-row [stat]="stat"></kpn-overview-table-row>
      </ng-container>
      </tbody>
    </table>
  `
})
export class OverviewTableComponent {

  @Input() statistics: Statistics;

  constructor(private overviewService: OverviewService) {
  }

  stats(): List<Stat> {
    return this.overviewService.statisticConfigurations.map(configuration => {
      const figures = this.statistics.get(configuration.id);
      return new Stat(figures, configuration);
    });
  }
}
