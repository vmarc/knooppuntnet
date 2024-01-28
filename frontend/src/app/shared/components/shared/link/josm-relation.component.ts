import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { JosmLinkComponent } from './josm-link.component';

@Component({
  selector: 'kpn-josm-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<kpn-josm-link kind="relation" [elementId]="relationId()" [full]="true" />`,
  standalone: true,
  imports: [JosmLinkComponent],
})
export class JosmRelationComponent {
  relationId = input.required<number>();
}
