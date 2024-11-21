import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatLabel } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatListItem } from '@angular/material/list';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-menu-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-list-item [routerLink]="link">
      <div class="item">
        <button mat-icon-button>
          <mat-icon>{{ icon }}</mat-icon>
        </button>
        <mat-label>
          <a>{{ label }}</a>
        </mat-label>
      </div>
    </mat-list-item>
  `,
  styles: [
    `
      .item {
        display: flex;
        align-items: center;
      }
    `,
  ],
  standalone: true,
  imports: [MatIcon, MatIconButton, MatListItem, RouterLink, MatLabel],
})
export class MenuItemComponent {
  @Input() label: string;
  @Input() icon: string;
  @Input() link: string;
}
