import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { UnrecoverableStateEvent } from '@angular/service-worker';
import { VersionEvent } from '@angular/service-worker';
import { SwUpdate } from '@angular/service-worker';

@Injectable()
export class LogUpdateService {
  private readonly updates = inject(SwUpdate);

  constructor() {
    this.updates.versionUpdates.subscribe((event: VersionEvent) => {
      console.log(`LogUpdateService: event ${event.type}`);
    });
    this.updates.unrecoverable.subscribe((event: UnrecoverableStateEvent) => {
      console.log(`LogUpdateService: UnrecoverableStateEvent, reason:  ${event.reason}`);
    });
  }
}
