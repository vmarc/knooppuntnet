import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { MonitorChangesComponent } from '../components/monitor-changes.component';
import { MonitorChangesPageService } from './monitor-changes-page.service';

@Component({
  selector: 'kpn-monitor-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.monitor.changes">Changes</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <h1 i18n="@@monitor.changes.title">Monitor</h1>

      <kpn-error />

      @if (service.changesState(); as state) {
        @if (state.response; as response) {
          @if (!response.result) {
            <p i18n="@@monitor.changes.no-changes">No group changes</p>
          }

          @if (response.result; as page) {
            <div class="kpn-spacer-above">
              <mat-slide-toggle
                [checked]="service.impact()"
                (change)="impactChanged($event)"
                i18n="@@monitor.changes.impact"
                >Impact
              </mat-slide-toggle>

              <kpn-paginator
                [pageSize]="service.pageSize()"
                (pageSizeChange)="pageSizeChanged($event)"
                [pageIndex]="page.pageIndex"
                (pageIndexChange)="pageIndexChanged($event)"
                [length]="page.totalChangeCount"
                [showPageSizeSelection]="true"
              />

              <kpn-monitor-changes
                [pageSize]="page.pageSize"
                [pageIndex]="page.pageIndex"
                [changes]="page.changes"
              />
            </div>
          }
        }
      }
    </kpn-page>
  `,
  providers: [MonitorChangesPageService],
  imports: [
    ErrorComponent,
    MatSlideToggleModule,
    MonitorChangesComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageComponent,
    PaginatorComponent,
    RouterLink,
  ],
})
export class MonitorChangesPageComponent {
  readonly service = inject(MonitorChangesPageService);

  impactChanged(event: MatSlideToggleChange) {
    this.service.updateImpact(event.checked);
  }

  pageSizeChanged(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  pageIndexChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePageIndex(pageIndex);
  }
}
