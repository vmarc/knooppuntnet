import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NodeService } from '../node.service';

@Component({
  selector: 'ui-node-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page-header [pageTitle]="service.nodeName()" subject="node-page">
      <span i18n="@@node.title">Node</span>
      @if (service.nodeName()) {
        <span>&nbsp;{{ service.nodeName() }}</span>
      } @else {
        <span>&nbsp;{{ service.nodeId() }}</span>
      }
    </ui-page-header>

    <ui-page-menu>
      <ui-page-menu-option
        [link]="linkNodeDetails()"
        [active]="pageName() === 'details'"
        i18n="@@node.menu.details"
      >
        Details
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="linkNodeChanges()"
        [active]="pageName() === 'changes'"
        [elementCount]="service.changeCount()"
        i18n="@@node.menu.changes"
      >
        Changes
      </ui-page-menu-option>
    </ui-page-menu>
  `,
  imports: [PageHeaderComponent, PageMenuComponent, PageMenuOptionComponent],
})
export class NodePageHeaderComponent {
  pageName = input.required<string>();

  readonly service = inject(NodeService);

  linkNodeDetails(): string {
    return this.linkNode('');
  }

  linkNodeChanges(): string {
    return this.linkNode('/changes');
  }

  private linkNode(suffix: string): string {
    return `/analysis/node/${this.service.nodeId()}${suffix}`;
  }
}
