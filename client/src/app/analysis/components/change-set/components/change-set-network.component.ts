import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetNetwork } from '@api/common/change-set-network';
import { ChangeKey } from '@api/common/changes/details/change-key';

export class ChangeSetNetworkAction {
  constructor(
    readonly changeKey: ChangeKey,
    readonly action: string,
    readonly network: ChangeSetNetwork
  ) {}
}

@Component({
  selector: 'kpn-change-set-network',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <span>{{ domain() }}</span>
      <kpn-network-type-icon
        [networkType]="changeSetNetworkAction.network.networkType"
      />
      <span>{{ changeSetNetworkAction.action }}</span>
      <a
        [routerLink]="link()"
        [fragment]="changeSetNetworkAction.network.networkId.toString()"
      >
        {{ changeSetNetworkAction.network.networkName }}
      </a>
    </div>
    <kpn-change-set-element-refs
      elementType="node"
      [changeSetElementRefs]="nodeChanges()"
    />
    <kpn-change-set-element-refs
      elementType="route"
      [changeSetElementRefs]="routeChanges()"
    />
  `,
})
export class ChangesSetNetworkComponent {
  @Input() changeSetNetworkAction: ChangeSetNetworkAction;

  domain() {
    if (this.changeSetNetworkAction.network.country) {
      return this.changeSetNetworkAction.network.country.toUpperCase();
    }
    return '??';
  }

  nodeChanges() {
    return this.changeSetNetworkAction.network.nodeChanges;
  }

  routeChanges() {
    return this.changeSetNetworkAction.network.routeChanges;
  }

  link(): string {
    const changeSetId = this.changeSetNetworkAction.changeKey.changeSetId;
    const replicationNumber =
      this.changeSetNetworkAction.changeKey.replicationNumber;
    return `/analysis/changeset/${changeSetId}/${replicationNumber}`;
  }
}
