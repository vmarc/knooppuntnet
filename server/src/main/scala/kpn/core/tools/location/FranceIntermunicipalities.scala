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

  val types: Seq[String] = Seq("CA", "CC", "CU", "metropole")

  // municipalities that are documented in wikipedia.org as having no intermunicipality
  val orphanMunicipalityIds: Seq[Long] = Seq(
    399669L, // siren=22016 name=Île-de-Bréhat
    278723L, // siren=29083 name=Île-de-Sein
    156066L, // siren=85113 name=L'Île-d'Yeu
    659733L, // siren=29155 name=Ouessant
  )

  // intermunicipalities with hardcoded municipalities (municipalities could not be assigned based on geometry)
  val intermunicipalities: Seq[FranceIntermunicipality] = Seq(
    FranceIntermunicipality(
      "fr-2-200023794", // Communauté de communes de Blaye
      1664257L, // 33058 Blaye
    ),
    FranceIntermunicipality(
      "fr-2-200023794", // Communauté de communes de Blaye
      1689066L, // 33405 Saint-Genès-de-Blaye
    ),
    FranceIntermunicipality(
      "fr-2-200039915", // Communauté d'agglomération Cannes Pays de Lérins
      91734L, // 06029 Cannes
    ),
    FranceIntermunicipality(
      "fr-2-200041762", // relation=2177707  Communauté d'agglomération Rochefort Océan
      113454L, // 17004 Île-d'Aix
      1119088L, // 17484 Port-des-Barques
    ),
    FranceIntermunicipality(
      "fr-2-200042174", // relation=1354662  Lorient Agglomération
      117493L, // 56062 Gâvres
      107972L, // 56069 Groix
      114517L, // 56118 Locmiquélic
      114521L, // 56181 Port-Louis
      114516L, // 56193 Riantec
    ),
    FranceIntermunicipality(
      "fr-2-200042604", // Communauté de communes Granville Terre et Mer
      1019742L, // 50218 Granville
    ),
    FranceIntermunicipality(
      "fr-2-200043123", // relation=1354267  Auray Quiberon Terre Atlantique
      215401L, // 56085 Hœdic
      215400L, // 56086 Île-d'Houat
    ),
    FranceIntermunicipality(
      "fr-2-200043693", // relation=3523740  Communauté de communes Terre Lorraine du Longuyonnais
      1429422L, // 54412 Othe
    ),
    FranceIntermunicipality(
      "fr-2-200044014", // Communauté de communes Creuse Grand Sud
      119733L, // 23179 Saint-Alpinien
    ),
    FranceIntermunicipality(
      "fr-2-200065928", // Lannion-Trégor Communauté
      959225L, // 22111 Lanmodez
    ),
    FranceIntermunicipality(
      "fr-2-200065928", // Lannion-Trégor Communauté
      411003L, // 22198 Pleumeur-Bodou
    ),
    FranceIntermunicipality(
      "fr-2-200066868", // relation=6876314  Communauté de communes Presqu’île de Crozon-Aulne Maritime
      296071L, // 29240 Rosnoën
      296072L, // 29053 Le Faou
      1189544L, // 29302 Pont-de-Buis-lès-Quimerch
    ),
    FranceIntermunicipality(
      "fr-2-200067072", // Haut-Léon Communauté
      417923L, // 29082 Île-de-Batz
    ),
    FranceIntermunicipality(
      "fr-2-200067932", // relation=6946240  Golfe du Morbihan - Vannes agglomération
      960821L, // 56087 Île-aux-Moines
      192873L, // 56088 Île-d'Arz
      960849L, // 56106  Larmor-Baden
      959441L, // 56205  Saint-Armel
    ),
    FranceIntermunicipality(
      "fr-2-200068500", // relation=7009235  Communauté d'Agglomération de Cambrai
      56236L, // 59097 Boursies
      56237L, // 59176 Doignies
      56235L, // 59405 Mœuvres
    ),
    FranceIntermunicipality(
      "fr-2-200068989", // Dinan Agglomération
      377037L, // 22302 Saint-Jacut-de-la-Mer
    ),
    FranceIntermunicipality(
      "fr-2-200069300", // relation=7008934  Communauté d'agglomération Tarbes-Lourdes-Pyrénées
      398772L, // 65185 Gardères
      398750L, // 65292 Luquet
      1821439L, // 65422 Séron
    ),
    FranceIntermunicipality(
      "fr-2-200069615", // Communauté de communes Bresse Haute Seille
      147907L, // 39140 Chêne-Sec
    ),
    FranceIntermunicipality(
      "fr-2-200071173", // relation=6915461  Communauté de communes du Pays Arnay Liernais
      131862L, // 21403 Ménessaire
    ),
    FranceIntermunicipality(
      "fr-2-200073104", // Communauté de communes de l'Île-Rousse - Balagne
      1928202L, // 2B134 L'Île-Rousse
    ),
    FranceIntermunicipality(
      "fr-2-242900074", // relation=1657554  Pays d'Iroise Communauté
      900558L, // 29084 Île-Molène
    ),
    FranceIntermunicipality(
      "fr-2-240800862", // Communauté de communes des Crêtes Préardennaises
      3287034L, // 08422 Singly
    ),
    FranceIntermunicipality(
      "fr-2-241200187", // Rodez Agglomération
      123303L, // 12264 Sébazac-Concourès
    ),
    FranceIntermunicipality(
      "fr-2-242900074", // Pays d'Iroise Communauté
      568778L, // 29040 Le Conquet
    ),
    FranceIntermunicipality(
      "fr-2-242900314", // relation=1657556  Brest Métropole
      85684L, // 29189 Plougastel-Daoulas
    ),
    FranceIntermunicipality(
      "fr-2-242900777", // relation=8672137  Communauté de communes du Pays Léonard ()
      417923L, // 29082 Île-de-Batz
    ),
    FranceIntermunicipality(
      "fr-2-242900835", // Morlaix Communauté
      419078L, // 29023 Carantec
    ),
    FranceIntermunicipality(
      "fr-2-243300811", // Communauté de communes de l'Estuaire
      1664320L, // 33370 Saint-Androny
    ),
    FranceIntermunicipality(
      "fr-2-244500484", // relation=2000854  Communauté de communes de la Forêt
      149646L, // 45214 Montigny
    ),
    FranceIntermunicipality(
      "fr-2-246000871", // Communauté de communes du Pays de Valois
      1083086L, // 60658 Vauciennes
    ),
    FranceIntermunicipality(
      "fr-2-246701080", // relation=2029707  Communauté de communes du Pays de Sainte-Odile
      902366L, // 67031 Bernardswiller
      903639L, // 67248 Krautergersheim
      903802L, // 67286 Meistratzheim
      903829L, // 67329 Niedernai
      903831L, // 67348 Obernai
    ),
    FranceIntermunicipality(
      "fr-2-246800569", // Communauté de communes de la Région de Guebwiller
      911056L, // 68318 Soultzmatt
    ),
    FranceIntermunicipality(
      "fr-2-248300543", // Métropole Toulon-Provence-Méditerranée
      380060L, // 83069 Hyères
    ),
  )
}
