import { NgFor } from '@angular/common';
import { DOCUMENT } from '@angular/common';
import { Input } from '@angular/core';
import { Inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { WebSocketSubject } from 'rxjs/webSocket';
import { webSocket } from 'rxjs/webSocket';

@Component({
  selector: 'kpn-monitor-websocket-upload',
  template: `
    <div class="messages">
      <p>Messages received via websocket:</p>
      <pre *ngFor="let message of messages">  {{ message }}</pre>
    </div>
  `,
  styles: [
    `
      .messages {
        padding: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [NgFor],
})
export class MonitorWebsocketUploadComponent implements OnInit {
  @Input() data;

  messages: string[] = [];
  subject: WebSocketSubject<any>;

  constructor(@Inject(DOCUMENT) private document) {}

  ngOnInit() {
    let protocol = 'wss';
    if (document.location.protocol === 'http:') {
      protocol = 'ws';
    }
    const host = document.location.host;
    const url = `${protocol}://${host}/websocket`;

    this.subject = webSocket<any>({
      url,
      // serializer: (x) => {
      //   console.log(['websocket serializer', x]);
      //   return x;
      // },
      deserializer: (messageEvent) => {
        return messageEvent.data;
      },
      openObserver: {
        next: () => {
          console.log('websocket connection open');
        },
      },
    });
    this.subject.subscribe({
      next: (msg) => {
        console.log('websocket message received: ' + msg);
        this.messages.push(msg);
      },
      error: (err) => {
        console.log(['websocket error', err]);
      },
      complete: () => {
        console.log('websocket complete');
      },
    });
    this.subject.next(this.data);
  }
}
