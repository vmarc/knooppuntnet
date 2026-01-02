import { OnInit } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteStructureComponent } from '@app/route/route-structure.component';
import { RouteMembersPageService } from './route-members-page.service';

@Component({
  selector: 'ui-route-members-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response()) {
      <ui-route-structure [routeType]="routeType()" [rows]="members()" />
    }
  `,
  providers: [RouteMembersPageService],
  imports: [RouteStructureComponent],
})
export class RouteMembersPageComponent implements OnInit {
  private readonly service = inject(RouteMembersPageService);
  protected readonly response = this.service.response;
  protected readonly routeType = computed(() => this.response()?.result.routeInfo.routeTypes[0]);
  protected readonly members = computed(() => this.response()?.result?.structureRows);

  ngOnInit(): void {
    this.service.onInit();
  }
}
