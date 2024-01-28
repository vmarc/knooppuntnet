import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { JosmLinkComponent } from './josm-link.component';

@Component({
  selector: 'kpn-josm-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<kpn-josm-link kind="node" [elementId]="nodeId()" />`,
  standalone: true,
  imports: [JosmLinkComponent],
})
export class JosmNodeComponent {
  nodeId = input.required<number>();
}
