import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { BrowserStorageService } from '@app/shared/services/browser-storage.service';
import { ApiService } from '@app/shared/services/api.service';
import { State } from '@app/state/state';
import { BehaviorSubject } from 'rxjs';
import { InterpretedPoiConfiguration } from '../../state/poi/interpreted-poi-configuration';
import { PoiGroupPreference } from '../../state/poi/poi-group-preference';
import { PoiNameTranslations } from '../../state/poi/poi-name-translations';
import { PoiPreference } from '../../state/poi/poi-preference';
import { PoiPreferences } from '../../state/poi/poi-preferences';
import { PoiStyleMap } from '../style/poi-style-map';

@Injectable()
export class PoiService {
  private readonly apiService = inject(ApiService);
  private readonly browserStorageService = inject(BrowserStorageService);
  private readonly state = inject(State);

  readonly changeCount: BehaviorSubject<number> = new BehaviorSubject(0);
  readonly poiConfiguration: BehaviorSubject<InterpretedPoiConfiguration> = new BehaviorSubject(
    null
  );
  private poiPreferences: PoiPreferences;
  private readonly poiNames: ReadonlyMap<string, string> = PoiNameTranslations.nameMap;

  constructor() {
    this.loadPoiConfiguration();
  }

  name(poiId: string): string {
    return this.poiNames.get(poiId);
  }

  isPoiActive(poiId: string): boolean {
    let active = this.state.map.poiActive().get(poiId);
    if (active === undefined) {
      active = false;
    }
    return active;
  }

  // updateZoomLevel(zoomLevel: number): void {
  //   this.zoomLevel = zoomLevel;
  //   this.updatePoiActive();
  // }

  isEnabled(): boolean {
    if (this.poiPreferences != null) {
      return this.poiPreferences.enabled;
    } else {
      const json = this.browserStorageService.get('poi-config');
      if (json !== null) {
        const pref = PoiPreferences.fromJSON(JSON.parse(json));
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
    console.log('updatePoiActive()', this.state.map.zoom(), this.poiPreferences);
    if (this.state.map.zoom() != null && this.poiPreferences != null) {
      let activeChanged = false;
      this.poiPreferences.groups.forEach((group, groupName) => {
        group.pois.forEach((poi, poiName) => {
          const active =
            this.poiPreferences.enabled &&
            group.enabled &&
            poi.minLevel !== 0 &&
            poi.minLevel <= this.state.map.zoom();
          if (this.state.map.poiActive().get(poiName) !== active) {
            const entries = Array.from(this.state.map.poiActive());
            entries.push([poiName, active]);
            const newMap = new Map(entries);
            this.state.map.updatePoiActive(newMap); // TODO redesign - do update only once, not inside this loop!!!
            activeChanged = true;
          }
        });
      });

      if (activeChanged) {
        this.changeCount.next(this.changeCount.value + 1);
      }
    }
    console.log(
      'updatePoiActive() DONE',
      this.state.map.zoom(),
      this.poiPreferences,
      this.state.map.poiActive()
    );
  }

  private loadPoiConfiguration() {
    console.log('load poiConfiguration');
    this.apiService.poiConfiguration().subscribe((response) => {
      console.log('poiConfiguration loaded', response);
      this.poiConfiguration.next(new InterpretedPoiConfiguration(response.result));
      this.initPoiConfig();
      this.updatePoiActive();
      this.state.map.updatePoiStyleMap(
        new PoiStyleMap(new InterpretedPoiConfiguration(response.result))
      );
      console.log('poiConfiguration', this.poiConfiguration);
    });
  }

  private updateGroup(groupName: string, action: (groupPreference: PoiGroupPreference) => void) {
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
      this.poiConfiguration.value.getGroupDefinitions().forEach((groupDefinition) => {
        const poiEntries: Array<[string, PoiPreference]> = [];
        groupDefinition.poiDefinitions.forEach((poiDefinition) => {
          poiEntries.push([poiDefinition.name, new PoiPreference(poiDefinition.defaultLevel)]);
        });
        const pois = new Map<string, PoiPreference>(poiEntries);
        groupEntries.push([
          groupDefinition.name,
          new PoiGroupPreference(groupDefinition.enabledDefault, pois),
        ]);
      });
      const groups = new Map<string, PoiGroupPreference>(groupEntries);
      this.poiPreferences = new PoiPreferences(groups, true); // TODO redesign - make default false again
    }
  }

  private savePoiConfig() {
    this.browserStorageService.set('poi-config', JSON.stringify(this.poiPreferences));
  }
}
