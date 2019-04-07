import {Component, Input} from '@angular/core';
import {List} from "immutable";
import {ChangeSetNetwork} from "../../../kpn/shared/change-set-network";
import {ChangeSetSummaryInfo} from "../../../kpn/shared/change-set-summary-info";
import {ChangeSetNetworkAction} from "./components/change-set-network.component";

@Component({
  selector: 'kpn-change-set',
  template: `

    <div class="change-set">

      <div class="kpn-line">
        <kpn-link-changeset
          [changeSetId]="changeSet.summary.key.changeSetId"
          [replicationNumber]="changeSet.summary.key.replicationNumber"
          class="kpn-thick">
        </kpn-link-changeset>
        <kpn-timestamp [timestamp]="changeSet.summary.key.timestamp" class="kpn-thin"></kpn-timestamp>
        <mat-icon svgIcon="happy" *ngIf="changeSet.summary.happy"></mat-icon>
        <mat-icon svgIcon="investigate" *ngIf="changeSet.summary.investigate"></mat-icon>
      </div>

      <div *ngIf="changeSet.comment" class="comment">
        {{changeSet.comment}}
      </div>

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
  styleUrls: ['./change-set.component.scss']
})
export class ChangesSetComponent {

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
