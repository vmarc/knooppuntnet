import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-data',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="data">
      <div class="title">
        {{ title }}
      </div>
      <div class="body">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styleUrls: ['./data.component.scss'],
})
export class DataComponent {
  @Input() title: string;
}
