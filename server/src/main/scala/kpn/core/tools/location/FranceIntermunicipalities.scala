package kpn.core.tools.location

case class FranceIntermunicipality(
  intermunicipalityId: Long,
  municipalityIds: Long*
)

/*
  Manually configured intermunicipalities (and municipalities within these intermunicipalities) that could
  not be derived by analyzing the geometries of the intermunicipalities and municipalities (mostly because
  of 'strange' geometries).

  Looked up in https://fr.wikipedia.org/ (right sidebar under Adminitration/Intercommunalité).
 */
object FranceIntermunicipalities {

  // communes that are documented in wikipedia.org to have no intermunicipality:
  //   399669L  22016  Île-de-Bréhat
  //   278723L  29083  Île-de-Sein
  //   156066L  85113  L'Île-d'Yeu
  //   659733L  29155  Ouessant

  val intermunicipalities = Seq(
    FranceIntermunicipality(
      2177707L, // Communauté d'agglomération Rochefort Océan
      113454L, // 17004 Île-d'Aix
    ),
    FranceIntermunicipality(
      6915461L, // Communauté de communes du Pays Arnay Liernais
      131862L, // 21403 Ménessaire
    ),
    FranceIntermunicipality(
      6876314L, // Communauté de communes Presqu’île de Crozon-Aulne Maritime
      296072L, // 29053 Le Faou
    ),
    FranceIntermunicipality(
      8672137L, //Communauté de communes du Pays Léonard ()
      417923L, // 29082 Île-de-Batz
    ),
    FranceIntermunicipality(
      1657554L, // Pays d'Iroise Communauté
      900558L, // 29084 Île-Molène
    ),
    FranceIntermunicipality(
      1657556L, // Brest Métropole
      85684L, // 29189 Plougastel-Daoulas
    ),
    FranceIntermunicipality(
      6876314L, // Communauté de communes Presqu’île de Crozon-Aulne Maritime
      296071L, // 29240 Rosnoën
      1189544L, // 29302 Pont-de-Buis-lès-Quimerch
    ),
    FranceIntermunicipality(
      2000854L, // Communauté de communes de la Forêt
      149646L, // 45214 Montigny
    ),
    FranceIntermunicipality(
      3523740L, // Communauté de communes Terre Lorraine du Longuyonnais
      1429422L, // 54412 Othe
    ),
    FranceIntermunicipality(
      1354662L, // Lorient Agglomération
      117493L, // 56062 Gâvres
      107972L, // 56069 Groix
      114517L, // 56118 Locmiquélic
      114521L, // 56181 Port-Louis
      114516L, // 56193 Riantec
    ),
    FranceIntermunicipality(
      1354267L, // Auray Quiberon Terre Atlantique
      215401L, // 56085 Hœdic
      215400L, // 56086 Île-d'Houat
    ),
    FranceIntermunicipality(
      6946240L, // Golfe du Morbihan - Vannes agglomération
      960821L, // 56087 Île-aux-Moines
      192873L, // 56088 Île-d'Arz
    ),
    FranceIntermunicipality(
      7009235L, // Communauté d'Agglomération de Cambrai
      56236L, // 59097 Boursies
      56237L, // 59176 Doignies
      56235L, // 59405 Mœuvres
    ),
    FranceIntermunicipality(
      7008934L, // Communauté d'agglomération Tarbes-Lourdes-Pyrénées
      398772L, // 65185 Gardères
      398750L, // 65292 Luquet
      1821439L, // 65422 Séron
    ),
    FranceIntermunicipality(
      2029707L, // Communauté de communes du Pays de Sainte-Odile
      902366L, // 67031 Bernardswiller
      903639L, // 67248 Krautergersheim
      903802L, // 67286 Meistratzheim
      903829L, // 67329 Niedernai
      903831L, // 67348 Obernai
    )
  )
}
