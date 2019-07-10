import {Component, Input, OnInit} from "@angular/core";
import {NodeNetworkReference} from "../../../../kpn/shared/node/node-network-reference";
import {NodeInfo} from "../../../../kpn/shared/node-info";

@Component({
  selector: "kpn-node-network-reference-statement",
  template: `

    <markdown *ngIf="summaryStatement == 'role-connection'" i18n="@@node.network.role-connection">
      This node has the role _"connection"_ in the networkrelation.
    </markdown>

    <markdown *ngIf="summaryStatement == 'role-connection-invalid'" i18n="@@node.network.role-connection">
      This node has the role _"connection"_ in the networkrelation:
      the _"{{expectedRouteRelationsTag}}"_ tag does not
      apply to this netwerk.
    </markdown>

    <p *ngIf="summaryStatement == 'network-member'" i18n="@@node.network.network-member">
      This node is included as member in the networkrelation.
    </p>

    <p *ngIf="summaryStatement == 'not-network-member'" i18n="@@node.network.not-network-member">
      This node is not included as member in the networkrelation.
    </p>

    <markdown *ngIf="summaryStatement == 'not-network-member-invalid'" i18n="@@node.network.not-network-member-invalid">
      This node is not included as member in the networkrelation:
      the _"{{expectedRouteRelationsTag}}"_ tag does not apply
      to this netwerk.
    </markdown>

  `
})
export class NodeNetworkReferenceStatementComponent implements OnInit {

  @Input() nodeInfo: NodeInfo;
  @Input() reference: NodeNetworkReference;

  summaryStatement: string;

  ngOnInit(): void {
    let statement = "";
    if (this.reference.nodeRoleConnection) {
      if (this.hasExpectedRouteRelationsTag()) {
        statement = "role-connection-invalid";
      } else {
        statement = "role-connection";
      }
    } else if (this.reference.nodeDefinedInRelation) {
      statement = "network-member";
    } else if (this.hasExpectedRouteRelationsTag()) {
      statement = "not-network-member-invalid";
    } else {
      statement = "not-network-member";
    }

    this.summaryStatement = statement;
  }

  get expectedRouteRelationsTag() {
    return `expected_${this.reference.networkType.id}_route_relations`
  }

  private hasExpectedRouteRelationsTag() {
    return this.nodeInfo.tags.has(this.expectedRouteRelationsTag);
  }

}
