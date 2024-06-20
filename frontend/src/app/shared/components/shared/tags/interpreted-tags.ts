import { Tag } from '@api/custom';
import { NetworkScopes } from '@app/kpn/common';
import { NetworkTypes } from '@app/kpn/common';
import { List } from 'immutable';

export class InterpretedTags {
  static nodeTags(tags: Tag[]): InterpretedTags {
    const prefixes: string[] = [];
    NetworkScopes.all.forEach((networkScope) => {
      NetworkTypes.all.forEach((networkType) => {
        const s = NetworkScopes.letter(networkScope);
        const t = NetworkTypes.letter(networkType);
        prefixes.push(s + t);
      });
    });
    const standardTagKeys: string[] = [];
    prefixes.forEach((prefix) => {
      standardTagKeys.push(`${prefix}n_ref`);
      standardTagKeys.push(`${prefix}n_name`);
      standardTagKeys.push(`${prefix}n:name`);
      standardTagKeys.push(`name:${prefix}n_ref`);
      standardTagKeys.push(`proposed:${prefix}n_name`);
      standardTagKeys.push(`proposed:${prefix}n:name`);
      standardTagKeys.push(`proposed:name:${prefix}n_ref`);
      standardTagKeys.push(`expected_${prefix}n_route_relations`);
    });
    standardTagKeys.push('network:type');
    standardTagKeys.push('survey:date');
    return new InterpretedTags(standardTagKeys, tags);
  }

  static routeTags(tags: Tag[]): InterpretedTags {
    const standardTagKeys = ['ref', 'note', 'network', 'type', 'route', 'network:type'];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static networkTags(tags: Tag[]): InterpretedTags {
    const standardTagKeys = ['network', 'type', 'name', 'network:type'];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static locationTags(tags: Tag[]): InterpretedTags {
    const standardTagKeys = [
      'admin_level',
      'boundary',
      'name',
      'name:en',
      'name:nl',
      'name:fr',
      'name:de',
      'ref',
      'ref:INS',
      'ref:nuts',
      'ref:at:gkz',
      'ref:INSEE',
      'de:regionalschluessel',
      'de:amtlicher_gemeindeschluessel',
      'ref:gemeentecode',
      'ine:provincia',
      'local_authority:FR',
      'ref:FR:SIREN',
      'website',
      'wikidata',
      'wikipedia',
    ];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static all(tags: Tag[]): InterpretedTags {
    const standardTagKeys = [];
    return new InterpretedTags(standardTagKeys, tags);
  }

  private constructor(
    private standardTagKeys: string[],
    private tags: Tag[]
  ) {}

  isEmpty(): boolean {
    return this.tags.length === 0;
  }

  standardTags(): Tag[] {
    const tagArray: Array<Tag> = [];
    this.standardTagKeys.forEach((key) => {
      this.tags.filter((t) => t.key === key).forEach((x) => tagArray.push(x));
    });
    return tagArray;
  }

  extraTags(): Tag[] {
    const tags = this.tags.filter((tag) => !this.standardTagKeys.includes(tag.key));
    return List(tags)
      .sortBy((tag) => tag.key)
      .toArray();
  }

  hasStandardTags(): boolean {
    return this.tags.find((tag) => this.standardTagKeys.includes(tag.key)) !== undefined;
  }

  hasExtraTags(): boolean {
    return this.tags.find((tag) => !this.standardTagKeys.includes(tag.key)) !== undefined;
  }
}
