import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { JosmLinkComponent } from './josm-link.component';

@Component({
  selector: 'kpn-josm-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<kpn-josm-link
    kind="relation"
    [elementId]="relationId"
    [full]="true"
  />`,
  standalone: true,
  imports: [JosmLinkComponent],
})
export class JosmRelationComponent {
  @Input({ required: true }) relationId: number;
}
