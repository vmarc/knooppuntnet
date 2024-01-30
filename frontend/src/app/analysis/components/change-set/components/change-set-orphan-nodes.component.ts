import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetSubsetElementRefs } from '@api/common';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { ChangesSetElementRefsComponent } from './change-set-element-refs.component';

@Component({
  selector: 'kpn-change-set-orphan-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    <div class="kpn-line">
      <span>{{ domain() }}</span>
      <span><kpn-network-type-icon [networkType]="networkType()" /></span>
      <span i18n="@@change-set.orphan-nodes">Orphan node(s)</span>
    </div>
    <kpn-change-set-element-refs
      [elementType]="'node'"
      [changeSetElementRefs]="subsetElementRefs().elementRefs"
    />
  `,
  standalone: true,
  imports: [NetworkTypeIconComponent, ChangesSetElementRefsComponent],
})
export class ChangesSetOrphanNodesComponent {
  subsetElementRefs = input.required<ChangeSetSubsetElementRefs>();

  domain() {
    if (this.subsetElementRefs().subset.country) {
      return this.subsetElementRefs().subset.country.toUpperCase();
    }
    return '';
  }

  networkType() {
    return this.subsetElementRefs().subset.networkType;
  }
}
