import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';
import { NetworkType } from '@api/common';
import { RouteStructureRow } from '@api/common/route';
import { DayPipe } from '@app/components/shared/format';
import { LinkNodeComponent } from '@app/components/shared/link';
import { TagsTextComponent } from '@app/components/shared/tags';
import { SymbolComponent } from '@app/symbol';
import { RouteDistanceComponent } from './route-distance.component';
import { RouteMemberIdComponent } from './route-member-id.component';
import { RouteMemberImageComponent } from './route-member-image.component';
import { RouteMemberNameComponent } from './route-member-name.component';

@Component({
  selector: 'kpn-route-members',
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
        <div>
          <div class="items">
            @for (row of rows(); track row; let rowIndex = $index) {
              @defer (on viewport) {
                <div class="item">
                  <div class="item-number">
                    {{ rowIndex + 1 }}
                  </div>
                  <kpn-route-member-image [row]="row" />
                  <div class="row-contents">
                    <div class="row-line-1">
                      <kpn-route-member-id [row]="row" />
                      @if (row.role) {
                        <span class="role" matTooltip="role" matTooltipPosition="after">
                          {{ row.role }}
                        </span>
                      }
                      <kpn-route-member-name [row]="row" />
                      <kpn-route-distance [row]="row" />
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
                        <kpn-symbol
                          [description]="row.relation.symbol"
                          [width]="25"
                          [height]="25"
                        />
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
                    @if (networkType() === 'cycling') {
                      @if (row.way) {
                        @if (row.way.oneWay === 'forward') {
                          <div i18n="@@route.members.table.one-way.yes">Yes</div>
                        }
                        @if (row.way.oneWay === 'backward') {
                          <div i18n="@@route.members.table.one-way.reverse">Reverse</div>
                        }
                      }
                    }
                    @if (networkType() === 'cycling') {
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
                    @if (row.relation) {
                      {{ row.relation.survey | day }}
                    }
                  </div>
                </div>
              } @placeholder {
                <div class="item-placeholder">{{ rowIndex }}</div>
              }
            }
          </div>
        </div>
      }
    </div>
  `,
  styles: `
    .items {
      margin-top: 20px;
      border-top-color: lightgray;
      border-top-style: solid;
      border-top-width: 1px;
    }

    .item {
      border-bottom: 1px solid lightgray;
      display: flex;
    }

    @media (max-width: 768px) {
      /* media.maxWidth(PageWidth.SmallMaxWidth.px) */
      .items {
        margin-left: -20px;
        margin-right: -20px;
      }
    }

    .item-number {
      flex-grow: 0;
      flex-shrink: 0;
      flex-basis: 2em;
      padding: 0.5em;
      border-right: 1px solid lightgray;
    }

    .item-placeholder {
      height: 18em;
    }

    .role {
      font-style: italic;
      margin-right: 1em;
      padding: 0.3em 0.8em 0.3em 0.5em;
      border-radius: 0.2em;
      border: 1px solid lightgray;
      min-width: 5em;
    }

    .extra-line {
      padding: 0.3em 0.8em 0.3em 2.5em;
    }

    .row-line-1 {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }

    .row-contents {
      flex-grow: 1;
      border-left: 1px solid lightgray;
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
  ],
})
export class RouteMembersComponent {
  networkType = input.required<NetworkType>();
  rows = input.required<RouteStructureRow[]>();
}
