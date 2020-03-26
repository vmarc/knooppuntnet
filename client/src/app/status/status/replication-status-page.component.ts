import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {Params} from "@angular/router";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {flatMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {AppService} from "../../app.service";
import {PeriodParameters} from "../../kpn/api/common/status/period-parameters";
import {ReplicationStatusPage} from "../../kpn/api/common/status/replication-status-page";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-replication-status-page",
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/status" i18n="@@breadcrumb.status">Status</a></li>
      <li i18n="@@breadcrumb.replication">Replication</li>
    </ul>

    <h1>Replication</h1>

    <div *ngIf="page$ | async as page">
      <kpn-page-menu>
        <kpn-page-menu-option link="/status/replication/hour"> Hour</kpn-page-menu-option>
        <kpn-page-menu-option link="/status/replication/day"> Day</kpn-page-menu-option>
        <kpn-page-menu-option link="/status/replication/week"> Week</kpn-page-menu-option>
        <kpn-page-menu-option link="/status/replication/month"> Month</kpn-page-menu-option>
        <kpn-page-menu-option link="/status/replication/year"> Year</kpn-page-menu-option>
      </kpn-page-menu>

      <div>
        <a [routerLink]="'TODO previous'" class="previous">previous</a>
        <a [routerLink]="'TODO next'">next</a>
      </div>

      <div class="chart-group">
        <kpn-delay-chart [barChart]="page.delay" [xAxisLabel]="xAxisLabel"></kpn-delay-chart>
        <kpn-analysis-delay-chart [barChart]="page.analysisDelay" [xAxisLabel]="xAxisLabel"></kpn-analysis-delay-chart>
        <kpn-update-delay-chart [barChart]="page.updateDelay" [xAxisLabel]="xAxisLabel"></kpn-update-delay-chart>
        <kpn-replication-delay-chart [barChart]="page.replicationDelay" [xAxisLabel]="xAxisLabel"></kpn-replication-delay-chart>
      </div>

      <div class="chart-group">
        <kpn-replication-bytes-chart [barChart]="page.replicationBytes" [xAxisLabel]="xAxisLabel"></kpn-replication-bytes-chart>
        <kpn-replication-elements-chart [barChart]="page.replicationElements" [xAxisLabel]="xAxisLabel"></kpn-replication-elements-chart>
        <kpn-replication-changesets-chart [barChart]="page.replicationChangeSets" [xAxisLabel]="xAxisLabel"></kpn-replication-changesets-chart>
      </div>

    </div>
  `,
  styles: [`
    .chart-group {
      padding-bottom: 40px;
      margin-bottom: 40px;
      border-bottom: 1px solid lightgray;
    }

    .previous:after {
      content: " | ";
      padding-left: 5px;
      padding-right: 5px;
    }

  `]
})
export class ReplicationStatusPageComponent implements OnInit {

  page$: Observable<ReplicationStatusPage>;

  xAxisLabel: string;

  constructor(private readonly activatedRoute: ActivatedRoute,
              private readonly appService: AppService) {
  }

  ngOnInit(): void {
    this.page$ = this.activatedRoute.params.pipe(
      map(params => this.toPeriodParameters(params)),
      tap(parameters => {
        if (parameters.period === "year") {
          this.xAxisLabel = "weeks";
        } else if (parameters.period === "month") {
          this.xAxisLabel = "days";
        } else if (parameters.period === "week") {
          this.xAxisLabel = "days";
        } else if (parameters.period === "day") {
          this.xAxisLabel = "hours";
        } else if (parameters.period === "hour") {
          this.xAxisLabel = "minutes";
        }
      }),
      flatMap(parameters => this.appService.replicationStatus(parameters).pipe(map(r => r.result)))
    );
  }

  private toPeriodParameters(params: Params): PeriodParameters {

    const period = params["period"];
    if ("year" === period) {
      return new PeriodParameters("year", +params["year"], null, null, null, null);
    }
    if ("month" === period) {
      return new PeriodParameters("month", +params["year"], +params["monthOrWeek"], null, null, null);
    }
    if ("week" === period) {
      return new PeriodParameters("week", +params["year"], null, +params["monthOrWeek"], null, null);
    }
    if ("day" === period) {
      return new PeriodParameters("day", +params["year"], +params["month"], null, +params["day"], null);
    }
    if ("hour" === period) {
      return new PeriodParameters("hour", +params["year"], +params["month"], null, +params["day"], +params["hour"]);
    }

    const now = new Date();
    return new PeriodParameters(
      "hour",
      now.getFullYear(),
      now.getMonth(),
      null,
      now.getDate(),
      now.getHours()
    );
  }

}
