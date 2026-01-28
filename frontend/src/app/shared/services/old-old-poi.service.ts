import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { OldOldInterpretedPoiConfiguration } from '@app/ol/domain/old-old-interpreted-poi-configuration';
import { Map } from 'immutable';
import { BehaviorSubject } from 'rxjs';
import { ApiService } from './api.service';
import { BrowserStorageService } from './browser-storage.service';
import { OldOldPoiNameService } from './old-old-poi-name.service';
import { OldOldPoiGroupPreference } from './old-old-poi-preferences';
import { OldOldPoiPreference } from './old-old-poi-preferences';
import { OldOldPoiPreferences } from './old-old-poi-preferences';

@Injectable()
export class OldOldPoiService {
  private readonly apiService = inject(ApiService);
  private readonly poiNameService = inject(OldOldPoiNameService);
  private readonly browserStorageService = inject(BrowserStorageService);

  readonly changeCount: BehaviorSubject<number> = new BehaviorSubject(0);
  poiActive = Map<string, boolean>();
  readonly poiConfiguration: BehaviorSubject<OldOldInterpretedPoiConfiguration> =
    new BehaviorSubject(null);
  private zoomLevel: number;
  private poiPreferences: OldOldPoiPreferences;
  private readonly poiNames: Map<string, string> = this.poiNameService.buildPoiNames();

  constructor() {
    this.loadPoiConfiguration();
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
    } else {
      const json = this.browserStorageService.get('poi-config');
      if (json !== null) {
        const pref = OldOldPoiPreferences.fromJSON(JSON.parse(json));
        return pref.enabled;
      }
    }
    return false;
  }

  updateEnabled(enabled: boolean): void {
    if (this.poiPreferences != null) {
      this.poiPreferences.enabled = enabled;
      this.savePoiConfig();
      this.updatePoiActive();
    }
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
        const poiDefinition = this.poiConfiguration.value.poiDefinitionWithName(poiName);
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
        this.changeCount.next(this.changeCount.value + 1);
      }
    }
  }

  private loadPoiConfiguration() {
    this.apiService.poiConfiguration().subscribe((response) => {
      this.poiConfiguration.next(new OldOldInterpretedPoiConfiguration(response.result));
      this.initPoiConfig();
      this.updatePoiActive();
    });
  }

  private updateGroup(
    groupName: string,
    action: (groupPreference: OldOldPoiGroupPreference) => void
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
      this.poiPreferences = OldOldPoiPreferences.fromJSON(JSON.parse(json));
      // TODO make sure that changes to poi and poi group definitions are taken into account (work with configuration versions?)
    } else {
      const groupEntries: Array<[string, OldOldPoiGroupPreference]> = [];
      this.poiConfiguration.value.getGroupDefinitions().forEach((groupDefinition) => {
        const poiEntries: Array<[string, OldOldPoiPreference]> = [];
        groupDefinition.poiDefinitions.forEach((poiDefinition) => {
          poiEntries.push([
            poiDefinition.name,
            new OldOldPoiPreference(poiDefinition.defaultLevel),
          ]);
        });
        const pois = Map<string, OldOldPoiPreference>(poiEntries);
        groupEntries.push([
          groupDefinition.name,
          new OldOldPoiGroupPreference(groupDefinition.enabledDefault, pois),
        ]);
      });
      const groups = Map<string, OldOldPoiGroupPreference>(groupEntries);
      this.poiPreferences = new OldOldPoiPreferences(groups, false);
    }
  }

  private savePoiConfig() {
    this.browserStorageService.set('poi-config', JSON.stringify(this.poiPreferences));
  }
}
