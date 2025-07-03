import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatLabel } from '@angular/material/form-field';
import { MatTooltip } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { Params } from '@angular/router';
import { RouteType } from '@api/common/route-type';
import { StructureRow } from '@api/common/route/structure-row';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';
import { RouteDetailsService } from '@app/route/route-details-service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { TimestampDayPipe } from '@app/shared/components/format/timestamp-day.pipe';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconWarningComponent } from '@app/shared/components/icon/icon-warning.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { TagsTextComponent } from '@app/shared/components/tags/tags-text.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
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

        <div>
          <span i18n="@@monitor.route.relation-table.relation" class="kpn-label"> Relation </span>
          <span class="action-button-table-cell">
            <div class="kpn-align-center">
              <ui-action-button-relation [relationId]="row.relationId" />
              {{ row.relationId }}
            </div>
          </span>
        </div>

        <div>
          <span i18n="@@monitor.route.relation-table.symbol" class="kpn-label"> Symbol </span>
          <span class="symbol">
            @if (structureRow().symbol) {
              <ui-symbol [description]="row.symbol" [width]="25" [height]="25" />
            }
          </span>
        </div>

        <div>
          <span i18n="@@monitor.route.relation-table.role" class="kpn-label">Role</span>
          <span>
            {{ row.role }}
          </span>
        </div>

        <div>
          <span i18n="@@monitor.route.relation-table.distance" class="kpn-label"> Distance </span>
          <span class="distance">
            @if (row.osmDistanceSubRelations > 0) {
              <span
                class="cumulative-distance"
                matTooltip="Total length of ways in all subrelations"
              >
                {{ row.osmDistanceSubRelations | distance }}
              </span>
            }

            @if (row.osmDistanceSubRelations > 0 && row.osmDistance > 0) {
              <span> / </span>
            }

            @if (row.osmDistance > 0) {
              <span matTooltip="Total length of ways in this relation">
                {{ row.osmDistance | distance }}
              </span>
            }
          </span>
        </div>

        <div>
          <span i18n="@@monitor.route.relation-table.survey" class="kpn-label"> Survey </span>
          <span>
            {{ row.survey | day }}
          </span>
        </div>

        @if (row.referenceFilename) {
          <div class="kpn-line">
            <span i18n="@@monitor.group.route-table.reference" class="kpn-label">Reference</span>
            <span>
              {{ row.referenceTimestamp | yyyymmdd }}
            </span>
            <span>
              {{ row.referenceDistance | distance }}
            </span>
            <span>
              {{ row.referenceFilename }}
            </span>
          </div>
        }

        @if (row.deviationCount > 0) {}
        <div class="kpn-line">
          <span i18n="@@monitor.group.route-table.deviations" class="kpn-label"> Deviations </span>
          <span>
            {{ row.deviationCount }}
          </span>
          <span>
            {{ row.deviationDistance | distance }}
          </span>
        </div>

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

        <div>
          <span i18n="@@monitor.group.route-table.actions" class="kpn-label">Actions</span>
          <span class="kpn-action-cell">
            @if (row.physical) {
              <button
                mat-icon-button
                [routerLink]="uploadGpx()"
                [queryParams]="subRelationIdQueryParams(row)"
                [disabled]="!canUpload()"
                title="Upload GPX trace for this sub-relation"
                i18n-title="@@action.gpx.upload"
                class="kpn-action-button"
                [class.kpn-disabled]="!canUpload()"
              >
                <nz-icon nzType="upload" />
              </button>
              <button
                [routerLink]="deleteGpx()"
                [queryParams]="subRelationIdQueryParams(row)"
                [disabled]="!canDelete(row)"
                title="Remove GPX trace for this sub-relation"
                i18n-title="@@action.gpx.delete"
                class="kpn-action-button"
                [class.kpn-disabled]="!row.referenceFilename"
                [class.kpn-warning]="row.referenceFilename"
              >
                <nz-icon nzType="delete" />
              </button>
            }
          </span>
        </div>
      </div>

      <!--      {{ 'surface=' + row.way?.surface }}-->
      <!--      {{ 'pathIds=' + row.pathIds }}-->
      <!--      {{ 'segmentIds=' + row.segmentIds }}-->
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
    IconHappyComponent,
    IconWarningComponent,
    LinkNodeComponent,
    MatLabel,
    MatTooltip,
    RouteDistanceComponent,
    RouteMemberIdComponent,
    RouteMemberImageComponent,
    RouteMemberNameComponent,
    SymbolComponent,
    TagsTextComponent,
    ActionButtonRelationComponent,
    DistancePipe,
    MatIconButton,
    TimestampDayPipe,
    NgClass,
    RouterLink,
    NzIconDirective,
  ],
})
export class RouteStructureRowComponent {
  readonly routeType = input.required<RouteType>();
  readonly structureRow = input.required<StructureRow>();
  readonly rowIndex = input.required<number>();

  private readonly routeDetailsService = inject(RouteDetailsService);

  subRelationIdQueryParams(row: StructureRow): Params {
    if (row.level === 1) {
      return {};
    }
    return { 'sub-relation-id': row.relationId };
  }

  uploadGpx(): string {
    return `/monitor/groups/${this.routeDetailsService.groupName()}/routes/${this.routeDetailsService.routeName()}/gpx`;
  }

  deleteGpx(): string {
    return `/monitor/groups/${this.routeDetailsService.groupName()}/routes/${this.routeDetailsService.routeName()}/gpx/delete`;
  }

  canUpload(): boolean {
    return this.routeDetailsService.referenceType() === 'multi-gpx';
  }

  canDelete(row: StructureRow): boolean {
    return this.routeDetailsService.referenceType() === 'multi-gpx' && !!row.referenceFilename;
  }
}
