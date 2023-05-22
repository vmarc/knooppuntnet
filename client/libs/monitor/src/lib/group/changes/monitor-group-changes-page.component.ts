import { NgIf } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';
import { MonitorGroupChangesPageService } from './monitor-group-changes-page.service';

@Component({
  selector: 'kpn-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <ng-container *ngIf="service.state() as state">
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </li>
        <li>Group changes</li>
      </ul>

      <h1>
        {{ state.groupDescription }}
      </h1>

      <kpn-monitor-group-page-menu
        pageName="changes"
        [groupName]="state.groupName"
      />

      <div *ngIf="state.response as response">
        <p *ngIf="!response.result">No group changes</p>
        <div *ngIf="response.result as page" class="kpn-spacer-above">
          <mat-slide-toggle
            [checked]="service.impact()"
            (change)="service.impactChanged($event.checked)"
          >
            Impact
          </mat-slide-toggle>

          <kpn-paginator
            (pageIndexChange)="pageChanged($event)"
            [pageIndex]="page.pageIndex"
            [length]="page.totalChangeCount"
            [showPageSizeSelection]="true"
          />

          <kpn-monitor-changes
            [pageSize]="page.pageSize"
            [pageIndex]="page.pageIndex"
            [changes]="page.changes"
          />
        </div>
      </div>
    </ng-container>
  `,
  standalone: true,
  imports: [
    MatSlideToggleModule,
    MonitorChangesComponent,
    MonitorGroupPageMenuComponent,
    NgIf,
    PaginatorComponent,
    RouterLink,
  ],
})
export class MonitorGroupChangesPageComponent implements OnInit {
  constructor(protected service: MonitorGroupChangesPageService) {}

  ngOnInit(): void {
    this.service.init();
  }

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.pageChanged(pageIndex);
  }
}
