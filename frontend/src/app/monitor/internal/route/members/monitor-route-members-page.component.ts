import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteStructureComponent } from '@app/route/route-structure.component';
import { MonitorRouteMembersPageService } from './monitor-route-members-page.service';

@Component({
  selector: 'ui-monitor-route-members-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response().result; as page) {
      <ui-route-structure [routeType]="page.routeTypes[0]" [rows]="page.structureRows" />
    }
  `,
  providers: [MonitorRouteMembersPageService],
  imports: [RouteStructureComponent],
})
export class MonitorRouteMembersPageComponent {
  readonly service = inject(MonitorRouteMembersPageService);
  readonly response = this.service.response;
}
