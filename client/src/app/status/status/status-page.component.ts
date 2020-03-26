import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {AppService} from "../../app.service";
import {Status} from "../../kpn/api/common/status/status";
import {ApiResponse} from "../../kpn/api/custom/api-response";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-status-page",
  template: `
    <div *ngIf="response$ | async as response">
      <p>
        <span class="kpn-label" i18n="@@status.situation-on">The analysis is up-to-date until</span>
        <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
      </p>
      <p>
        <span class="kpn-label" i18n="@@status.replication">Replication details</span>
        <span class="kpn-comma-list">
          <a [routerLink]="hourLink">Hour</a>
          <a [routerLink]="dayLink">Day</a>
          <a [routerLink]="weekLink">Week</a>
          <a [routerLink]="monthLink">Month</a>
          <a [routerLink]="yearLink">Year</a>
        </span>
      </p>
      <p>
        <span class="kpn-label" i18n="@@status.system">System details</span>
        <span class="kpn-comma-list">
          <a [routerLink]="systemHourLink">Hour</a>
          <a [routerLink]="systemDayLink">Day</a>
          <a [routerLink]="systemWeekLink">Week</a>
          <a [routerLink]="systemMonthLink">Month</a>
          <a [routerLink]="systemYearLink">Year</a>
        </span>
      </p>
    </div>
  `
})
export class StatusPageComponent implements OnInit {

  response$: Observable<ApiResponse<Status>>;

  yearLink: string;
  weekLink: string;
  monthLink: string;
  dayLink: string;
  hourLink: string;

  systemYearLink: string;
  systemWeekLink: string;
  systemMonthLink: string;
  systemDayLink: string;
  systemHourLink: string;

  constructor(private readonly appService: AppService) {
  }

  ngOnInit(): void {
    this.response$ = this.appService.status().pipe(
      tap(response => {

        const ts = response.result.timestamp;

        this.yearLink = `/status/replication/year/${ts.weekYear}`;
        this.monthLink = `/status/replication/month/${ts.year}/${ts.month}`;
        this.weekLink = `/status/replication/week/${ts.weekYear}/${ts.weekWeek}`;
        this.dayLink = `/status/replication/day/${ts.year}/${ts.month}/${ts.day}`;
        this.hourLink = `/status/replication/hour/${ts.year}/${ts.month}/${ts.day}//${ts.hour}`;

        this.systemYearLink = `/status/system/year/${ts.weekYear}`;
        this.systemMonthLink = `/status/system/month/${ts.year}/${ts.month}`;
        this.systemWeekLink = `/status/system/week/${ts.weekYear}/${ts.weekWeek}`;
        this.systemDayLink = `/status/system/day/${ts.year}/${ts.month}/${ts.day}`;
        this.systemHourLink = `/status/system/hour/${ts.year}/${ts.month}/${ts.day}//${ts.hour}`;

      })
    );
  }

}
