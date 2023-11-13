import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorRouteDeletePageService } from './monitor-route-delete-page.service';

@Component({
  selector: 'kpn-monitor-route-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page *ngIf="service.state() as state">
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </li>
        <li>
          <a [routerLink]="state.groupLink">{{ state.groupName }}</a>
        </li>
        <li i18n="@@breadcrumb.monitor.route">Route</li>
      </ul>

      <h1>
        <span class="kpn-label">{{ state.routeName }}</span>
        <span>{{ state.routeDescription }}</span>
      </h1>

      <h2 i18n="@@monitor.route.delete.title">Delete</h2>

      <kpn-error />

      <div class="kpn-form">
        <p i18n="@@monitor.route.delete.comment">
          Remove this route from the monitor.
        </p>

        <p class="kpn-line">
          <mat-icon svgIcon="warning" />
          <span i18n="@@monitor.route.delete.warning"
            >Attention: all history will be lost!</span
          >
        </p>

        <div class="kpn-form-buttons">
          <button mat-stroked-button (click)="service.delete()">
            <span class="kpn-warning" i18n="@@monitor.route.delete.action"
              >Delete Route</span
            >
          </button>
          <a [routerLink]="state.groupLink" i18n="@@action.cancel">Cancel</a>
        </div>
      </div>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [MonitorRouteDeletePageService, NavService],
  standalone: true,
  imports: [
    ErrorComponent,
    MatButtonModule,
    MatIconModule,
    NgIf,
    PageComponent,
    RouterLink,
    SidebarComponent,
  ],
})
export class MonitorRouteDeletePageComponent {
  constructor(protected service: MonitorRouteDeletePageService) {}
}
