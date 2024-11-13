import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button routerLink="/">
      <mat-icon svgIcon="back" />
    </button>
    Analysis
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton, RouterLink],
})
export class AnalysisComponent {}
