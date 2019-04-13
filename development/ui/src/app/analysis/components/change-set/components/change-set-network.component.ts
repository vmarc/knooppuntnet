import {Component, Input} from "@angular/core";
import {ChangeSetNetwork} from "../../../../kpn/shared/change-set-network";

export class ChangeSetNetworkAction {
  constructor(readonly action: string,
              readonly network: ChangeSetNetwork) {
  }
}

@Component({
  selector: "kpn-change-set-network",
  template: `
    <div class="kpn-line">
      <span>{{domain()}}</span>
      <kpn-network-type-icon [networkType]="changeSetNetworkAction.network.networkType"></kpn-network-type-icon>
      <span>{{changeSetNetworkAction.action}}</span>
      <a>{{changeSetNetworkAction.network.networkName}}</a>
    </div>
    <kpn-change-set-element-refs elementType="node" [changeSetElementRefs]="nodeChanges()"></kpn-change-set-element-refs>
    <kpn-change-set-element-refs elementType="route" [changeSetElementRefs]="routeChanges()"></kpn-change-set-element-refs>
  `
})
export class ChangesSetNetworkComponent {

  @Input() changeSetNetworkAction: ChangeSetNetworkAction;

  domain() {
    if (this.changeSetNetworkAction.network.country) {
      return this.changeSetNetworkAction.network.country.domain.toUpperCase();
    }
    return "??";
  }

  nodeChanges() {
    return this.changeSetNetworkAction.network.nodeChanges;
  }

  routeChanges() {
    return this.changeSetNetworkAction.network.routeChanges;
  }

}
