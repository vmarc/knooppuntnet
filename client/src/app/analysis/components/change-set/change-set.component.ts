import {Component, Input, OnInit} from "@angular/core";
import {List} from "immutable";
import {ChangeSetNetwork} from "../../../kpn/shared/change-set-network";
import {ChangeSetSummaryInfo} from "../../../kpn/shared/change-set-summary-info";
import {ChangeSetNetworkAction} from "./components/change-set-network.component";

@Component({
  selector: "kpn-change-set",
  template: `

    <div class="change-set">

      <kpn-change-header
        [changeKey]="changeSet.summary.key"
        [happy]="changeSet.summary.happy"
        [investigate]="changeSet.summary.investigate"
        [comment]="changeSet.comment">
      </kpn-change-header>

      <div *ngFor="let action of networkActions">
        <kpn-change-set-network [changeSetNetworkAction]="action"></kpn-change-set-network>
      </div>

      <div *ngFor="let subsetElementRefs of changeSet.summary.orphanNodeChanges">
        <kpn-change-set-orphan-nodes [subsetElementRefs]="subsetElementRefs"></kpn-change-set-orphan-nodes>
      </div>

      <div *ngFor="let subsetElementRefs of changeSet.summary.orphanRouteChanges">
        <kpn-change-set-orphan-routes [subsetElementRefs]="subsetElementRefs"></kpn-change-set-orphan-routes>
      </div>

    </div>
  `,
  styles: [`
    .change-set {
      margin-top: 5px;
      margin-bottom: 5px;
    }
  `]
})
export class ChangesSetComponent implements OnInit {

  @Input() changeSet: ChangeSetSummaryInfo;
  networkActions: List<ChangeSetNetworkAction>;

  ngOnInit() {
    this.networkActions = this.buildNetworkActions();
  }

  private buildNetworkActions(): List<ChangeSetNetworkAction> {
    const networkChanges = this.changeSet.summary.networkChanges;
    const creates = this.toNetworkActions("C", networkChanges.creates);
    const updates = this.toNetworkActions("U", networkChanges.updates);
    const deletes = this.toNetworkActions("D", networkChanges.deletes);
    return creates.concat(updates).concat(deletes);
  }

  private toNetworkActions(action: string, networks: List<ChangeSetNetwork>) {
    return networks.map(nc => new ChangeSetNetworkAction(action, nc));
  }

}
