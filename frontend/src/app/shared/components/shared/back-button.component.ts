import { Location } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'kpn-back-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button (click)="back()">
      <mat-icon>arrow_back</mat-icon>
    </button>
  `,
  imports: [MatIcon, MatIconButton],
})
export class BackButtonComponent {
  private readonly location = inject(Location);

  back(): void {
    this.location.back();
  }
}
