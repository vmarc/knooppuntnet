import { output } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { PageWidthService } from '@app/components/shared';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'kpn-plan-action-button',
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
  imports: [MatButtonModule, MatIconModule, NzIconDirective, NzButtonComponent],
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
