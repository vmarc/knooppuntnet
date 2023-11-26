import { Injectable } from '@angular/core';
import { UnrecoverableStateEvent } from '@angular/service-worker';
import { VersionEvent } from '@angular/service-worker';
import { SwUpdate } from '@angular/service-worker';

@Injectable()
export class LogUpdateService {
  constructor(updates: SwUpdate) {
    updates.versionUpdates.subscribe((event: VersionEvent) => {
      console.log(`LogUpdateService: event ${event.type}`);
    });
    updates.unrecoverable.subscribe((event: UnrecoverableStateEvent) => {
      console.log(
        `LogUpdateService: UnrecoverableStateEvent, reason:  ${event.reason}`
      );
    });
  }
}
