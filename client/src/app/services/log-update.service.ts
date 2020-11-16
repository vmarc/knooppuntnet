import {Injectable} from '@angular/core';
import {SwUpdate} from '@angular/service-worker';

@Injectable()
export class LogUpdateService {

  constructor(updates: SwUpdate) {
    updates.available.subscribe(event => {
      console.log(`LogUpdateService: current version is ${event.current}, available version is ${event.available}`);
    });
    updates.activated.subscribe(event => {
      console.log(`LogUpdateService: old version was ${event.previous}, new version is ${event.current}`);
    });
  }
}
