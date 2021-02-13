import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkType} from '@api/custom/network-type';
import {RouteMemberInfo} from '@api/custom/route-member-info';

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
            <th i18n="@@route.members.table.unaccessible">Unaccessible</th>
            <th colSpan="2" *ngIf="networkType === 'cycling'" i18n="@@route.members.table.one-way">One Way</th>
          </tr>
          </thead>

          <tbody>
          <tr *ngFor="let member of members">

            <td class="image-cell">
              <div class="image">
                <img [src]="'/assets/images/links/' + member.linkName + '.png'" [alt]="member.linkName">
              </div>
            </td>
            <td>
              <div class="kpn-comma-list">
                <kpn-link-node
                    *ngFor="let node of member.nodes"
                    [nodeId]="node.id"
                    [nodeName]="node.alternateName">
                </kpn-link-node>
              </div>
            </td>
            <td>
              <kpn-osm-link [kind]="member.memberType" [elementId]="member.id.toString()" [title]="member.id.toString()"></kpn-osm-link>
            </td>
            <td>
              <kpn-osm-link kind="node" [elementId]="member.fromNodeId.toString()" [title]="member.from"></kpn-osm-link>
            </td>
            <td>
              <kpn-osm-link *ngIf="member.isWay" kind="node" [elementId]="member.toNodeId.toString()" [title]="member.to"></kpn-osm-link>
            </td>
            <td>
              {{member.role}}
            </td>
            <td>
              {{member.length}}
            </td>
            <td>
              {{member.nodeCount}}
            </td>
            <td>
              {{member.description}}
            </td>
            <td>
              <div *ngIf="!member.accessible">
                <mat-icon svgIcon="warning"></mat-icon>
              </div>
            </td>
            <td *ngIf="networkType === 'cycling'">
              <div *ngIf="member.oneWay == 'Forward'" i18n="@@route.members.table.one-way.yes">Yes</div>
              <div *ngIf="member.oneWay == 'Backward'" i18n="@@route.members.table.one-way.reverse">Reverse</div>
            </td>
            <td *ngIf="networkType === 'cycling'">
              <kpn-tags-text *ngIf="member.oneWayTags.tags.length > 0" [tags]="member.oneWayTags"></kpn-tags-text>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [`
    .image-cell {
      padding: 0;
      height: 40px;
      min-height: 40px;
      max-height: 40px;
    }

    .image {
      height: 40px;
      padding: 0;
      margin: 0;
    }
  `]
})
export class RouteMembersComponent {
  @Input() networkType: NetworkType;
  @Input() members: RouteMemberInfo[];
}
