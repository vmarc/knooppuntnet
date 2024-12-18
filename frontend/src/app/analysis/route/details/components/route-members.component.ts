import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NetworkType } from '@api/common';
import { RouteStructureRow } from '@api/common/route';
import { DayPipe } from '@app/components/shared/format';
import { LinkNodeComponent } from '@app/components/shared/link';
import { OsmLinkComponent } from '@app/components/shared/link';
import { TagsTextComponent } from '@app/components/shared/tags';
import { ActionButtonRelationComponent } from '../../../components/action/action-button-relation.component';
import { LinkImageComponent } from './link-image.component';
import { SymbolComponent } from '@app/symbol';
import { MonitorRouteGapComponent } from '../../../../monitor/route/monitor-route-gap.component';

@Component({
  selector: 'kpn-route-members',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p i18n="@@route.members.title">Route members</p>
      @if (rows().length === 0) {
        <div>
          <span i18n="@@route.members.none">None</span>
        </div>
      } @else {
        <div>
          <table class="kpn-table">
            <thead>
              <tr>
                <th i18n="@@route.members.table.nr">Nr</th>
                <th></th>
                <th i18n="@@route.members.table.node">Node</th>
                <th i18n="@@route.members.table.id">Id</th>
                <th i18n="@@route.members.table.name">Name</th>
                <th i18n="@@route.members.table.role">Role</th>
                <th i18n="@@route.members.table.length">Length</th>
                <th i18n="@@route.members.table.inaccessible">Inaccessible</th>
                @if (networkType() === 'cycling') {
                  <th colSpan="2" i18n="@@route.members.table.one-way">One Way</th>
                }
                <th [colSpan]="2" i18n="@@monitor.route.relation-table.name">Name</th>
                <th i18n="@@monitor.route.relation-table.relation">Relation</th>
                <th i18n="@@monitor.route.relation-table.symbol">Symbol</th>
                <th i18n="@@monitor.route.relation-table.distance">Distance</th>
                <th i18n="@@monitor.route.relation-table.survey">Survey</th>
                <th i18n="@@monitor.group.route-table.segments">Segments</th>
              </tr>
            </thead>
            <tbody>
              @for (row of rows(); track row; let rowIndex = $index) {
                <tr>
                  <td>
                    {{ rowIndex + 1 }}
                  </td>
                  <td class="image-cell">
                    <kpn-link-image [linkName]="row.linkName" />
                  </td>
                  <td>
                    <div class="kpn-comma-list">
                      @for (node of row.nodes; track node) {
                        <kpn-link-node [nodeId]="node.id" [nodeName]="node.alternateName" />
                      }
                    </div>
                  </td>
                  <td>
                    @if (row.memberType === 'node') {
                      <span>N</span>
                    } @else if (row.memberType === 'way') {
                      <span>W</span>
                    } @else if (row.memberType === 'relation') {
                      <span>R</span>
                    }
                    <kpn-osm-link
                      [kind]="row.memberType"
                      [elementId]="row.id.toString()"
                      [title]="row.id.toString()"
                    />
                  </td>
                  <td>
                    {{ row.description }}
                  </td>
                  <td>
                    {{ row.role }}
                  </td>
                  <td class="distance">
                    {{ row.length }}
                  </td>
                  <td>
                    @if (!row.accessible) {
                      <div>
                        <mat-icon svgIcon="warning" />
                      </div>
                    }
                  </td>
                  @if (networkType() === 'cycling') {
                    <td>
                      @if (row.oneWay === 'forward') {
                        <div i18n="@@route.members.table.one-way.yes">Yes</div>
                      }
                      @if (row.oneWay === 'backward') {
                        <div i18n="@@route.members.table.one-way.reverse">Reverse</div>
                      }
                    </td>
                  }
                  @if (networkType() === 'cycling') {
                    <td>
                      @if (row.oneWayTags.length > 0) {
                        <kpn-tags-text [tags]="row.oneWayTags" />
                      }
                    </td>
                  }
                  <td>
                    @switch (row.level) {
                      @case (1) {
                        <div class="level-1">{{ row.name }}</div>
                      }
                      @case (2) {
                        <div class="level-2">{{ row.name }}</div>
                      }
                      @case (3) {
                        <div class="level-3">{{ row.name }}</div>
                      }
                      @case (4) {
                        <div class="level-4">{{ row.name }}</div>
                      }
                      @case (5) {
                        <div class="level-5">{{ row.name }}</div>
                      }
                    }
                  </td>
                  <td>
                    @if (row.happy) {
                      <mat-icon svgIcon="happy" />
                    }
                  </td>
                  <td class="action-button-table-cell">
                    <div class="kpn-align-center">
                      <kpn-action-button-relation [relationId]="row.relationId" />
                      {{ row.relationId }}
                    </div>
                  </td>
                  <td class="symbol">
                    @if (row.symbol) {
                      <kpn-symbol [description]="row.symbol" [width]="25" [height]="25" />
                    }
                  </td>
                  <td>
                    <div class="distance">
                      0m
                      <!--                      @if (row.osmDistanceSubRelations > 0) {-->
                      <!--                        <span-->
                      <!--                          class="cumulative-distance"-->
                      <!--                          matTooltip="Total length of ways in all subrelations"-->
                      <!--                        >-->
                      <!--                          {{ row.osmDistanceSubRelations | distance }}-->
                      <!--                        </span>-->
                      <!--                      }-->

                      <!--                      @if (row.osmDistanceSubRelations > 0 && row.osmDistance > 0) {-->
                      <!--                        <span> / </span>-->
                      <!--                      }-->

                      <!--                      @if (row.osmDistance > 0) {-->
                      <!--                        <span matTooltip="Total length of ways in this relation">-->
                      <!--                          {{ row.osmDistance | distance }}-->
                      <!--                        </span>-->
                      <!--                      }-->
                    </div>
                  </td>
                  <td>
                    {{ row.survey | day }}
                  </td>
                  <td [ngClass]="{ 'no-route-gap': row.gaps === undefined }">
                    gaps
                    @if (row.gaps !== undefined) {
                      <kpn-monitor-route-gap
                        [description]=""
                        [osmSegmentCount]="row.osmSegmentCount"
                      />
                    }
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      }
    </div>
  `,
  styles: `
    .image-cell {
      padding: 0;
      height: 100%;
    }

    .distance {
      white-space: nowrap;
      text-align: right;
    }
  `,
  imports: [
    LinkImageComponent,
    LinkNodeComponent,
    NgClass,
    MatIconModule,
    OsmLinkComponent,
    TagsTextComponent,
    SymbolComponent,
    MonitorRouteGapComponent,
    ActionButtonRelationComponent,
    DayPipe,
  ],
})
export class RouteMembersComponent {
  networkType = input.required<NetworkType>();
  rows = input.required<RouteStructureRow[]>();
}
