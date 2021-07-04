import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RouteInvestigateIndicatorDialogComponent } from './route-investigate-indicator-dialog.component';

@Component({
  selector: 'kpn-route-investigate-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="F"
      i18n-letter="@@route-investigate-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class RouteInvestigateIndicatorComponent implements OnInit {
  @Input() investigate: boolean;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.investigate ? 'red' : 'green';
  }

  onOpenDialog() {
    this.dialog.open(RouteInvestigateIndicatorDialogComponent, {
      data: this.color,
      maxWidth: 600,
    });
  }
}
