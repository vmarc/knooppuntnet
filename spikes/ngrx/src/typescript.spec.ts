import {Country} from './app/api/country';
import {ScopedNetworkType} from './app/api/scoped-network-type';

interface Person {
  readonly name: string;
  readonly age: number;
}

interface Persons {
  readonly [id: string]: Person;
}

class PersonClass {
  constructor(readonly name: string,
              readonly age: number) {
  }
}

describe('typescript concepts', () => {

  it('immutable', () => {

    const person1: Person = {
      name: 'name1',
      age: 20
    };

    const person2: Person = {
      ...person1,
      name: 'name2'
    };

    expect(person1.name).toEqual('name1');
    expect(person2.name).toEqual('name2');
    expect(person1.age).toEqual(20);
    expect(person2.age).toEqual(20);

    const persons1: Persons = {
      1: person1
    };

    const persons2: Persons = {
      ...persons1,
      2: person2
    };

    expect(Object.values(persons1).map(p => p.name)).toEqual(['name1']);
    expect(Object.values(persons2).map(p => p.name)).toEqual(['name1', 'name2']);

    // Object.fromEntries([ ['0', 'a'], ['1', 'b'], ['2', 'c'] ])
    //
    // const persons3: Persons = Object.fromEntries(Object.entries(persons2).filter(e => e[0] !== "1"));
  });


  it('type consisting of list of possible string values', () => {

    const scopedNetworkType: ScopedNetworkType = {
      networkScope: '',
      networkType: 'hiking',
      key: 'key'
    };

    const country: Country = 'es';

    expect(scopedNetworkType.networkType).toEqual('hiking');
    expect(country).toEqual('es');
  });

  it('spread operator can be used on class instances', () => {

    const person1 = new PersonClass('name1', 50);

    const person2 = {
      ...person1,
      name: 'name2'
    };

    expect(person1.name).toEqual('name1');
    expect(person2.name).toEqual('name2');
    expect(person1.age).toEqual(50);
    expect(person2.age).toEqual(50);
  });

  it('timestamp as string type', () => {

    type Timestamp = string;

    const timestamp1: Timestamp = '2020-08-11 12:34:56';
    const timestamp2: Timestamp = '2020-08-12 12:34:56';
    const timestamp3: Timestamp = '2020-08-12 12:34:56';

    expect(timestamp1 < timestamp2).toEqual(true);
    expect(timestamp1 <= timestamp2).toEqual(true);
    expect(timestamp1 > timestamp2).toEqual(false);
    expect(timestamp1 >= timestamp2).toEqual(false);

    expect(timestamp2 < timestamp1).toEqual(false);

    expect(timestamp2 === timestamp3).toEqual(true);
  });

});
