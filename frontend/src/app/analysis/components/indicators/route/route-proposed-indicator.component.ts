import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { IndicatorComponent } from '@app/components/shared/indicator';
import { RouteProposedIndicatorDialogComponent } from './route-proposed-indicator-dialog.component';

@Component({
  selector: 'kpn-route-proposed-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="P"
      i18n-letter="@@route-proposed-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    />
  `,
  standalone: true,
  imports: [IndicatorComponent],
})
export class RouteProposedIndicatorComponent implements OnInit {
  @Input() proposed: boolean;

  private readonly dialog = inject(MatDialog);
  color: string;

  ngOnInit(): void {
    this.color = this.proposed ? 'blue' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(RouteProposedIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
