import {Component, Input} from "@angular/core";
import {NetworkNodeInfo2} from "../../../kpn/api/common/network/network-node-info2";

@Component({
  selector: "network-node-routes",
  template: `

    <span *ngIf="node.routeReferences.isEmpty()" class="no-routes" i18n="@@network-nodes.no-routes">
      no routes
    </span>

    <div *ngIf="node.routeReferences.size > 0" class="kpn-comma-list route-list">
      <span *ngFor="let ref of node.routeReferences">
        <a [routerLink]="'/analysis/route/' + ref.id">{{ref.name}}</a>
      </span>
    </div>
  `,
  styles: [`

    .no-routes {
      color: red;
    }

    .route-list {
      display: inline-block;
    }

  `]
})
export class NetworkNodeRoutesComponent {
  @Input() node: NetworkNodeInfo2;
}
