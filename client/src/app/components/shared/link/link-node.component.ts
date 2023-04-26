import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="['/analysis/node', nodeId]" [state]="{nodeName}">
      {{ nodeName }}
    </a>
  `,
})
export class LinkNodeComponent {
  @Input() nodeId: number;
  @Input() nodeName: string;
}
