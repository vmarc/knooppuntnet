import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetSubsetElementRefs } from '@api/common/change-set-subset-element-refs';

@Component({
  selector: 'kpn-change-set-orphan-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <span>{{ domain() }}</span>
      <span
        ><kpn-network-type-icon
          [networkType]="networkType()"
        ></kpn-network-type-icon
      ></span>
      <span i18n="@@change-set.orphan-nodes">Orphan node(s)</span>
    </div>
    <kpn-change-set-element-refs
      [elementType]="'node'"
      [changeSetElementRefs]="subsetElementRefs.elementRefs"
    >
    </kpn-change-set-element-refs>
  `,
})
export class ChangesSetOrphanNodesComponent {
  @Input() subsetElementRefs: ChangeSetSubsetElementRefs;

  domain() {
    if (this.subsetElementRefs.subset.country) {
      return this.subsetElementRefs.subset.country.domain.toUpperCase();
    }
    return '';
  }

  networkType() {
    return this.subsetElementRefs.subset.networkType;
  }
}
