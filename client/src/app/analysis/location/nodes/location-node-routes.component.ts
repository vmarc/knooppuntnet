import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {LocationNodeInfo} from "../../../kpn/api/common/location/location-node-info";

@Component({
  selector: "kpn-location-node-routes",
  template: `
    <span *ngIf="node.routeReferences.isEmpty()" class="no-routes" i18n="@@location-nodes.no-routes">
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
export class LocationNodeRoutesComponent {
  @Input() node: LocationNodeInfo;
}
