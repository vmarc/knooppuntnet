import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-josm-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<kpn-josm-link kind="node" [elementId]="nodeId"/>`,
})
export class JosmNodeComponent {
  @Input() nodeId: number;
}
