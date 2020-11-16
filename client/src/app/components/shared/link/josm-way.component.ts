import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-josm-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<kpn-josm-link kind="way" [elementId]="wayId"></kpn-josm-link>`
})
export class JosmWayComponent {
  @Input() wayId: number;
}
