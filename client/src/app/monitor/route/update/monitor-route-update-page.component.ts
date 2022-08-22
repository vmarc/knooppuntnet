import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UntypedFormGroup } from '@angular/forms';
import { of } from 'rxjs';

@Component({
  selector: 'kpn-monitor-route-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>
        <a routerLink="groupLink$ | async">{{ groupDescription$ | async }}</a>
      </li>
      <li>Route</li>
    </ul>

    <h1>
      {{ routeName$ | async }}
    </h1>

    <kpn-page-menu>
      <span> Update </span>
    </kpn-page-menu>

    <form [formGroup]="form">
      <kpn-monitor-route-reference></kpn-monitor-route-reference>

      <div class="kpn-button-group">
        <button mat-stroked-button (click)="save()">Save Route</button>
        <a [routerLink]="groupLink$ | async">Cancel</a>
      </div>
    </form>
  `,
  styles: [
    `
      .kpn-button-group {
        padding-top: 3em;
      }
    `,
  ],
})
export class MonitorRouteUpdatePageComponent {
  readonly groupDescription$ = of('Group One');
  readonly groupLink$ = of('/monitor/groups/group-1');
  readonly routeName$ = of('GR05 Vlaanderen');

  readonly form = new UntypedFormGroup({
    // routeId: this.routeId
  });

  save(): void {}
}
