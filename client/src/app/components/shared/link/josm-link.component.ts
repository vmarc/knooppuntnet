import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppService } from '../../../app.service';
import { TimeoutComponent } from './timeout.component';

@Component({
  selector: 'kpn-josm-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      rel="nofollow"
      (click)="edit()"
      title="Open in editor (like JOSM)"
      i18n="@@links.edit"
      >edit</a
    >
  `,
})
export class JosmLinkComponent {
  @Input() kind: string;
  @Input() elementId: number;
  @Input() full = false;

  constructor(private appService: AppService, private dialog: MatDialog) {}

  edit(): void {
    const url =
      'http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6';
    const fullUrl = `${url}/${this.kind}/${this.elementId}${
      this.full ? '/full' : ''
    }`;
    this.appService.edit(fullUrl).subscribe(
      (result) => {},
      (err) => {
        this.dialog.open(TimeoutComponent, { maxWidth: 500 });
      }
    );
  }
}
