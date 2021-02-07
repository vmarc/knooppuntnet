import {Component} from '@angular/core';
import {Input} from '@angular/core';

@Component({
  selector: 'app-badge',
  template: `
    <nz-badge
      nzStandalone
      [nzCount]="count"
      [nzOverflowCount]="999999"
      [nzStyle]="{ backgroundColor: '#fff', color: '#999', boxShadow: '0 0 0 1px #d9d9d9 inset' }">
    </nz-badge>
  `,
  styles: [`
    nz-badge {
      padding-left: 1em;
    }
  `]
})
export class BadgeComponent {
  @Input() count: number;
}
