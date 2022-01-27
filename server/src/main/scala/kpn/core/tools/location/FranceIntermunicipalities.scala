package kpn.core.tools.location

case class FranceIntermunicipality(
  intermunicipalityId: String,
  municipalityIds: Long*
)

/*
  Manually configured intermunicipalities (and municipalities within these intermunicipalities) that could
  not be derived by analyzing the geometries of the intermunicipalities and municipalities (mostly because
  of 'strange' geometries).

  Looked up in https://fr.wikipedia.org/ (right sidebar under Adminitration/Intercommunalité).
 */
object FranceIntermunicipalities {

  val types = Seq("CA", "CC", "CU", "metropole")

  // municipalities that are documented in wikipedia.org as having no intermunicipality
  val orphanMunicipalityIds = Seq(
    399669L, // siren=22016 name=Île-de-Bréhat
    278723L, // siren=29083 name=Île-de-Sein
    156066L, // siren=85113 name=L'Île-d'Yeu
    659733L, // siren=29155 name=Ouessant
  )

  val intermunicipalities = Seq(
    FranceIntermunicipality(
      "200041762", // relation=2177707  Communauté d'agglomération Rochefort Océan
      113454L, // 17004 Île-d'Aix
    ),
    FranceIntermunicipality(
      "200071173", // relation=6915461  Communauté de communes du Pays Arnay Liernais
      131862L, // 21403 Ménessaire
    ),
    FranceIntermunicipality(
      "200066868", // relation=6876314  Communauté de communes Presqu’île de Crozon-Aulne Maritime
      296072L, // 29053 Le Faou
    ),
    FranceIntermunicipality(
      "242900777", // relation=8672137  Communauté de communes du Pays Léonard ()
      417923L, // 29082 Île-de-Batz
    ),
    FranceIntermunicipality(
      "242900074", // relation=1657554  Pays d'Iroise Communauté
      900558L, // 29084 Île-Molène
    ),
    FranceIntermunicipality(
      "242900314", // relation=1657556  Brest Métropole
      85684L, // 29189 Plougastel-Daoulas
    ),
    FranceIntermunicipality(
      "200066868", // relation=6876314  Communauté de communes Presqu’île de Crozon-Aulne Maritime
      296071L, // 29240 Rosnoën
      1189544L, // 29302 Pont-de-Buis-lès-Quimerch
    ),
    FranceIntermunicipality(
      "244500484", // relation=2000854  Communauté de communes de la Forêt
      149646L, // 45214 Montigny
    ),
    FranceIntermunicipality(
      "", // relation=3523740  Communauté de communes Terre Lorraine du Longuyonnais
      1429422L, // 54412 Othe
    ),
    FranceIntermunicipality(
      "200042174", // relation=1354662  Lorient Agglomération
      117493L, // 56062 Gâvres
      107972L, // 56069 Groix
      114517L, // 56118 Locmiquélic
      114521L, // 56181 Port-Louis
      114516L, // 56193 Riantec
    ),
    FranceIntermunicipality(
      "\t200043123", // relation=1354267  Auray Quiberon Terre Atlantique
      215401L, // 56085 Hœdic
      215400L, // 56086 Île-d'Houat
    ),
    FranceIntermunicipality(
      "200067932", // relation=6946240  Golfe du Morbihan - Vannes agglomération
      960821L, // 56087 Île-aux-Moines
      192873L, // 56088 Île-d'Arz
    ),
    FranceIntermunicipality(
      "200068500", // relation=7009235  Communauté d'Agglomération de Cambrai
      56236L, // 59097 Boursies
      56237L, // 59176 Doignies
      56235L, // 59405 Mœuvres
    ),
    FranceIntermunicipality(
      "200069300", // relation=7008934  Communauté d'agglomération Tarbes-Lourdes-Pyrénées
      398772L, // 65185 Gardères
      398750L, // 65292 Luquet
      1821439L, // 65422 Séron
    ),
    FranceIntermunicipality(
      "246701080", // relation=2029707  Communauté de communes du Pays de Sainte-Odile
      902366L, // 67031 Bernardswiller
      903639L, // 67248 Krautergersheim
      903802L, // 67286 Meistratzheim
      903829L, // 67329 Niedernai
      903831L, // 67348 Obernai
    )
  )
}
