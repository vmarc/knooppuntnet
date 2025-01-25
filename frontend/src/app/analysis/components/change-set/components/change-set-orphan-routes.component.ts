import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetSubsetElementRefs } from '@api/common';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { ChangesSetElementRefsComponent } from './change-set-element-refs.component';

@Component({
  selector: 'kpn-change-set-orphan-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <span>{{ domain() }}</span>
      <nz-icon nzType="routeType()" />
      <span i18n="@@change-set.orphan-routes">Free route(s)</span>
    </div>
    <kpn-change-set-element-refs
      [elementType]="'route'"
      [changeSetElementRefs]="subsetElementRefs().elementRefs"
    />
  `,
  imports: [ChangesSetElementRefsComponent, NzIconDirective],
})
export class ChangesSetOrphanRoutesComponent {
  subsetElementRefs = input.required<ChangeSetSubsetElementRefs>();

  domain() {
    if (this.subsetElementRefs().subset.country) {
      return this.subsetElementRefs().subset.country.toUpperCase();
    }
    return '??country??';
  }

  routeType() {
    return this.subsetElementRefs().subset.routeType;
  }
}
