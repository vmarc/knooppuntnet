import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { SwitchComponent } from '@app/shared/components/switch/switch.component';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteChangesPageService } from './monitor-route-changes-page.service';

@Component({
  selector: 'ui-monitor-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    @if (service.changesState(); as state) {
      <ui-page>
        <ui-monitor-route-page-header />

        @if (state.response; as response) {
          <div class="kpn-spacer-above">
            @if (!response.result) {
              <div>Route not found</div>
            }

            @if (response.result; as page) {
              <div class="kpn-spacer-above">
                <ui-switch
                  i18n-label="@@monitor.changes.impact"
                  label="Impact"
                  [value]="service.impact()"
                  (valueChange)="impactChanged($event)"
                />

                <ui-paginator
                  (pageIndexChange)="pageChanged($event)"
                  [pageIndex]="page.pageIndex"
                  [pageSize]="page.pageSize"
                  [length]="page.totalChangeCount"
                />

                <ui-monitor-changes
                  [pageSize]="page.pageSize"
                  [pageIndex]="page.pageIndex"
                  [changes]="page.changes"
                />
              </div>
            }
          </div>
        }
      </ui-page>
    }
  `,
  providers: [MonitorRouteChangesPageService, NavService],
  imports: [
    MonitorChangesComponent,
    MonitorRoutePageHeaderComponent,
    PageComponent,
    PaginatorComponent,
    SwitchComponent,
  ],
})
export class MonitorRouteChangesPageComponent {
  readonly service = inject(MonitorRouteChangesPageService);

  impactChanged(impact: boolean) {
    this.service.updateImpact(impact);
  }

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePage(pageIndex);
  }
}
