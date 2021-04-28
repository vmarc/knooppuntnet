import { NetworkScope } from '@api/custom/network-scope';
import { Tag } from '@api/custom/tag';
import { Tags } from '@api/custom/tags';
import { List } from 'immutable';
import { NetworkTypes } from '../../../kpn/common/network-types';

export class InterpretedTags {
  static nodeTags(tags: Tags): InterpretedTags {
    const standardTagKeys: string[] = [];
    NetworkScope.all.forEach((networkScope) => {
      NetworkTypes.all.forEach((networkType) => {
        const prefix = `${networkScope.letter}${NetworkTypes.letter(
          networkType
        )}`;
        standardTagKeys.push(`${prefix}n_ref`);
        standardTagKeys.push(`expected_${prefix}n_route_relations`);
      });
    });
    standardTagKeys.push('network:type');
    standardTagKeys.push('survey:date');
    return new InterpretedTags(standardTagKeys, tags);
  }

  static routeTags(tags: Tags): InterpretedTags {
    const standardTagKeys = [
      'ref',
      'note',
      'network',
      'type',
      'route',
      'network:type',
    ];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static networkTags(tags: Tags): InterpretedTags {
    const standardTagKeys = ['network', 'type', 'name', 'network:type'];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static all(tags: Tags): InterpretedTags {
    const standardTagKeys = [];
    return new InterpretedTags(standardTagKeys, tags);
  }

  private constructor(private standardTagKeys: string[], private tags: Tags) {}

  isEmpty(): boolean {
    return this.tags.tags.length === 0;
  }

  standardTags(): Tag[] {
    const tagArray: Array<Tag> = [];
    this.standardTagKeys.forEach((key) => {
      this.tags.tags
        .filter((t) => t.key === key)
        .forEach((x) => tagArray.push(x));
    });
    return tagArray;
  }

  extraTags(): Tag[] {
    const tags = this.tags.tags.filter(
      (tag) => !this.standardTagKeys.includes(tag.key)
    );
    return List(tags)
      .sortBy((tag) => tag.key)
      .toArray();
  }

  hasStandardTags(): boolean {
    return (
      this.tags.tags.find((tag) => this.standardTagKeys.includes(tag.key)) !==
      undefined
    );
  }

  hasExtraTags(): boolean {
    return (
      this.tags.tags.find((tag) => !this.standardTagKeys.includes(tag.key)) !==
      undefined
    );
  }
}
