import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouteSegmentListComponent } from '@app/shared/components/route/segments/route-segment-list.component';
import { RouteSegmentsPageService } from './route-segments-page.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';

@Component({
  selector: 'ui-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="segments" />
      <ui-route-segment-list [segments]="segments()" [selectedSegment]="undefined" />
    </ui-page>
  `,
  providers: [RouteSegmentsPageService, RouterService],
  imports: [PageComponent, RoutePageHeaderComponent, RouteSegmentListComponent],
})
export class RouteSegmentsPageComponent implements OnInit {
  private readonly service = inject(RouteSegmentsPageService);
  protected readonly segments = () => this.service.response().result.segments;

  ngOnInit(): void {
    this.service.onInit();
  }
}
