import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatTooltipModule } from "@angular/material/tooltip";
import { ShowIfTruncatedDirective } from "./show-if-truncated.directive";

@Component({
  selector: 'kpn-ellipsis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div #textContent class="kpn-ellipsis" matTooltip="text" showIfTruncated>
      <ng-content />
    </div>
  `,
  standalone: true,
  imports: [
    MatTooltipModule,
    ShowIfTruncatedDirective
  ]
})
export class EllipsisComponent {
}
