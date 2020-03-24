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
        <a [routerLink]="hourLink">hour</a>
        <a [routerLink]="dayLink">day</a>
        <a [routerLink]="weekLink">week</a>
        <a [routerLink]="monthLink">month</a>
        <a [routerLink]="yearLink">year</a>
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
      })
    );
  }

}
