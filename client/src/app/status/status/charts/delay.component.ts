import {Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {BarChart2D} from "../../../kpn/api/common/status/bar-chart2d";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-delay",
  template: `
    <h2>
      Average delay
    </h2>
    <div class="chart">
      <kpn-action-bar-chart-stacked
        *ngIf="barChart$ | async as barChart"
        [barChart]="barChart"
        xAxisLabel="Today"
        yAxisLabel="Average delay">
      </kpn-action-bar-chart-stacked>
    </div>
  `,
  styles: []
})
export class DelayComponent implements OnInit {

  barChart$: Observable<BarChart2D>;

  constructor(private readonly appService: AppService) {
  }

  ngOnInit(): void {
    this.barChart$ = this.appService.dayDelay().pipe(map(r => r.result));
  }

}

