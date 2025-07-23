import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouteStructureComponent } from '@app/route/route-structure.component';
import { RouteMembersPageService } from './route-members-page.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';

@Component({
  selector: 'ui-route-members-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="members" />
      <ui-route-structure [routeType]="routeType()" [rows]="members()" />
    </ui-page>
  `,
  providers: [RouteMembersPageService, RouterService],
  imports: [PageComponent, RoutePageHeaderComponent, RouteStructureComponent],
})
export class RouteMembersPageComponent implements OnInit {
  private readonly service = inject(RouteMembersPageService);
  protected readonly routeType = computed(
    () => this.service.response().result.routeInfo.routeTypes[0]
  );
  protected readonly members = computed(() => this.service.response().result.structureRows);

  ngOnInit(): void {
    this.service.onInit();
  }
}
