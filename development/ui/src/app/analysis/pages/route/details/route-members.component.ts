import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {RouteMemberInfo} from "../../../../kpn/shared/route/route-member-info";

@Component({
  selector: 'kpn-route-members',
  template: `
    <div>
      <h4>Route Members</h4> <!-- Route onderdelen -->
      <div *ngIf="members.isEmpty()">
        <span>None</span> <!--Geen -->
      </div>
      <div *ngIf="!members.isEmpty()">

        <table class="kpn-table">
          <thead>
          <tr>
            <th></th>
            <th>
              Node
              <!-- Knoop -->
            </th>
            <th>
              Id
              <!-- Id -->
            </th>
            <th colSpan="2">
              Nodes
              <!-- Knooppunten -->
            </th>
            <th>
              Role
              <!-- Rol -->
            </th>

            <th>
              Length
              <!-- Lengte -->
            </th>
            <th>
              #Nodes
              <!-- #Knopen -->
            </th>
            <th>
              Name
              <!-- Naam -->
            </th>
            <th>
              Unaccessible
              <!-- Ontoegankelijk -->
            </th>
            <th colSpan="2" *ngIf="networkType.name == 'rcn'">
              One Way
              <!-- Enkele richting -->
            </th>
          </tr>
          </thead>

          <tbody>
          <tr *ngFor="let member of members">

            <td class="image-cell">
              <img [src]="'/assets/images/links/' + member.linkName + '.png'">
            </td>
            <td>
              <div class="kpn-comma-list">
                <kpn-link-node
                  *ngFor="let node of member.nodes"
                  [nodeId]="node.id"
                  [title]="node.alternateName">
                </kpn-link-node>
              </div>
            </td>
            <td>
              UiOsmLink.osmLink(member.memberType, member.id)
            </td>
            <td>
              UiOsmLink.link("node", member.fromNodeId, member.from)
            </td>
            <td>
              TODO
              <!--TagMod.when(member.isWay) {-->
              <!--UiOsmLink.link("way", member.toNodeId, member.to)-->
              <!--}-->
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
              <div *ngIf="!member.isAccessible">
                TODO
                <--UiImage("warning.png")-->
              </div>
            </td>
            <td *ngIf="networkType.name == 'rcn'">
              TODO
              <!--<div *ngIf="member.oneWay == Forward">-->
              <!--Yes-->
              <!--&lt;!&ndash; Ja &ndash;&gt;-->
              <!--</div>-->
              <!--<div *ngIf="member.oneWay == Backward">-->
              <!--Reverse-->
              <!--&lt;!&ndash; Omgekeerd &ndash;&gt;-->
              <!--</div>-->
            </td>
            <td>
              {{member.oneWayTags.tagString}}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [`
    .image-cell {
      padding: 0px;
      height: 40px;
      min-height: 40px;
      max-height: 40px;
    }
  `]
})
export class RouteMembersComponent {
  @Input() networkType: NetworkType; // page.route.summary.networkType
  @Input() members: List<RouteMemberInfo>;
}
