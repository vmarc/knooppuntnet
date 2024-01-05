import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LocationNodeInfo } from '@api/common/location';
import { IndicatorComponent } from '@app/components/shared/indicator';
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
    />
  `,
  standalone: true,
  imports: [IndicatorComponent],
})
export class LocationNodeFactIndicatorComponent implements OnInit {
  @Input() node: LocationNodeInfo;

  private readonly dialog = inject(MatDialog);
  color: string;

  ngOnInit(): void {
    this.color = this.determineColor();
  }

  onOpenDialog() {
    this.dialog.open(LocationNodeFactIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }

  private determineColor() {
    let color;
    if (this.node.facts.length > 0) {
      color = 'red';
    } else {
      color = 'green';
    }
    return color;
  }
}
