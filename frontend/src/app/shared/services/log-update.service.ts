import { Injectable } from '@angular/core';
import { NoNewVersionDetectedEvent } from "@angular/service-worker";
import { VersionReadyEvent } from "@angular/service-worker";
import { VersionInstallationFailedEvent } from "@angular/service-worker";
import { VersionDetectedEvent } from "@angular/service-worker";
import { UnrecoverableStateEvent } from "@angular/service-worker";
import { VersionEvent } from "@angular/service-worker";
import { SwUpdate } from '@angular/service-worker';
import { Observable } from "rxjs";

@Injectable()
export class LogUpdateService {
  constructor(updates: SwUpdate) {
    updates.versionUpdates.subscribe((event: VersionEvent) => {
      console.log(
        `LogUpdateService: event ${event.type}`
      );
    });
    updates.unrecoverable.subscribe((event: UnrecoverableStateEvent) => {
      console.log(
        `LogUpdateService: UnrecoverableStateEvent, reason:  ${event.reason}`
      );
    });
  }
}
