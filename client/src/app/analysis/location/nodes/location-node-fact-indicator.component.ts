import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { LocationNodeFactIndicatorDialogComponent } from './location-node-fact-indicator-dialog.component';

@Component({
  selector: 'kpn-location-node-fact-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="F"
      i18n-letter="@@location-node-fact-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class LocationNodeFactIndicatorComponent implements OnInit {
  @Input() node: LocationNodeInfo;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.determineColor();
  }

  onOpenDialog() {
    this.dialog.open(LocationNodeFactIndicatorDialogComponent, {
      data: this.color,
      maxWidth: 600,
    });
  }

  private determineColor() {
    let color;
    if (this.node.factCount > 0) {
      color = 'red';
    } else {
      color = 'green';
    }
    return color;
  }
}
