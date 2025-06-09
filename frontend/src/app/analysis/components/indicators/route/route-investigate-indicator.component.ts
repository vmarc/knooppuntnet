import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { IndicatorComponent } from '@app/shared/components/indicator/indicator.component';
import { RouteInvestigateIndicatorDialogComponent } from './route-investigate-indicator-dialog.component';

@Component({
  selector: 'ui-route-investigate-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-indicator
      letter="F"
      i18n-letter="@@route-investigate-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    />
  `,
  imports: [IndicatorComponent],
})
export class RouteInvestigateIndicatorComponent implements OnInit {
  readonly investigate = input.required<boolean>();

  private readonly dialog = inject(MatDialog);
  color: string;

  ngOnInit(): void {
    this.color = this.investigate() ? 'red' : 'green';
  }

  onOpenDialog() {
    this.dialog.open(RouteInvestigateIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
