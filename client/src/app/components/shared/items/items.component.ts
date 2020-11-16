import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-items',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="items">
      <ng-content></ng-content>
    </div>
  `,
  styles: [`

    .items {
      margin-top: 20px;
      border-top-color: lightgray;
      border-top-style: solid;
      border-top-width: 1px;
    }

    @media (max-width: 768px) { /* media.maxWidth(PageWidth.SmallMaxWidth.px) */
      .items {
        margin-left: -20px;
        margin-right: -20px;
      }
    }

  `]
})
export class ItemsComponent {
}
