import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<p style="padding: 2em">Map component</p>`,
  standalone: true,
  imports: [],
})
export class MapComponent {
  constructor() {
    console.log('MapComponent constructor');
  }
}
