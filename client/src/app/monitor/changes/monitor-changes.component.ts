import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-monitor-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    Monitor changes
  `,
  styles: [`
  `]
})
export class MonitorChangesComponent {
}
