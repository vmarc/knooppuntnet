import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { JosmLinkComponent } from './josm-link.component';

@Component({
  selector: 'kpn-josm-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<kpn-josm-link kind="way" [elementId]="wayId()" />`,
  standalone: true,
  imports: [JosmLinkComponent],
})
export class JosmWayComponent {
  wayId = input.required<number>();
}
