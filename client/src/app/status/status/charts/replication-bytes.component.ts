import {Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {BarChart} from "../../../kpn/api/common/status/bar-chart";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-replication-bytes",
  template: `
    <h2>
      Replication bytes
    </h2>
    <div class="chart">
      <kpn-action-bar-chart
        *ngIf="barChart$ | async as barChart"
        [barChart]="barChart"
        xAxisLabel="Today"
        yAxisLabel="Bytes downloaded">
      </kpn-action-bar-chart>
    </div>
  `
})
export class ReplicationBytesComponent implements OnInit {

  barChart$: Observable<BarChart>;

  constructor(private readonly appService: AppService) {
  }

  ngOnInit(): void {
    this.barChart$ = this.appService.dayReplicationBytes().pipe(map(r => r.result));
  }

}
