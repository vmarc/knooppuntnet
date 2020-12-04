import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-long-distance-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <h1>
      Long distance routes
    </h1>

    <kpn-long-distance-routes-table></kpn-long-distance-routes-table>

  `,
  styles: [`
  `]
})
export class LongDistanceRoutesComponent {
}
