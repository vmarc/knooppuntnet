import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NetworkType } from '@api/custom';
import { RouteMemberInfo } from '@api/custom';
import { LinkNodeComponent } from '@app/components/shared/link';
import { OsmLinkComponent } from '@app/components/shared/link';
import { TagsTextComponent } from '@app/components/shared/tags';
import { LinkImageComponent } from './link-image.component';

@Component({
  selector: 'kpn-route-members',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <h4 i18n="@@route.members.title">Route Members</h4>
      @if (members.length === 0) {
        <div>
          <span i18n="@@route.members.none">None</span>
        </div>
      } @else {
        <div>
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
                @if (networkType === 'cycling') {
                  <th colSpan="2" i18n="@@route.members.table.one-way">One Way</th>
                }
              </tr>
            </thead>
            <tbody>
              @for (member of members; track member) {
                <tr>
                  <td class="image-cell">
                    <kpn-link-image [linkName]="member.linkName" />
                  </td>
                  <td>
                    <div class="kpn-comma-list">
                      @for (node of member.nodes; track node) {
                        <kpn-link-node [nodeId]="node.id" [nodeName]="node.alternateName" />
                      }
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
                    @if (member.isWay) {
                      <kpn-osm-link
                        kind="node"
                        [elementId]="member.toNodeId.toString()"
                        [title]="member.to"
                      />
                    }
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
                    @if (!member.accessible) {
                      <div>
                        <mat-icon svgIcon="warning" />
                      </div>
                    }
                  </td>
                  @if (networkType === 'cycling') {
                    <td>
                      @if (member.oneWay === 'Forward') {
                        <div i18n="@@route.members.table.one-way.yes">Yes</div>
                      }
                      @if (member.oneWay === 'Backward') {
                        <div i18n="@@route.members.table.one-way.reverse">Reverse</div>
                      }
                    </td>
                  }
                  @if (networkType === 'cycling') {
                    <td>
                      @if (member.oneWayTags.tags.length > 0) {
                        <kpn-tags-text [tags]="member.oneWayTags" />
                      }
                    </td>
                  }
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
  standalone: true,
  imports: [
    LinkImageComponent,
    LinkNodeComponent,
    MatIconModule,
    OsmLinkComponent,
    TagsTextComponent,
  ],
})
export class RouteMembersComponent {
  @Input() networkType: NetworkType;
  @Input() members: RouteMemberInfo[];
}
