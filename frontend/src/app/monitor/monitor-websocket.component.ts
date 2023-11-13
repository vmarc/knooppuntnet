import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { from } from 'rxjs';
import { MonitorWebsocketUploadComponent } from './monitor-websocket-upload.component';

@Component({
  selector: 'kpn-monitor-websocket',
  template: `
    <form [formGroup]="referenceDetailsForm" (ngSubmit)="submit()">
      <div class="padding">
        <input type="file" (change)="selectFile($event)" #fileInput />
      </div>
      <div class="padding">
        <button type="submit">Submit</button>
      </div>
    </form>

    <div *ngIf="data">
      <kpn-monitor-websocket-upload [data]="data" />
    </div>
  `,
  styles: [
    `
      .padding {
        padding: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [
    NgFor,
    NgIf,
    MatButtonModule,
    ReactiveFormsModule,
    MonitorWebsocketUploadComponent,
  ],
})
export class MonitorWebsocketComponent {
  data: any;

  readonly referenceFilename = new FormControl<string>(null);
  readonly referenceFile = new FormControl<File>(null);
  readonly referenceDetailsForm = new FormGroup({
    referenceFilename: this.referenceFilename,
    referenceFile: this.referenceFile,
  });

  selectFile(selectEvent: any) {
    this.referenceFile.setValue(selectEvent.target.files[0]);
    this.referenceFilename.setValue(selectEvent.target.files[0].name);
  }

  submit() {
    const file = this.referenceFile.value;

    const promise = file.text();
    console.log(`Send file ${file.name}, size=${file.size}`);
    from(promise).subscribe((buffer) => {
      const data = {
        action: 'add-route',
        filename: this.referenceFilename.value,
        file: buffer,
      };

      console.log(`Send file ${file.name}, size=${file.size}`);
      this.data = data;
    });
  }
}
