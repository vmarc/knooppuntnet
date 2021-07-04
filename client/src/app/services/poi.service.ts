import { Injectable } from '@angular/core';
import { Map } from 'immutable';
import { BehaviorSubject } from 'rxjs';
import { AppService } from '../app.service';
import { InterpretedPoiConfiguration } from '../components/ol/domain/interpreted-poi-configuration';
import { BrowserStorageService } from './browser-storage.service';
import {
  PoiGroupPreference,
  PoiPreference,
  PoiPreferences,
} from './poi-preferences';
import { Observable } from 'rxjs';

@Injectable()
export class PoiService {
  _enabled: BehaviorSubject<boolean>;
  enabled: Observable<boolean>;
  changed: BehaviorSubject<boolean> = new BehaviorSubject(null);
  poiActive = Map<string, boolean>();
  poiConfiguration: BehaviorSubject<InterpretedPoiConfiguration> =
    new BehaviorSubject(null);
  private zoomLevel: number;
  private poiPreferences: PoiPreferences;
  private poiNames: Map<string, string> = null;

  constructor(
    private appService: AppService,
    private browserStorageService: BrowserStorageService
  ) {
    this.loadPoiConfiguration();
    this._enabled = new BehaviorSubject<boolean>(this.isEnabled());
    this.enabled = this._enabled.asObservable();
  }

  updatePoiNameRegistry(poiElements: HTMLCollection) {
    if (this.poiNames === null) {
      const keysAndValues: Array<[string, string]> = [];
      Array.from(poiElements).forEach((span) => {
        const id = span.getAttribute('id');
        const translation = span.textContent;
        keysAndValues.push([id, translation]);
      });
      this.poiNames = Map<string, string>(keysAndValues);
    }
  }

  isPoiNameRegistryUpdated() {
    return this.poiNames !== null;
  }

  name(poiId: string): string {
    return this.poiNames.get(poiId);
  }

  isPoiActive(poiId: string): boolean {
    return this.poiActive.get(poiId, false);
  }

  updateZoomLevel(zoomLevel: number): void {
    this.zoomLevel = zoomLevel;
    this.updatePoiActive();
  }

  isEnabled(): boolean {
    if (this.poiPreferences != null) {
      return this.poiPreferences.enabled;
    }
    return false;
  }

  updateEnabled(enabled: boolean): void {
    if (this.poiPreferences != null) {
      this.poiPreferences.enabled = enabled;
      this.savePoiConfig();
      this.updatePoiActive();
    }
    this._enabled.next(enabled);
  }

  isGroupEnabled(groupName: string): boolean {
    if (this.poiPreferences != null) {
      const group = this.poiPreferences.groups.get(groupName);
      if (group != null) {
        return group.enabled;
      }
    }
    return false;
  }

  updateGroupEnabled(groupName: string, enabled: boolean): void {
    this.updateGroup(groupName, (group) => (group.enabled = enabled));
  }

  updateGroupShowAll(groupName: string) {
    this.updateGroup(groupName, (group) => {
      group.pois.forEach((poi, poiName) => {
        poi.minLevel = 11;
      });
    });
  }

  updateGroupHideAll(groupName: string) {
    this.updateGroup(groupName, (group) => {
      group.pois.forEach((poi, poiName) => {
        poi.minLevel = 0;
      });
    });
  }

  updateGroupDefault(groupName: string) {
    this.updateGroup(groupName, (group) => {
      group.pois.forEach((poi, poiName) => {
        const poiDefinition =
          this.poiConfiguration.value.poiDefinitionWithName(poiName);
        if (poiDefinition != null) {
          poi.minLevel = poiDefinition.minLevel;
        }
      });
    });
  }

  poiLevel(poiId: string): string {
    if (this.poiPreferences != null) {
      const poi = this.poiPreferences.poi(poiId);
      if (poi != null) {
        return '' + poi.minLevel;
      }
    }
    return null;
  }

  updatePoiLevel(poiId: string, minLevel: number): void {
    const poi = this.poiPreferences.poi(poiId);
    if (poi != null) {
      poi.minLevel = minLevel;
      this.savePoiConfig();
      this.updatePoiActive();
    }
  }

  updatePoiActive(): void {
    if (this.zoomLevel != null && this.poiPreferences != null) {
      let activeChanged = false;
      this.poiPreferences.groups.forEach((group, groupName) => {
        group.pois.forEach((poi, poiName) => {
          const active =
            this.poiPreferences.enabled &&
            group.enabled &&
            poi.minLevel !== 0 &&
            poi.minLevel <= this.zoomLevel;
          if (this.poiActive.get(poiName) !== active) {
            this.poiActive = this.poiActive.set(poiName, active);
            activeChanged = true;
          }
        });
      });

      if (activeChanged) {
        this.changed.next(true);
      }
    }
  }

  private loadPoiConfiguration() {
    this.appService.poiConfiguration().subscribe((response) => {
      this.poiConfiguration.next(
        new InterpretedPoiConfiguration(response.result)
      );
      this.initPoiConfig();
      this.updatePoiActive();
      this._enabled.next(this.isEnabled());
    });
  }

  private updateGroup(
    groupName: string,
    action: (groupPreference: PoiGroupPreference) => void
  ) {
    if (this.poiPreferences != null) {
      const groupPreference = this.poiPreferences.groups.get(groupName);
      if (groupPreference != null) {
        action(groupPreference);
        this.savePoiConfig();
        this.updatePoiActive();
      }
    }
  }

  private initPoiConfig() {
    const json = this.browserStorageService.get('poi-config');
    if (json !== null) {
      this.poiPreferences = PoiPreferences.fromJSON(JSON.parse(json));
      // TODO make sure that changes to poi and poi group definitions are taken into account (work with configuration versions?)
    } else {
      const groupEntries: Array<[string, PoiGroupPreference]> = [];
      this.poiConfiguration.value
        .getGroupDefinitions()
        .forEach((groupDefinition) => {
          const poiEntries: Array<[string, PoiPreference]> = [];
          groupDefinition.poiDefinitions.forEach((poiDefinition) => {
            poiEntries.push([
              poiDefinition.name,
              new PoiPreference(poiDefinition.defaultLevel),
            ]);
          });
          const pois = Map<string, PoiPreference>(poiEntries);
          groupEntries.push([
            groupDefinition.name,
            new PoiGroupPreference(groupDefinition.enabledDefault, pois),
          ]);
        });
      const groups = Map<string, PoiGroupPreference>(groupEntries);
      this.poiPreferences = new PoiPreferences(groups, false);
    }
  }

  private savePoiConfig() {
    this.browserStorageService.set(
      'poi-config',
      JSON.stringify(this.poiPreferences)
    );
  }
}
