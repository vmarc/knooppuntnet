import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="'/analysis/node/' + nodeId"
      [state]="{nodeName}"
      title="Open node page"
      i18n-title="@@link-node.title"
    >
      {{ nodeName }}
    </a>
  `,
})
export class LinkNodeComponent {
  @Input() nodeId: number;
  @Input() nodeName: string;
}
