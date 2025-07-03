import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatLabel } from '@angular/material/form-field';
import { MatTooltip } from '@angular/material/tooltip';
import { RouteType } from '@api/common/route-type';
import { StructureRow } from '@api/common/route/structure-row';
import {
  RouteStructureDeviationsComponent
} from '@app/route/internal/components/structure/route-structure-deviations.component';
import { RouteStructureReferenceComponent } from '@app/route/internal/components/structure/route-structure-reference.component';
import { RouteDetailsService } from '@app/route/route-details-service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconWarningComponent } from '@app/shared/components/icon/icon-warning.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { TagsTextComponent } from '@app/shared/components/tags/tags-text.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { RouteDistanceComponent } from './route-distance.component';
import { RouteMemberIdComponent } from './route-member-id.component';
import { RouteMemberImageComponent } from './route-member-image.component';
import { RouteMemberNameComponent } from './route-member-name.component';

@Component({
  selector: 'ui-route-structure-row',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    @let row = structureRow();
    <div class="member">
      <div class="member-number">
        {{ rowIndex() + 1 }}
      </div>
      <ui-route-member-image [row]="row" />
      <div class="member-details">
        <div class="first-line">
          <ui-route-member-id [structureRow]="row" />
          @if (row.role) {
            <span class="role" matTooltip="role" matTooltipPosition="after">
              {{ row.role }}
            </span>
          }
          <ui-route-member-name [structureRow]="row" />
          <ui-route-distance [structureRow]="row" />
        </div>
        @if (row.way && row.way.nodes.length > 0) {
          <div>
            @for (node of row.way.nodes; track node) {
              <div class="kpn-line extra-line">
                <ui-link-node [nodeId]="node.id" [nodeName]="node.alternateName" />
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
            <ui-symbol [description]="row.relation.symbol" [width]="25" [height]="25" />
          }
        }
        @if (row.way) {
          @if (!row.way.accessible) {
            <div class="kpn-line extra-line">
              <ui-icon-warning />
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
              <ui-tags-text [tags]="row.way.oneWayTags" />
            }
          }
        }
        @if (row.relation) {
          @if (row.relation.happy) {
            <ui-icon-happy />
          }
        }
        @if (row.relation && row.relation.survey) {
          <div class="extra-line">
            <span class="kpn-label">Survey</span>
            <span>{{ row.relation.survey | day }}</span>
          </div>
        }

        @if (row.happy) {
          <div>
            <ui-icon-happy />
          </div>
        }

        @if (row.symbol) {
          <div>
            <span i18n="@@monitor.route.relation-table.symbol" class="kpn-label"> Symbol </span>
            <span class="symbol">
              <ui-symbol [description]="row.symbol" [width]="25" [height]="25" />
            </span>
          </div>
        }

        @if (hasReference()) {
          <ui-route-structure-reference [structureRow]="row" />
        }

        @if (row.deviationCount > 0) {
          <ui-route-structure-deviations [structureRow]="row" />
        }

        <div>
          <span i18n="@@monitor.group.route-table.segments" class="kpn-label"> Segments </span>
          <span [ngClass]="{ 'no-route-gap': row.gaps === undefined }">
            @if (row.gaps !== undefined) {
              GAP
              <!--              <ui-monitor-route-gap-->
              <!--                [description]="row.gaps"-->
              <!--                [osmSegmentCount]="row.osmSegmentCount"-->
              <!--              />-->
            }
          </span>
        </div>

        <!--      {{ 'surface=' + row.way?.surface }}-->
        <!--      {{ 'pathIds=' + row.pathIds }}-->
        <!--      {{ 'segmentIds=' + row.segmentIds }}-->
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
      padding: 0.5em;
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
      gap: 0.5em;
    }

    .extra-line {
      padding: 0.3em 0.8em 0.3em 2.5em;
    }
  `,
  imports: [
    DayPipe,
    DistancePipe,
    IconHappyComponent,
    IconWarningComponent,
    LinkNodeComponent,
    MatLabel,
    MatTooltip,
    NgClass,
    RouteDistanceComponent,
    RouteMemberIdComponent,
    RouteMemberImageComponent,
    RouteMemberNameComponent,
    RouteStructureReferenceComponent,
    SymbolComponent,
    TagsTextComponent,
    RouteStructureDeviationsComponent,
  ],
})
export class RouteStructureRowComponent {
  readonly routeType = input.required<RouteType>();
  readonly structureRow = input.required<StructureRow>();
  readonly rowIndex = input.required<number>();

  private readonly routeDetailsService = inject(RouteDetailsService);

  hasReference(): boolean {
    return this.routeDetailsService.referenceType() === 'multi-gpx';
  }
}
