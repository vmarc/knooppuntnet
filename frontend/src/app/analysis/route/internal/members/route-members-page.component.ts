import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteStructureComponent } from '@app/route/route-structure.component';
import { RouteMembersPageService } from './route-members-page.service';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-route-members-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response.hasValue()) {
      <ui-route-structure [routeType]="routeType()" [rows]="members()" />
    }
  `,
  providers: [RouteMembersPageService, RouterService],
  imports: [RouteStructureComponent],
})
export class RouteMembersPageComponent {
  private readonly service = inject(RouteMembersPageService);
  protected readonly response = this.service.response;
  protected readonly routeType = computed(
    () => this.response.value().result.routeInfo.routeTypes[0]
  );
  protected readonly members = computed(() => this.response.value()?.result?.structureRows);
}
