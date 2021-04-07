import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { NodeInfo } from '@api/common/node-info';
import { NodeNetworkReference } from '@api/common/node/node-network-reference';
import { NetworkTypes } from '../../../kpn/common/network-types';

@Component({
  selector: 'kpn-node-network-reference-statement',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown
      *ngIf="summaryStatement == 'role-connection'"
      i18n="@@node.network.role-connection"
    >
      This node has the role _"connection"_ in the networkrelation.
    </markdown>

    <p
      *ngIf="summaryStatement == 'network-member'"
      i18n="@@node.network.network-member"
    >
      This node is included as member in the networkrelation.
    </p>

    <p
      *ngIf="summaryStatement == 'not-network-member'"
      i18n="@@node.network.not-network-member"
    >
      This node is not included as member in the networkrelation.
    </p>
  `,
})
export class NodeNetworkReferenceStatementComponent implements OnInit {
  @Input() nodeInfo: NodeInfo;
  @Input() reference: NodeNetworkReference;

  summaryStatement: string;

  ngOnInit(): void {
    let statement = '';
    if (this.reference.nodeRoleConnection) {
      statement = 'role-connection';
    } else if (this.reference.nodeDefinedInRelation) {
      statement = 'network-member';
    } else {
      statement = 'not-network-member';
    }

    this.summaryStatement = statement;
  }

  get expectedRouteRelationsTag() {
    const tagValue = NetworkTypes.tagValue(this.reference.networkType);
    return `expected_${tagValue}_route_relations`;
  }
}
