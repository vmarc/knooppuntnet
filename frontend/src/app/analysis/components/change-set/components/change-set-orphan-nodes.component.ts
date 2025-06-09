import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetSubsetElementRefs } from '@api/common/change-set-subset-element-refs';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { ChangesSetElementRefsComponent } from './change-set-element-refs.component';

@Component({
  selector: 'ui-change-set-orphan-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <span>{{ domain() }}</span>
      <span><nz-icon [nzType]="routeType()" /></span>
      <span i18n="@@change-set.orphan-nodes">Orphan node(s)</span>
    </div>
    <ui-change-set-element-refs
      [elementType]="'node'"
      [changeSetElementRefs]="subsetElementRefs().elementRefs"
    />
  `,
  imports: [ChangesSetElementRefsComponent, NzIconDirective],
})
export class ChangesSetOrphanNodesComponent {
  readonly subsetElementRefs = input.required<ChangeSetSubsetElementRefs>();

  domain() {
    if (this.subsetElementRefs().subset.country) {
      return this.subsetElementRefs().subset.country.toUpperCase();
    }
    return '';
  }

  routeType() {
    return this.subsetElementRefs().subset.routeType;
  }
}
