import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';
import { NodeService } from '../node.service';

@Component({
  selector: 'kpn-node-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header [pageTitle]="service.nodeName()" subject="node-page">
      <span i18n="@@node.title">Node</span>
      @if (service.nodeName()) {
        <span>&nbsp;{{ service.nodeName() }}</span>
      } @else {
        <span>&nbsp;{{ service.nodeId() }}</span>
      }
    </kpn-page-header>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="linkNodeDetails()"
        [active]="pageName() === 'details'"
        i18n="@@node.menu.details"
      >
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeMap()"
        [active]="pageName() === 'map'"
        i18n="@@node.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeChanges()"
        [active]="pageName() === 'changes'"
        [elementCount]="service.changeCount()"
        i18n="@@node.menu.changes"
      >
        Changes
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
  standalone: true,
  imports: [PageHeaderComponent, PageMenuComponent, PageMenuOptionComponent],
})
export class NodePageHeaderComponent {
  pageName = input.required<string>();

  readonly service = inject(NodeService);

  linkNodeDetails(): string {
    return this.linkNode('');
  }

  linkNodeMap(): string {
    return this.linkNode('/map');
  }

  linkNodeChanges(): string {
    return this.linkNode('/changes');
  }

  private linkNode(suffix: string): string {
    return `/analysis/node/${this.service.nodeId()}${suffix}`;
  }
}
