import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteType } from '@api/common/route-type';
import { StructureRow } from '@api/common/route/structure-row';
import { RouteStructureRowComponent } from './route-structure-row.component';

@Component({
  selector: 'ui-route-structure',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      @if (rows().length === 0) {
        <div>
          <span i18n="@@route.members.none">None</span>
        </div>
      } @else {
        <div class="members">
          @for (row of rows(); track row) {
            @defer (on viewport) {
              <ui-route-structure-row [routeType]="routeType()" [structureRow]="row" />
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
  imports: [MatIconModule, RouteStructureRowComponent],
})
export class RouteStructureComponent {
  readonly routeType = input.required<RouteType>();
  readonly rows = input.required<StructureRow[]>();
}
