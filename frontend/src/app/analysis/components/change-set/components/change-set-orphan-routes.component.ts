import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetSubsetElementRefs } from '@api/common';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { ChangesSetElementRefsComponent } from './change-set-element-refs.component';

@Component({
  selector: 'kpn-change-set-orphan-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    <div class="kpn-line">
      <span>{{ domain() }}</span>
      <kpn-network-type-icon [networkType]="networkType()" />
      <span i18n="@@change-set.orphan-routes">Free route(s)</span>
    </div>
    <kpn-change-set-element-refs
      [elementType]="'route'"
      [changeSetElementRefs]="subsetElementRefs().elementRefs"
    />
  `,
  standalone: true,
  imports: [NetworkTypeIconComponent, ChangesSetElementRefsComponent],
})
export class ChangesSetOrphanRoutesComponent {
  subsetElementRefs = input<ChangeSetSubsetElementRefs | undefined>();

  domain() {
    if (this.subsetElementRefs().subset.country) {
      return this.subsetElementRefs().subset.country.toUpperCase();
    }
    return '??country??';
  }

  networkType() {
    return this.subsetElementRefs().subset.networkType;
  }
}
