import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';

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
  standalone: true,
  imports: [RouterLink],
})
export class LinkNodeComponent {
  @Input() nodeId: number;
  @Input() nodeName: string;
}
