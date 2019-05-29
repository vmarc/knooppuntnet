import {Component, Input} from "@angular/core";
import {ChangeSetSubsetElementRefs} from "../../../../kpn/shared/change-set-subset-element-refs";

@Component({
  selector: "kpn-change-set-orphan-routes",
  template: `
    <div class="kpn-line">
      <span>{{domain()}}</span>
      <kpn-network-type-icon [networkType]="networkType()"></kpn-network-type-icon>
      <span>Orphan route(s)</span>
    </div>
    <kpn-change-set-element-refs [elementType]="'route'" [changeSetElementRefs]="subsetElementRefs.elementRefs"></kpn-change-set-element-refs>
  `
})
export class ChangesSetOrphanRoutesComponent {

  @Input() subsetElementRefs: ChangeSetSubsetElementRefs;

  domain() {
    if (this.subsetElementRefs.subset.country) {
      return this.subsetElementRefs.subset.country.domain.toUpperCase();
    }
    return "??country??";
  }

  networkType() {
    return this.subsetElementRefs.subset.networkType;
  }

}
