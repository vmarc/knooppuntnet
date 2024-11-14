import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
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
      <p i18n="@@route.members.title">Route members</p>
      @if (members().length === 0) {
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
              </tr>
            </thead>
            <tbody>
              @for (member of members(); track member; let rowIndex = $index) {
                <tr>
                  <td>
                    {{ rowIndex + 1 }}
                  </td>
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
                    @if (member.memberType === 'node') {
                      <span>N</span>
                    } @else if (member.memberType === 'way') {
                      <span>W</span>
                    } @else if (member.memberType === 'relation') {
                      <span>R</span>
                    }
                    <kpn-osm-link
                      [kind]="member.memberType"
                      [elementId]="member.id.toString()"
                      [title]="member.id.toString()"
                    />
                  </td>
                  <td>
                    {{ member.description }}
                  </td>
                  <td>
                    {{ member.role }}
                  </td>
                  <td class="distance">
                    {{ member.length }}
                  </td>
                  <td>
                    @if (!member.accessible) {
                      <div>
                        <mat-icon svgIcon="warning" />
                      </div>
                    }
                  </td>
                  @if (networkType() === 'cycling') {
                    <td>
                      @if (member.oneWay === 'Forward') {
                        <div i18n="@@route.members.table.one-way.yes">Yes</div>
                      }
                      @if (member.oneWay === 'Backward') {
                        <div i18n="@@route.members.table.one-way.reverse">Reverse</div>
                      }
                    </td>
                  }
                  @if (networkType() === 'cycling') {
                    <td>
                      @if (member.oneWayTags.length > 0) {
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
  networkType = input.required<NetworkType>();
  members = input.required<RouteMemberInfo[]>();
}
