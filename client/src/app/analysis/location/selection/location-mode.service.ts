import { Injectable } from '@angular/core';
import { BrowserStorageService } from '@app/services';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class LocationModeService {
  isModeName: Observable<boolean>;
  isModeTree: Observable<boolean>;
  private readonly localStorageKey = 'location-mode';
  private readonly modeSubject: BehaviorSubject<string>;

  constructor(private browserStorageService: BrowserStorageService) {
    let initialMode = browserStorageService.get(this.localStorageKey);
    if (initialMode === null) {
      browserStorageService.set(this.localStorageKey, 'name');
      initialMode = 'name';
    }
    this.modeSubject = new BehaviorSubject(initialMode);
    this.isModeName = this.modeSubject.pipe(map((e) => e === 'name'));
    this.isModeTree = this.modeSubject.pipe(map((e) => e === 'tree'));
  }

  mode(): Observable<string> {
    return this.modeSubject;
  }

  currentMode(): string {
    return this.modeSubject.getValue();
  }

  setMode(mode: string) {
    this.browserStorageService.set(this.localStorageKey, mode);
    this.modeSubject.next(mode);
  }
}
