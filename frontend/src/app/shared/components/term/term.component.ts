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
      [nzPopoverTitle]="title()"
      [nzPopoverContent]="contentTemplate"
      >{{ title() }}</span
    >
    <ng-template #contentTemplate>
      <div class="tooltip-content">
        <markdown>
          <ng-content />
        </markdown>
      </div>
    </ng-template>
  `,
  styles: `
    /* 'tooltip-link' is used in termStyle signal below */
    .tooltip-link {
      text-decoration: underline;
      text-decoration-style: dotted;
    }

    .tooltip-content {
      max-width: 40em;
      margin-top: 1em;
    }
  `,
  imports: [MarkdownComponent, NzPopoverDirective],
})
export class TermComponent {
  readonly level = input.required<FactLevel>();
  readonly title = input.required<string>();
  readonly termStyle = computed(() => `tooltip-link color-${this.level()}`);
}
