import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkType } from '@api/custom';
import { RouteMemberInfo } from '@api/custom';

@Component({
  selector: 'kpn-route-members',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <h4 i18n="@@route.members.title">Route Members</h4>
      <div *ngIf="members.length === 0">
        <span i18n="@@route.members.none">None</span>
      </div>
      <div *ngIf="members.length > 0">
        <table class="kpn-table">
          <thead>
            <tr>
              <th></th>
              <th i18n="@@route.members.table.node">Node</th>
              <th i18n="@@route.members.table.id">Id</th>
              <th colSpan="2" i18n="@@route.members.table.nodes">Nodes</th>
              <th i18n="@@route.members.table.role">Role</th>
              <th i18n="@@route.members.table.length">Length</th>
              <th i18n="@@route.members.table.node-count">#Nodes</th>
              <th i18n="@@route.members.table.name">Name</th>
              <th i18n="@@route.members.table.inaccessible">Inaccessible</th>
              <th
                colSpan="2"
                *ngIf="networkType === 'cycling'"
                i18n="@@route.members.table.one-way"
              >
                One Way
              </th>
            </tr>
          </thead>

          <tbody>
            <tr *ngFor="let member of members">
              <td class="image-cell">
                <kpn-link-image [linkName]="member.linkName" />
              </td>
              <td>
                <div class="kpn-comma-list">
                  <kpn-link-node
                    *ngFor="let node of member.nodes"
                    [nodeId]="node.id"
                    [nodeName]="node.alternateName"
                  />
                </div>
              </td>
              <td>
                <kpn-osm-link
                  [kind]="member.memberType"
                  [elementId]="member.id.toString()"
                  [title]="member.id.toString()"
                />
              </td>
              <td>
                <kpn-osm-link
                  kind="node"
                  [elementId]="member.fromNodeId.toString()"
                  [title]="member.from"
                />
              </td>
              <td>
                <kpn-osm-link
                  *ngIf="member.isWay"
                  kind="node"
                  [elementId]="member.toNodeId.toString()"
                  [title]="member.to"
                />
              </td>
              <td>
                {{ member.role }}
              </td>
              <td class="distance">
                {{ member.length }}
              </td>
              <td>
                {{ member.nodeCount }}
              </td>
              <td>
                {{ member.description }}
              </td>
              <td>
                <div *ngIf="!member.accessible">
                  <mat-icon svgIcon="warning" />
                </div>
              </td>
              <td *ngIf="networkType === 'cycling'">
                <div
                  *ngIf="member.oneWay === 'Forward'"
                  i18n="@@route.members.table.one-way.yes"
                >
                  Yes
                </div>
                <div
                  *ngIf="member.oneWay === 'Backward'"
                  i18n="@@route.members.table.one-way.reverse"
                >
                  Reverse
                </div>
              </td>
              <td *ngIf="networkType === 'cycling'">
                <kpn-tags-text
                  *ngIf="member.oneWayTags.tags.length > 0"
                  [tags]="member.oneWayTags"
                />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [
    `
      .image-cell {
        padding: 0;
        height: 100%;
      }

      .distance {
        white-space: nowrap;
        text-align: right;
      }
    `,
  ],
})
export class RouteMembersComponent {
  @Input() networkType: NetworkType;
  @Input() members: RouteMemberInfo[];
}
