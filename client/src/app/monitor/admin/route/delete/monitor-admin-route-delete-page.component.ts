import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {of} from 'rxjs';

@Component({
  selector: 'kpn-monitor-admin-route-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li><a [routerLink]="groupLink$ | async">{{groupDescription$ | async}}</a></li>
      <li>Route</li>
    </ul>

    <h1>
      {{routeName$ | async}}
    </h1>

    <kpn-page-menu>
      <span>
        Delete
      </span>
    </kpn-page-menu>

    <p>
      Remove this route from the monitor.
    </p>

    <p class="kpn-line">
      <mat-icon svgIcon="warning"></mat-icon>
      <span>All history will be lost!</span>
    </p>

    <div class="kpn-button-group">
      <button mat-stroked-button (click)="delete()">
        <span class="delete-button">
          Delete Route
        </span>
      </button>
      <a [routerLink]="groupLink$ | async">Cancel</a>
    </div>
  `,
  styles: [`
    .kpn-button-group {
      padding-top: 3em;
    }

    .delete-button {
      color: red;
    }
  `]
})
export class MonitorAdminRouteDeletePageComponent {

  groupDescription$ = of('Group One');
  groupLink$ = of('/monitor/groups/group-1');
  routeName$ = of('GR05 Vlaanderen');

  delete(): void {
  }

}
