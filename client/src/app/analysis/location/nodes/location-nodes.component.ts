import {ChangeDetectionStrategy} from "@angular/core";
import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {LocationNodesPage} from "../../../kpn/api/common/location/location-nodes-page";
import {LocationNodesPageService} from "./location-nodes-page.service";

@Component({
  selector: "kpn-location-nodes",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="page.nodes.isEmpty()" class="kpn-spacer-above" i18n="@@location-nodes.no-nodes">
      No nodes
    </div>
    <kpn-location-node-table
      *ngIf="!page.nodes.isEmpty()"
      (page)="service.pageChanged($event)"
      [timeInfo]="page.timeInfo"
      [nodes]="page.nodes"
      [nodeCount]="page.summary.nodeCount">
    </kpn-location-node-table>
  `
})
export class LocationNodesComponent {

  @Input() page: LocationNodesPage;

  constructor(public service: LocationNodesPageService) {
  }
}
