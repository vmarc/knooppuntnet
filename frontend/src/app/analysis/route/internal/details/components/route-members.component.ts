import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteType } from '@api/common/route-type';
import { RouteStructureRow } from '@api/common/route/route-structure-row';
import { RouteMemberComponent } from './route-member.component';

@Component({
  selector: 'ui-route-members',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p i18n="@@route.members.title">
        <span>Route members</span>
        <span class="kpn-brackets">{{ rows().length }}</span>
      </p>
      @if (rows().length === 0) {
        <div>
          <span i18n="@@route.members.none">None</span>
        </div>
      } @else {
        <div class="members">
          @for (row of rows(); track row; let rowIndex = $index) {
            @defer (on viewport) {
              <ui-route-member
                [routeType]="routeType()"
                [structureRow]="row"
                [rowIndex]="rowIndex"
              />
            } @placeholder {
              <div class="member-placeholder"></div>
            }
          }
        </div>
      }
    </div>
  `,
  styles: `
    .members {
      margin-top: 20px;
      border-top-color: lightgray;
      border-top-style: solid;
      border-top-width: 1px;
    }

    @media (max-width: 768px) {
      .members {
        margin-left: -20px;
        margin-right: -20px;
      }
    }

    .member-placeholder {
      height: 3em;
    }
  `,
  imports: [MatIconModule, RouteMemberComponent],
})
export class RouteMembersComponent {
  routeType = input.required<RouteType>();
  rows = input.required<RouteStructureRow[]>();
}
