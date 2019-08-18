import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Statistics} from "../../kpn/shared/statistics/statistics";
import {OverviewService} from "./overview.service";
import {Stat} from "./stat";

@Component({
  selector: "kpn-overview-table",
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
