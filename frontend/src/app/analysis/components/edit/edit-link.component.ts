import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { EditService } from './edit.service';

@Component({
  selector: 'kpn-edit-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a rel="nofollow" (click)="clicked()" title="{{ linkTitle }}" i18n="@@edit.link">edit</a>
  `,
  providers: [EditService],
  standalone: true,
})
export class EditLinkComponent implements OnInit {
  title = input.required<string>();
  @Output() edit = new EventEmitter<void>();

  linkTitle: string;

  ngOnInit(): void {
    if (this.title()) {
      this.linkTitle = this.title();
    } else {
      this.linkTitle = $localize`:@@edit.link.title:Open in editor (like JOSM)`;
    }
  }

  clicked(): void {
    this.edit.emit();
  }
}
