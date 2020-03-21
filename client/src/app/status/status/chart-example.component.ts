import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {AppService} from "../../app.service";
import {BarChart2D} from "../../kpn/api/common/status/bar-chart2d";
import {ApiResponse} from "../../kpn/api/custom/api-response";

@Component({
  selector: "kpn-chart-example",
  template: `
    <div *ngIf="response$ | async as response">
      <ngx-charts-bar-vertical-2d
        [view]="view"
        [results]="response.result.data"
        [xAxis]="true"
        [yAxis]="true"
        [showXAxisLabel]="true"
        [showYAxisLabel]="true"
        [xAxisLabel]="response.result.xAxisLabel"
        [yAxisLabel]="response.result.yAxisLabel"
        [legend]="true"
        [legendTitle]="response.result.legendTitle"
        [roundDomains]="false"
        [roundEdges]="false"
        [showDataLabel]="false"
        (select)="onSelect($event)">
      </ngx-charts-bar-vertical-2d>
    </div>
  `
})
export class ChartExampleComponent implements OnInit {

  view: [number, number] = [700, 300];
  response$: Observable<ApiResponse<BarChart2D>>;

  constructor(private readonly appService: AppService) {
  }

  ngOnInit(): void {
    this.response$ = this.appService.statusExample();
  }

  onSelect(event) {
    console.log(event);
  }
}
