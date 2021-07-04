import {ChangeDetectionStrategy} from '@angular/core';
import {OnInit} from '@angular/core';
import {Component, Input} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {RouteProposedIndicatorDialogComponent} from "@app/analysis/components/indicators/route/route-proposed-indicator-dialog.component";

@Component({
  selector: 'kpn-route-proposed-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="P"
      i18n-letter="@@route-proposed-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class RouteProposedIndicatorComponent implements OnInit {
  @Input() proposed: boolean;
  color: string;

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.color = this.proposed ? 'blue' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(RouteProposedIndicatorDialogComponent, {
      data: this.color,
      maxWidth: 600,
    });
  }
}
