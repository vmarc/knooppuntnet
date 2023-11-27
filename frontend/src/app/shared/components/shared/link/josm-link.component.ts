import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApiService } from '@app/services';
import { TimeoutComponent } from './timeout.component';

@Component({
  selector: 'kpn-josm-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      rel="nofollow"
      (click)="edit()"
      title="Open in editor (like JOSM)"
      i18n-title="@@edit.link.title"
      i18n="@@edit.link"
      >edit</a
    >
  `,
  standalone: true,
})
export class JosmLinkComponent {
  @Input({ required: true }) kind: string;
  @Input({ required: true }) elementId: number;
  @Input({ required: false }) full = false;

  constructor(
    private apiService: ApiService,
    private dialog: MatDialog
  ) {}

  edit(): void {
    const url = 'http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6';
    const fullUrl = `${url}/${this.kind}/${this.elementId}${this.full ? '/full' : ''}`;
    this.apiService.edit(fullUrl).subscribe({
      next: (result) => {},
      error: (err) => {
        this.dialog.open(TimeoutComponent, { autoFocus: false, maxWidth: 500 });
      },
    });
  }
}
