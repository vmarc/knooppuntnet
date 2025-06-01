import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { OldPaginatorComponent } from '@app/shared/components/paginator/old-paginator.component';
import { MonitorChangesComponent } from '../components/monitor-changes.component';
import { MonitorChangesPageService } from './monitor-changes-page.service';

@Component({
  selector: 'ui-monitor-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <h1 i18n="@@monitor.changes.title">Monitor</h1>
      <ui-error />

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

              <ui-old-paginator
                [pageSize]="service.pageSize()"
                (pageSizeChange)="pageSizeChanged($event)"
                [pageIndex]="page.pageIndex"
                (pageIndexChange)="pageIndexChanged($event)"
                [length]="page.totalChangeCount"
                [showPageSizeSelection]="true"
              />

              <ui-monitor-changes
                [pageSize]="page.pageSize"
                [pageIndex]="page.pageIndex"
                [changes]="page.changes"
              />
            </div>
          }
        }
      }
    </ui-page>
  `,
  providers: [MonitorChangesPageService],
  imports: [
    BreadcrumbComponent,
    ErrorComponent,
    MatSlideToggleModule,
    MonitorChangesComponent,
    OldPaginatorComponent,
    PageComponent,
  ],
})
export class MonitorChangesPageComponent {
  protected readonly service = inject(MonitorChangesPageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.monitor,
    { label: Breadcrumbs.changesLabel },
  ];

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
