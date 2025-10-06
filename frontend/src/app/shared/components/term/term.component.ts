import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FactLevel } from '@api/common/fact-level';
import { NzPopoverDirective } from 'ng-zorro-antd/popover';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-term',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      [class]="termStyle()"
      nz-popover
      [nzPopoverTitle]="titleTemplate"
      [nzPopoverContent]="contentTemplate"
    >
      <span class="tooltip-link">
        {{ name() }}
      </span>
    </span>
    <ng-template #titleTemplate>
      <b>{{ titleText() }}</b>
    </ng-template>
    <ng-template #contentTemplate>
      <div class="tooltip-content">
        <markdown>
          <ng-content />
        </markdown>
      </div>
    </ng-template>
  `,
  imports: [MarkdownComponent, NzPopoverDirective],
})
export class TermComponent {
  readonly level = input.required<FactLevel>();
  readonly name = input.required<string>();
  readonly title = input<string>();
  readonly termStyle = computed(() => `color-${this.level()}`);
  readonly titleText = computed(() => this.title() || this.name());
}
