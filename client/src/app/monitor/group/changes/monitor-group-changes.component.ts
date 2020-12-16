import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    Monitor group changes
  `,
  styles: [`
  `]
})
export class MonitorGroupChangesComponent {
}
