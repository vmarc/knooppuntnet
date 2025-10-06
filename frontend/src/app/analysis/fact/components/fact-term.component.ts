import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Fact } from '@api/common/fact';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { Facts } from '@app/analysis/fact/components/facts';
import { NzPopoverDirective } from 'ng-zorro-antd/popover';

@Component({
  selector: 'ui-fact-term',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      nz-popover
      [class]="color()"
      [nzPopoverTitle]="titleTemplate"
      [nzPopoverContent]="contentTemplate"
    >
      <span class="tooltip-link">
        <ui-fact-name [fact]="fact()" />
      </span>
    </span>
    <ng-template #titleTemplate>
      <b>{{ fact() }}</b>
    </ng-template>
    <ng-template #contentTemplate>
      <div class="tooltip-content">
        <ui-fact-description [fact]="fact()" />
      </div>
    </ng-template>
  `,
  imports: [FactNameComponent, FactDescriptionComponent, NzPopoverDirective],
})
export class FactTermComponent {
  readonly fact = input.required<Fact>();
  readonly color = computed(() => `color-${Facts.factLevel(this.fact())}`);
}
