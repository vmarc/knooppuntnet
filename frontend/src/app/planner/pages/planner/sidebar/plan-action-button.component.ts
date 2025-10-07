import { output } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-plan-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (showButtonText()) {
      <button
        nz-button
        class="button-with-text"
        (click)="action.emit()"
        [disabled]="!enabled()"
        [title]="title()"
      >
        <nz-icon [nzType]="icon()" />
        <span>{{ text() }}</span>
      </button>
    } @else {
      <button nz-button (click)="action.emit()" [disabled]="!enabled()" [title]="title()">
        <nz-icon [nzType]="icon()" />
      </button>
    }
  `,
  imports: [NzIconDirective, NzButtonComponent],
})
export class PlanActionButtonComponent {
  readonly enabled = input(false);
  readonly icon = input('');
  readonly text = input('');
  readonly title = input('');
  readonly action = output<void>();

  private readonly pageWidthService = inject(PageWidthService);
  protected showButtonText = computed(
    () => this.pageWidthService.isVeryLarge() || this.pageWidthService.isLarge()
  );
}
