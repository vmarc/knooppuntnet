import { LocationPipe } from './location.pipe';

describe('location pipe', () => {
  it('transform location name', () => {
    const testPipe = (location, expected) => {
      const pipe = new LocationPipe();
      expect(pipe.transform(location)).toEqual(expected);
    };

    testPipe('Communauté de communes Bugey Sud', 'Bugey Sud');
    testPipe(`Communauté de communes de la plaine de l'Ain`, `plaine de l'Ain`);
    testPipe(`Communauté de communes de la Station des Rousses`, `Station des Rousses`);
    testPipe(
      `Communauté de communes des Rives de l’Ain - Pays du Cerdon`,
      `Rives de l’Ain - Pays du Cerdon`
    );
    testPipe(`Communauté de communes du Pays Bellegardien`, `Pays Bellegardien`);

    testPipe(`Communauté d'agglomération Privas Centre Ardèche`, `Privas Centre Ardèche`);
    testPipe(
      `Communauté d'agglomération du Bassin de Bourg-en-Bresse`,
      `Bassin de Bourg-en-Bresse`
    );
    testPipe(`Communauté d'agglomération des Portes de l'Isère`, `Portes de l'Isère`);
    testPipe(`Communauté d'agglomération de la Provence Verte`, `Provence Verte`);
    testPipe(`Communauté urbaine de Dunkerque`, `Dunkerque`);
    testPipe(`Communauté territoriale du Sud Luberon`, `Sud Luberon`);
    testPipe(`Golfe du Morbihan - Vannes agglomération`, `Golfe du Morbihan - Vannes`);
    testPipe(`Lorient Agglomération`, `Lorient`);
    testPipe(
      `Communauté d’agglomération de Béthune-Bruay, Artois Lys Romane`,
      `Béthune-Bruay, Artois Lys Romane`
    );
    testPipe(`Grand Belfort Communauté d'agglomération`, `Grand Belfort`);
    testPipe(`Dracénie Provence Verdon agglomération`, `Dracénie Provence Verdon`);
    testPipe(`Challans-Gois Communauté`, `Challans-Gois`);
    testPipe(`Les Sables d'Olonne Agglomération`, `Les Sables d'Olonne`);
    testPipe(
      `Terres-de-Montaigu, communauté de communes Montaigu-Rocheservière`,
      `Terres-de-Montaigu, Montaigu-Rocheservière`
    );
    testPipe(`Communauté urbaine Grand Paris Seine et Oise`, `Grand Paris Seine et Oise`);
    testPipe(`Communauté de communes du canton de La Chambre`, `Chambre`);
  });
});
