import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
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
      <button mat-stroked-button (click)="action.emit()" [disabled]="!enabled()" [title]="title()">
        <mat-icon [svgIcon]="icon()" />
        <span class="button-text">{{ text() }}</span>
      </button>
    } @else {
      <button mat-icon-button (click)="action.emit()" [disabled]="!enabled()" [title]="title()">
        <mat-icon [svgIcon]="icon()" />
      </button>
    }
  `,
  styles: `
    .button-text {
      padding-left: 10px;
    }

    button {
      margin-right: 10px;
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
  @Output() action = new EventEmitter<any>();

  private readonly pageWidthService = inject(PageWidthService);
  protected showButtonText = computed(
    () => this.pageWidthService.isVeryLarge() || this.pageWidthService.isLarge()
  );
}
