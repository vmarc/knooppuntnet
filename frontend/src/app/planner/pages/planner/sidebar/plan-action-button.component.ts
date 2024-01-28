import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { PageWidth } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { map } from 'rxjs/operators';

@Component({
  selector: 'kpn-plan-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (showButtonText$ | async) {
      <button mat-stroked-button (click)="action.emit()" [disabled]="!enabled()" [title]="title()">
        <mat-icon [svgIcon]="icon()" />
        <span class="button-text()">{{ text() }}</span>
      </button>
    }

    @if (showButtonIcon$ | async) {
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

    button > mat-icon {
      height: 18px;
      line-height: 18px;
    }
  `,
  standalone: true,
  imports: [MatButtonModule, MatIconModule, AsyncPipe],
})
export class PlanActionButtonComponent {
  enabled = input(false);
  icon = input('');
  text = input('');
  title = input('');
  @Output() action = new EventEmitter<any>();

  private readonly pageWidthService = inject(PageWidthService);

  protected showButtonText$ = this.pageWidthService.current$.pipe(
    map((pageWidth) => pageWidth === PageWidth.veryLarge || pageWidth === PageWidth.large)
  );
  protected showButtonIcon$ = this.showButtonText$.pipe(map((enabled) => !enabled));
}
