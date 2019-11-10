import {Component, Input, OnInit} from "@angular/core";
import {List} from "immutable";
import {OverviewService} from "./overview.service";
import {Stat} from "./stat";
import {Statistics} from "../../kpn/api/custom/statistics";

@Component({
  selector: "kpn-overview-list",
  template: `
    <div *ngFor="let stat of stats">
      <kpn-overview-list-stat [stat]="stat"></kpn-overview-list-stat>
    </div>
  `
})
export class OverviewListComponent implements OnInit {

  @Input() statistics: Statistics;

  stats: List<Stat>;

  constructor(private overviewService: OverviewService) {
  }

  ngOnInit(): void {
    this.stats = this.overviewService.statisticConfigurations.map(configuration => {
      const figures = this.statistics.get(configuration.id);
      if (figures !== null) {
        return new Stat(figures, configuration);
      }
      console.log(`DEBUG OverviewListComponent figures not found: ${configuration.id}`);
      return null;
    }).filter(stat => stat !== null);
  }
}
