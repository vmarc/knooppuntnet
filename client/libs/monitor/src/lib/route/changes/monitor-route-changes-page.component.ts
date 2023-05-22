import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteChangesPageService } from './monitor-route-changes-page.service';

@Component({
  selector: 'kpn-monitor-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <ng-container *ngIf="service.state() as state">
      <kpn-monitor-route-page-header
        pageName="changes"
        [groupName]="state.groupName"
        [routeName]="state.routeName"
        [routeDescription]="state.routeDescription"
      />

      <div *ngIf="state.response as response" class="kpn-spacer-above">
        <div *ngIf="!response.result">Route not found</div>
        <div *ngIf="response.result as page" class="kpn-spacer-above">
          <mat-slide-toggle
            [checked]="service.impact()"
            (change)="impactChanged($event)"
            >Impact
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
    MonitorRoutePageHeaderComponent,
    NgIf,
    PaginatorComponent,
  ],
})
export class MonitorRouteChangesPageComponent {
  constructor(protected service: MonitorRouteChangesPageService) {}

  impactChanged(event: MatSlideToggleChange) {
    this.service.impactChanged(event.checked);
  }

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.pageChanged(pageIndex);
  }
}
