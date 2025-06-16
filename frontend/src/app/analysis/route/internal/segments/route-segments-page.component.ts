import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';

@Component({
  selector: 'ui-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="segments" />
      <div>SEGMENTS</div>
    </ui-page>
  `,
  providers: [RouterService],
  imports: [PageComponent, RoutePageHeaderComponent],
})
export class RouteSegmentsPageComponent {}
