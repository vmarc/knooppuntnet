import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../app.service";
import {BarChart2D} from "../../kpn/api/common/status/bar-chart2d";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-status-page",
  template: `

    <div class="chart-group">
      <kpn-delay></kpn-delay>
      <kpn-analysis-delay></kpn-analysis-delay>
      <kpn-update-delay></kpn-update-delay>
      <kpn-replication-delay></kpn-replication-delay>
    </div>

    <div class="chart-group">
      <kpn-replication-bytes></kpn-replication-bytes>
      <kpn-replication-elements></kpn-replication-elements>
      <kpn-replication-changesets></kpn-replication-changesets>
    </div>

    <h2>
      Example 2D
    </h2>
    <div class="chart">
      <kpn-chart-example
        *ngIf="example$ | async as barChart"
        [barChart]="barChart">
      </kpn-chart-example>
    </div>
  `,
  styles: [`
    .chart-group {
      padding-bottom: 40px;
      margin-bottom: 40px;
      border-bottom: 1px solid lightgray;
    }
  `]
})
export class StatusPageComponent implements OnInit {

  example$: Observable<BarChart2D>;

  constructor(private readonly appService: AppService) {
  }

  ngOnInit(): void {
    this.example$ = this.appService.statusExample().pipe(map(r => r.result));
  }

}
