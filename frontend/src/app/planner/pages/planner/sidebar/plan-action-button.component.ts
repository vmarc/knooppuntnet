import { output } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { PageWidthService } from '@app/components/shared';

@Component({
  selector: 'kpn-plan-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (showButtonText()) {
      <button
        mat-stroked-button
        class="button-with-text"
        (click)="action.emit()"
        [disabled]="!enabled()"
        [title]="title()"
      >
        <mat-icon [svgIcon]="icon()" />
        <span>{{ text() }}</span>
      </button>
    } @else {
      <button mat-icon-button (click)="action.emit()" [disabled]="!enabled()" [title]="title()">
        <mat-icon [svgIcon]="icon()" />
      </button>
    }
  `,
  styles: `
    button {
      margin-right: 10px;
    }

    .button-with-text > mat-icon {
      height: 18px;
      line-height: 18px;
    }

    .button-with-text > span {
      padding-left: 10px;
    }
  `,
  standalone: true,
  imports: [MatButtonModule, MatIconModule],
})
export class PlanActionButtonComponent {
  enabled = input(false);
  icon = input('');
  text = input('');
  title = input('');
  action = output<void>();

  private readonly pageWidthService = inject(PageWidthService);
  protected showButtonText = computed(
    () => this.pageWidthService.isVeryLarge() || this.pageWidthService.isLarge()
  );
}
