import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { SwitchComponent } from '@app/shared/components/switch/switch.component';
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
          <!-- eslint-disable-next-line @angular-eslint/template/prefer-at-else -->
          @if (response.result; as page) {
            <div class="kpn-spacer-above">
              <ui-switch
                i18n-label="@@monitor.changes.impact"
                label="Impact"
                [value]="service.impact()"
                (valueChange)="impactChanged($event)"
              />

              <ui-paginator
                [pageSize]="service.pageSize()"
                (pageSizeChange)="pageSizeChanged($event)"
                [pageIndex]="page.pageIndex"
                (pageIndexChange)="pageIndexChanged($event)"
                [length]="page.totalChangeCount"
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
    MonitorChangesComponent,
    PageComponent,
    PaginatorComponent,
    SwitchComponent,
  ],
})
export class MonitorChangesPageComponent {
  protected readonly service = inject(MonitorChangesPageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.monitor,
    { label: Breadcrumbs.changesLabel },
  ];

  impactChanged(value: boolean) {
    this.service.updateImpact(value);
  }

  pageSizeChanged(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  pageIndexChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePageIndex(pageIndex);
  }
}
