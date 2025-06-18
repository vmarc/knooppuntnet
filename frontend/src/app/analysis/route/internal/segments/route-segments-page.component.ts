import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteSegment } from '@api/common/route/route-segment';
import { RouteSegmentComponent } from '@app/analysis/route/internal/segments/components/route-segment.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouteSegmentsPageService } from './route-segments-page.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';

@Component({
  selector: 'ui-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <div class="kpn-small-spacer-above kpn-small-spacer-below">
        <ui-route-page-header pageName="segments" />
      </div>
      <div>
        <button nz-button (click)="zoomToFitRoute()">
          <nz-icon nzType="fullscreen-exit" />
          <span>Zoom to fit entire route</span>
        </button>
      </div>
      @if (response()) {
        <ui-list>
          @for (segment of segments(); track segment.id) {
            <ui-list-item
              [clickable]="true"
              [selected]="selectedSegment()?.id == segment.id"
              (click)="onSelectionChange(segment)"
            >
              <ui-route-segment [segment]="segment" />
            </ui-list-item>
          }
        </ui-list>
      }
    </ui-page>
  `,
  providers: [RouteSegmentsPageService, RouterService],
  imports: [
    ListComponent,
    ListItemComponent,
    NzButtonComponent,
    NzIconDirective,
    PageComponent,
    RoutePageHeaderComponent,
    RouteSegmentComponent,
  ],
})
export class RouteSegmentsPageComponent implements OnInit {
  private readonly service = inject(RouteSegmentsPageService);
  protected readonly response = computed(() => this.service.response());
  protected readonly segments = computed(() => this.response().result.segments);
  protected readonly selectedSegment = computed(() => this.service.selectedSegment());

  ngOnInit(): void {
    this.service.onInit();
  }

  onSelectionChange(routeSegment: RouteSegment): void {
    this.service.selectSegment(routeSegment);
  }

  zoomToFitRoute(): void {
    this.service.selectSegment(undefined);
  }
}
