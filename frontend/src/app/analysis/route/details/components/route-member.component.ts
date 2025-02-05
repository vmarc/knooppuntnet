import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';
import { RouteType } from '@api/common';
import { RouteStructureRow } from '@api/common/route';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { TagsTextComponent } from '@app/shared/components/tags/tags-text.component';
import { SymbolComponent } from '@app/symbol';
import { RouteDistanceComponent } from './route-distance.component';
import { RouteMemberIdComponent } from './route-member-id.component';
import { RouteMemberImageComponent } from './route-member-image.component';
import { RouteMemberNameComponent } from './route-member-name.component';

@Component({
  selector: 'kpn-route-member',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();
    <div class="member">
      <div class="member-number">
        {{ rowIndex() + 1 }}
      </div>
      <kpn-route-member-image [row]="row" />
      <div class="member-details">
        <div class="first-line">
          <kpn-route-member-id [structureRow]="row" />
          @if (row.role) {
            <span class="role" matTooltip="role" matTooltipPosition="after">
              {{ row.role }}
            </span>
          }
          <kpn-route-member-name [structureRow]="row" />
          <kpn-route-distance [structureRow]="row" />
        </div>
        @if (row.way && row.way.nodes.length > 0) {
          <div>
            @for (node of row.way.nodes; track node) {
              <div class="kpn-line extra-line">
                <kpn-link-node [nodeId]="node.id" [nodeName]="node.alternateName" />
                @if (node.longName) {
                  <span>{{ node.longName }}</span>
                }
                <span>node</span>
              </div>
            }
          </div>
        }
        @if (row.relation) {
          @if (row.relation.symbol) {
            <kpn-symbol [description]="row.relation.symbol" [width]="25" [height]="25" />
          }
        }
        @if (row.way) {
          @if (!row.way.accessible) {
            <div class="kpn-line extra-line">
              <mat-icon svgIcon="warning" />
              <mat-label>Not accessible</mat-label>
            </div>
          }
        }
        @if (routeType() === 'cycling') {
          @if (row.way) {
            @if (row.way.oneWay === 'forward') {
              <div i18n="@@route.members.table.one-way.yes">Yes</div>
            }
            @if (row.way.oneWay === 'backward') {
              <div i18n="@@route.members.table.one-way.reverse">Reverse</div>
            }
          }
        }
        @if (routeType() === 'cycling') {
          @if (row.way) {
            @if (row.way.oneWayTags.length > 0) {
              <kpn-tags-text [tags]="row.way.oneWayTags" />
            }
          }
        }
        @if (row.relation) {
          @if (row.relation.happy) {
            <mat-icon svgIcon="happy" />
          }
        }
        @if (row.relation && row.relation.survey) {
          <div class="extra-line">
            <span class="kpn-label">Survey</span>
            <span>{{ row.relation.survey | day }}</span>
          </div>
        }
      </div>
    </div>
  `,
  styles: `
    .member {
      border-bottom: 1px solid lightgray;
      display: flex;
    }

    .member-number {
      flex-grow: 0;
      flex-shrink: 0;
      flex-basis: 2em;
      padding: 0.5em;
      border-right: 1px solid lightgray;
    }

    .member-details {
      flex-grow: 1;
      border-left: 1px solid lightgray;
    }

    .role {
      font-style: italic;
      margin-right: 1em;
      padding: 0.3em 0.8em 0.3em 0.5em;
      border-radius: 0.2em;
      border: 1px solid lightgray;
      min-width: 5em;
    }

    .first-line {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }

    .extra-line {
      padding: 0.3em 0.8em 0.3em 2.5em;
    }
  `,
  imports: [
    LinkNodeComponent,
    MatIconModule,
    TagsTextComponent,
    SymbolComponent,
    DayPipe,
    RouteDistanceComponent,
    RouteMemberIdComponent,
    RouteMemberImageComponent,
    RouteMemberNameComponent,
    MatTooltip,
    MatLabel,
    DayPipe,
  ],
})
export class RouteMemberComponent {
  routeType = input.required<RouteType>();
  structureRow = input.required<RouteStructureRow>();
  rowIndex = input.required<number>();
}
