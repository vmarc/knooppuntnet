package kpn.core.tools.monitor

object MonitorDemoRoute {

  // routeId mapping, see: https://wiki.openstreetmap.org/wiki/WikiProject_Belgium/Long_Distance_Walking_Routes

  val routes: Seq[MonitorDemoRoute] = Seq(
    MonitorDemoRoute("GR017", 0L, "2020-06-26_Projet-GR017_Lesse-Lomme.gpx"), // 6 tracks in file, not loaded for now
    MonitorDemoRoute("Amblève", 0L, "Amblève-par-les-GR_20170213.gpx"), // 12 tracks in file, not loaded for now

    MonitorDemoRoute("GR005-a", 1L, "GR005_Parcours-principal_2021-05-06.gpx"),
    MonitorDemoRoute("GR005-b", 1L, "GR005-573-576_Liaison_2015-12-29.gpx"),
    MonitorDemoRoute("GR005-c", 1L, "GR005-576_Liaison_2016-01-07.gpx"),
    MonitorDemoRoute("GR005-d", 1L, "GR005_Liaison_GR573-GR576_2015-12-29.gpx"),
    MonitorDemoRoute("GR005-e", 1L, "GR005_Liaison_GR576_2016-01-07.gpx"),

    MonitorDemoRoute("GR012-a", 13632L, "GR012_Parcours-principal_2021-09-08.gpx"), // OK - relation much larger than gpx
    MonitorDemoRoute("GR012-b", 1L, "GR012-016_Liaison-2017-10-19.gpx"),
    MonitorDemoRoute("GR012-c", 1L, "GR012-126_Liaison_2020-02-01.gpx"),
    MonitorDemoRoute("GR012-d", 1L, "GR012_Liaison_GR126_2020-02-01.gpx"),

    MonitorDemoRoute("GR014-a", 2929186L, "GR014_Parcours-principal_2021-07-31.gpx"), // OK
    MonitorDemoRoute("GR014-b", 1L, "GR014-GR056_Var-Crue-Reinhardstein_2019-10-26.gpx"),
    MonitorDemoRoute("GR014-c", 1L, "GR014-GR057_Var-ResNat-Orti_2020-06-01.gpx"),
    MonitorDemoRoute("GR014-d", 1L, "GR014-GR151_Liaison-AJ-Champlon-Apr-Journal_2018-04-04.gpx"),
    MonitorDemoRoute("GR014-e", 1L, "GR014-GR151_Liaison-AJ-Champlon-Av-Journal_2018-04-04.gpx"),
    MonitorDemoRoute("GR014-f", 10291197L, "GR014_Liaison-AJ-Champlon-1_2018-04-04.gpx"), // OK
    MonitorDemoRoute("GR014-g", 10291210L, "GR014_Liaison-AJ-Champlon-2_2018-04-04.gpx"), // OK
    MonitorDemoRoute("GR014-h", 10285544L, "GR014_Var-Crue-Semois_2018-04-04.gpx"), // OK
    MonitorDemoRoute("GR014-i", 1L, "GR014_Var_ResNat_Orti_2020-06-01.gpx"),

    MonitorDemoRoute("GR015-a", 8613893L, "GR015_Parcours-principal_2021-02-17.gpx"), // OK
    MonitorDemoRoute("GR015-b", 1L, "GR015-151_Liaison_2020-04-28.gpx"), // TODO map page does not load ok, why???
    MonitorDemoRoute("GR015-c", 9157328L, "GR015-GR129_Liaison-Gare-Arlon_2021-02-17.gpx"), // OK
    MonitorDemoRoute("GR015-d", 1L, "GR015_Liaison-GR151,E3_2020-04-28.gpx"),
    MonitorDemoRoute("GR015-e", 1L, "GR015_Liaison-GRP571_2019-07-03.gpx"),
    MonitorDemoRoute("GR015-f", 9156234L, "GR015_Var-Charmille_2019-03-03.gpx"), // OK - two files for the same route?
    MonitorDemoRoute("GR015-g", 9156234L, "GR015_Var.Charmille_2019-03-03.gpx"), // OK - two files for the same route?
    MonitorDemoRoute("GR015-h", 8636396L, "GR015_Var-Fagne-de-Nahtsief_2019-03-03.gpx"), // OK - two files for the same route?
    MonitorDemoRoute("GR015-i", 8636396L, "GR015_Var.Fagne-de-Nahtsief_2019-03-03.gpx"), // OK - two files for the same route?
    MonitorDemoRoute("GR015-j", 4163012L, "GR015_Var-Mardasson_2020-04-28.gpx"), // OK - two files for the same route?
    MonitorDemoRoute("GR015-k", 4163012L, "GR015_Var.Mardasson_2020-04-28.gpx"), // OK - two files for the same route?

    MonitorDemoRoute("GR016-a", 197843L, "GR016-Parcours-principal_2021-03-24.gpx"), // OK
    MonitorDemoRoute("GR016-b", 7854005L, "GR016-Liaison-gare-Arlon_2021-03-18.gpx"), // NOK?
    MonitorDemoRoute("GR016-c", 1L, "GR016-Liaison-gare-Florenville_2021-03-18.gpx"),
    MonitorDemoRoute("GR016-d", 7854056L, "GR016-Liaison-GR151_2021-03-18.gpx"), // OK
    MonitorDemoRoute("GR016-e", 7860298L, "GR016-Variante-Frahan_2021-03-18.gpx"), // OK
    MonitorDemoRoute("GR016-f", 1L, "GR016-Variante-Herbeumont_2021-03-18.gpx"),
    MonitorDemoRoute("GR016-g", 7860671L, "GR016-Variante-Laforet_2021-03-18.gpx"), // OK
    MonitorDemoRoute("GR016-h", 197843L, "GR016-Variante-Les-Bulles_2021-03-18.gpx"), // NOK
    MonitorDemoRoute("GR016-i", 4161833L, "GR016-Variante-Les-Echelles_2021-03-18.gpx"), // OK
    MonitorDemoRoute("GR016-j", 7862661L, "GR016-Variante-Les-Hayons_2021-03-18.gpx"), // OK
    MonitorDemoRoute("GR016-k", 7857070L, "GR016-Variante-Mortehan_2021-03-18.gpx"), // OK
    MonitorDemoRoute("GR016-l", 7854108L, "GR016-Variante-Roche-Lenel_2021-03-18.gpx"), // OK
    MonitorDemoRoute("GR016-m", 197843L, "GR016-Variante-source-Semois_2021-03-18.gpx"), // NOK, relation is principal route

    MonitorDemoRoute("GR017-a", 1L, "GR017-Lesse_Parcours-principal_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-b", 1L, "GR017-Lesse_Parcours-principal-provisoire_2021-10-12.gpx"),
    MonitorDemoRoute("GR017-c", 1L, "GR017-Lesse_Boucle-01-Vallée-de-l_Our_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-d", 1L, "GR017-Lesse_Boucle-02-Vallée-de-l_Almache_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-e", 1L, "GR017-Lesse_Boucle-03-Vallée-du-Wéri_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-f", 1L, "GR017-Lesse_Boucle-04-Gouffres-et-chavées_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-g", 1L, "GR017-Lesse_Boucle-05b-Famenne_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-h", 1L, "GR017-Lesse_Boucle-05-Famenne_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-i", 1L, "GR017-Lesse_Boucle-06-Lesse-et-Hileau_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-j", 1L, "GR017-Lesse_Boucle-07-Hauts-lieux_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-k", 1L, "GR017-Lesse_Boucle-08-Meuse-et-Lesse_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-l", 1L, "GR017-Lesse_Liaison-GR577_2021-04-27.gpx"),
    MonitorDemoRoute("GR017-m", 1L, "GR017-Lesse_Variante-crue-Lesse-Daverdisse_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-n", 1L, "GR017-Lesse_Variante-crue-résurgence-Lomme_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-o", 1L, "GR017-Lesse_Variante-de-la-Bouchaille_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-p", 1L, "GR017-Lesse_Variante-des-Pairées_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-q", 1L, "GR017-Lomme_Parcours-principal_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-r", 1L, "GR017-Lomme_Boucle-09-Vallée-de-la-Marsol_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-s", 1L, "GR017-Lomme_Boucle-10-Vallée-de-la-Masblette_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-t", 1L, "GR017-Lomme_Liaison-gare-de-Jemelle_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-u", 1L, "GR017-Lomme_Liaison-GRP151_2021-04-09.gpx"),
    MonitorDemoRoute("GR017-v", 1L, "GR017-Lomme_Variante-de-Bure_2021-04-09.gpx"),

    MonitorDemoRoute("GR056-a", 1477696L, "GR056_Parcours-principal_2020-09-20.gpx"), // OK - osm relation has extra connection?
    MonitorDemoRoute("GR056-b", 1477696L, "GR056_Parcours-principal-provisoire_2021-11-23.gpx"), // configured principal relation
    MonitorDemoRoute("GR056-c", 1477696L, "GR056-GR014_Var-Crue-Reinhardstein_2019-10-26.gpx"), // configured principal relation
    MonitorDemoRoute("GR056-d", 1477696L, "GR056_Liaison-Bayehon_2018-03-25.gpx"), // configured principal relation
    MonitorDemoRoute("GR056-e", 1477696L, "GR056_Liaison-Variante_Warche-Krinkelt_2016-08-11.gpx"), // configured principal relation
    MonitorDemoRoute("GR056-f", 1477696L, "GR056_Var.Amblève_2016-08-11.gpx"), // OK - extra connection
    MonitorDemoRoute("GR056-g", 1477696L, "GR056_Var.Warche_2018-03-25.gpx"), // configured principal relation

    MonitorDemoRoute("GR057-a", 9308683L, "GR057_Sentier-de-l_Ourthe-Occidentale_Parcours-principal_2021-05-02.gpx"), // OK
    MonitorDemoRoute("GR057-b", 157527L, "GR057_Sentier-de-l_Ourthe-Orientale_Parcours-principal_2021-05-02.gpx"), // OK
    MonitorDemoRoute("GR057-c", 9294292L, "GR057_Sentier-de-l_Ourthe_Parcours-principal_2021-05-04.gpx"), // OK
    MonitorDemoRoute("GR057-d", 1L, "GR057-GR005_Liaison_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-e", 1L, "GR057-GR014_Var_ResNat_Orti_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-f", 1L, "GR057-GR571_Liaison_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-g", 1L, "GR057_Liaison-Poulseur_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-h", 1L, "GR057_Liaison-Tilff_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-i", 1L, "GR057_Sentier-du-Nord-déviation_2017-03-03.gpx"),
    MonitorDemoRoute("GR057-j", 1L, "GR057_Sentier-du-Nord_Parcours-principal_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-k", 1L, "GR057_Var_crue_La-Nasse_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-l", 1L, "GR057_Var_Hérou_2021-05-02.gpx"),
    MonitorDemoRoute("GR057-m", 1L, "GR057_Var_RAVeL-Presseux_2021-05-02.gpx"),
    // 9135812 Liaison Parc de la Boverie - GR 5 à Barchon ???

    MonitorDemoRoute("GR121", 1320213L, "GR121_Parcours complet_2019-07-09.gpx"), // OK

    MonitorDemoRoute("GR123-a", 1L, "GR123_Parcours-principal_2020-01-07.gpx"),
    MonitorDemoRoute("GR123-b", 1L, "GR123_Liaison-Gare_2020-01-07.gpx"),
    MonitorDemoRoute("GR123-c", 1L, "GR123_Variante_2020-01-07.gpx"),

    MonitorDemoRoute("GR126-a", 33768L, "GR126_Parcours-principal_2021-08-10.gpx"), // OK
    MonitorDemoRoute("GR126-b", 33768L, "GR126_Parcours-principal-provisoire_2021-10-12.gpx"), // OK
    MonitorDemoRoute("GR126-c", 33768L, "GR126_Parcours-principal-provisoire_2021-10-27.gpx"), // OK - only keep this one?
    MonitorDemoRoute("GR126-d", 1L, "GR126-579_Liaison_2016-08-11.gpx"),
    MonitorDemoRoute("GR126-e", 1L, "GR126_liaison-AJ-Rops_2017-10-25.gpx"),
    MonitorDemoRoute("GR126-f", 1L, "GR126_Liaison-Gare-Mollem_2016-08-11.gpx"),
    MonitorDemoRoute("GR126-g", 1437761L, "GR126_liaison-Gare-Namur_2018-02-19.gpx"), // OK
    MonitorDemoRoute("GR126-h", 1L, "GR126 louette.gpx"),
    MonitorDemoRoute("GR126-i", 1L, "GR126-Variante_Crue-Furfooz_2020-06-04.gpx"),
    MonitorDemoRoute("GR126-j", 1L, "GR126_Variante Crue-Furfooz_2020-06-14.gpx"),
    MonitorDemoRoute("GR126-k", 1L, "GR126_Var-Meuse-Freyr_2016-08-11.gpx"),
    // 4205279 Connection to the Youth Hostel La Plante (2.8 km)

    MonitorDemoRoute("GR129-a", 2521808L, "GR129_Parcours-Principal_2021-10-11.gpx"), // OK
    MonitorDemoRoute("GR129-b", 1L, "GR129_Parcours-Principal_2021-10-11.gpx"),
    MonitorDemoRoute("GR129-c", 1L, "GR129-GR122_Liaison-Haut-Pays_2021-02-05.gpx"),
    MonitorDemoRoute("GR129-d", 1L, "GR129-GRP125_Liaison-Beaumont_2021-02-05.gpx"),

    MonitorDemoRoute("GR129S-a", 1L, "GR129S_Parcours-principal_2020-08-08.gpx"),
    MonitorDemoRoute("GR129S-b", 1L, "GR129S_Itinéraire-de-substitution-peste-porcine_2020-05-06.gpx"),
    MonitorDemoRoute("GR129S-c", 1L, "GR129S_Liaison-gare-Arlon_2018-11-17.gpx"),
    MonitorDemoRoute("GR129S-d", 1L, "GR129S_Liaison-gare-Florenville_2017-11-17.gpx"),
    MonitorDemoRoute("GR129S-e", 1L, "GR129S_Parcours-principal-provisoire_2021-10-12.gpx"),
    MonitorDemoRoute("GR129S-f", 1L, "GR129S_VarianteCrue-Daverdisse-Lesse_2020-06-26.gpx"),

    MonitorDemoRoute("GR412-a", 157373L, "GR412_Parcours-principal_2021-09-29.gpx"), // OK - ignore older gpx?
    MonitorDemoRoute("GR412-b", 157373L, "GR412_Parcours-principal_2021-10-19.gpx"), // OK - only keep this?
    MonitorDemoRoute("GR412-c", 9994009L, "GR412_Boucle_des_Coteaux_2021-09-29.gpx"), // OK
    MonitorDemoRoute("GR412-d", 9994110L, "GR412_Boucle-Hasard_Bas-Bois_2021-09-29.gpx"), // OK
    MonitorDemoRoute("GR412-e", 9644845L, "GR412_Boucle_Noire_2021-09-29.gpx"), // OK - ignore older gpx?
    MonitorDemoRoute("GR412-f", 9644845L, "GR412_Boucle-Noire_2021-11-13.gpx"), // OK - only keep this?
    MonitorDemoRoute("GR412-g", 9644845L, "GR412_Boucle-Noire_variante-sommet-terril-des-Piges_2021-11-13.gpx"), // NOK relation mismatch
    MonitorDemoRoute("GR412-h", 13481799L, "GR412_Liaison-Gare-Angleur_2021-02-12.gpx"), // OK
    MonitorDemoRoute("GR412-i", 12420102L, "GR412_Liaison-Gare-Namur_2021-02-12.gpx"), // OK

    MonitorDemoRoute("GR512", 0L, "GR512_171201_16764.gpx"), // 2 tracks in file id 1860840, 112820?, 6315832?

    MonitorDemoRoute("GR564", 2099432L, "GR564_2020-06-04.gpx"), // OK - osm relation lot longer

    MonitorDemoRoute("GR570-a", 1L, "GR570_Parcours-principal_2021-07-05.gpx"),
    MonitorDemoRoute("GR570-b", 1L, "GR570_Parcours-principal_2021-10-25.gpx"),
    MonitorDemoRoute("GR570-c", 1L, "GR570_Liaison-gare-Halanzy_2021-10-25.gpx"),
    MonitorDemoRoute("GR570-d", 1L, "GR570_Liaison-gare-Messancy_2021-10-25.gpx"),
    MonitorDemoRoute("GR570-e", 1L, "GR570-Variante-GDLux_2016-09-14.gpx"),

    MonitorDemoRoute("GR573-a", 166219L, "GR573_Pacours-principal_2021-02-14.gpx"), // OK - osm relation has extra alternative?
    MonitorDemoRoute("GR573-b", 166219L, "GR573_Parcours-principal_2020-11-11.gpx"), // OK - osm relation has extra alternative?
    MonitorDemoRoute("GR573-c", 1L, "GR573-005-576_Liaison_2015-12-29.gpx"),
    MonitorDemoRoute("GR573-d", 1L, "GR573-412_Liaison_2015-12-29.gpx"),
    MonitorDemoRoute("GR573-e", 1L, "GR573-GR563_Liaison-Verviers_2016-10-17.gpx"),
    MonitorDemoRoute("GR573-f", 1L, "GR573_Itineraire-de-substitution_2015-12-29.gpx"),
    MonitorDemoRoute("GR573-g", 1L, "GR573_Liaison_GR412_2015-12-29.gpx"),
    MonitorDemoRoute("GR573-h", 1L, "GR573_Liaison_GR563-Verviers_2016-10-17.gpx"),
    MonitorDemoRoute("GR573-i", 1L, "GR573_Liaison_GR5-GR576_2015-12-29.gpx"),
    MonitorDemoRoute("GR573-j", 1L, "GR573_Var-Spa_2020-10-07.gpx"),
    MonitorDemoRoute("GR573-k", 1L, "GR573_Var-Spa_2021-02-14.gpx"),

    MonitorDemoRoute("GR575-a", 4642627L, "GR575-576_Pacours-principal_2020-02-26.gpx"), // OK
    MonitorDemoRoute("GR575-b", 1L, "GR575-576_Liaison_Les-Avins-Grand-Marchin_2020-02-25.gpx"),
    MonitorDemoRoute("GR575-c", 1L, "GR575_Liaison-AJ-Namur_2020-05-20.gpx"),
    MonitorDemoRoute("GR575-d", 1L, "GR575_Liaison_Mozet-Gesves-Porcheresse_2020-02-25.gpx"),

    MonitorDemoRoute("GR576-a", 1L, "GR576-005-573_Liaison_2020-02-25.gpx"),
    MonitorDemoRoute("GR576-b", 1L, "GR576-005_Liaison_2020-02-25.gpx"),
    MonitorDemoRoute("GR576-c", 1L, "GR576-579_Liaison_2020-02-25.gpx"),
    MonitorDemoRoute("GR576-d", 1L, "GR576_Liaison-Gare-Huy_2020-02-25.gpx"),
    // 4642627 	GR576 Tour du Condroz Liégeois

    MonitorDemoRoute("GR579-a", 53358L, "GR579-Parcours-principal_2020-11-09.gpx"), // OK
    MonitorDemoRoute("GR579-b", 53358L, "GR579-Parcours-principal_2021-09-28.gpx"), // OK - only keep this one?
    MonitorDemoRoute("GR579-c", 1L, "GR579_Laison_Jezus-Eik.gpx"),
    MonitorDemoRoute("GR579-d", 1L, "GR579_Liaison-Amay.gpx"),
    MonitorDemoRoute("GR579-e", 1L, "GR579_Variante-Dijleland.gpx"),
    MonitorDemoRoute("GR579-f", 1L, "GR579_Variante-Ottenburg.gpx"),
    // 1646736 GR579 Huldenberg - Wavre - Pécrot (22 km) Connection to GR121 at Wavre

    MonitorDemoRoute("GR655", 1L, "GR655_Tracé temporaire_2020-06-10.gpx"),

    MonitorDemoRoute("GRP123-a", 10494349L, "GRP123_Parcours-principal_2021-04-15.gpx"), // OK
    MonitorDemoRoute("GRP123-b", 10494349L, "GRP123_Parcours-principal-temporaire_2021-10-28.gpx"), // OK
    MonitorDemoRoute("GRP123-c", 10493121L, "GRP123_Liaison-Gare-de-Tournai_2021-04-15.gpx"), // OK
    MonitorDemoRoute("GRP123-d", 1L, "GRP123_Liaison_GR5A_Deux-Acren_2021-04-15.gpx"),
    MonitorDemoRoute("GRP123-e", 1L, "GRP123_Liaison_GR5A_Saint-Leger_2021-04-15.gpx"),
    MonitorDemoRoute("GRP123-f", 1L, "GRP123_Liaison_St-Léger-GR005A_2021-10-29.gpx"),
    MonitorDemoRoute("GRP123-g", 1L, "GRP123_Liaison-temporaire_Deux-Acren-GR005A_2021-10-29.gpx"),
    MonitorDemoRoute("GRP123-h", 10514878L, "GRP123_Variante-de-Silly_2021-04-15.gpx"), // OK

    MonitorDemoRoute("GRP125-a", 1L, "GRP125_Parcours-principal_ 2021-10-11.gpx"),
    MonitorDemoRoute("GRP125-b", 1L, "GRP125-126_Liaison-GareNamur_2016-08-11.gpx"),
    MonitorDemoRoute("GRP125-c", 1L, "GRP125-GR129-N_Liaison-Beaumont_2020-04-05.gpx"),
    MonitorDemoRoute("GRP125-d", 1L, "GRP125_liaison-AJ-Rops_2017-10-25.gpx"),
    MonitorDemoRoute("GRP125-e", 1L, "GRP125_liaison-Gare-Namur_2018-02-19.gpx"),
    MonitorDemoRoute("GRP125-f", 1L, "GRP125_Liaison-Lac-du-Valjoly_2018-02-19.gpx"),
    MonitorDemoRoute("GRP125-g", 1L, "GRP125_Var.Fraire_2017-10-25.gpx"),
    MonitorDemoRoute("GRP125-h", 1L, "GRP125_Var-Meuse-Freyr_2016-08-11.gpx"),
    MonitorDemoRoute("GRP125-i", 1L, "GRP125_Var.Senzeille_2017-10-22.gpx"),

    MonitorDemoRoute("GRP127-a", 34122L, "GRP127_Parcours-principal_2021-09-28.gpx"), // OK - ignore older gpx?
    MonitorDemoRoute("GRP127-b", 34122L, "GRP127_Parcours-principal_2021-12-09.gpx"), // OK
    MonitorDemoRoute("GRP127-c", 10965514L, "GRP127_Var.Pluie-Quenast_2018-01-25.gpx"), // OK

    MonitorDemoRoute("GRP151-a", 1L, "GRP151_Parcours-principal_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-b", 1L, "GRP151_Liaison-AJ-Champlon-Est-Journal_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-c", 1L, "GRP151_Liaison-AJ-Champlon-Nord-Journal_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-d", 1L, "GRP151_Liaison-gare-Forrières_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-e", 1L, "GRP151_Liaison-gare-Habay_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-f", 1L, "GRP151_Liaison-gare-Jemelle_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-g", 1L, "GRP151_Liaison-gare-Libramont_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-h", 1L, "GRP151_Liaison-gare-Marche_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-i", 1L, "GRP151_Liaison-gare-Marloie_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-j", 1L, "GRP151_Liaison-gare-Neufchateau_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-k", 1L, "GRP151_Liaison-GR16_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-l", 1L, "GRP151_Liaison-Martelange_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-m", 1L, "GRP151_Variante-Bodange-Neufchateau_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-n", 1L, "GRP151_Variante-Chenogne-Marbehan_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-o", 1L, "GRP151_Variante-Presseux_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-p", 1L, "GRP151_Variante-Suxy_2021-09-07.gpx"),
    MonitorDemoRoute("GRP151-q", 1L, "GRP151_Variante-Thibessart_2021-09-07.gpx"),

    MonitorDemoRoute("GRP563-a", 1L, "GRP563_Parcours-principal_2020-07-14.gpx"),
    MonitorDemoRoute("GRP563-b", 1L, "GRP563_Parcours-principal_2021-11-21.gpx"),
    MonitorDemoRoute("GRP563-c", 1L, "GRP563_Contournement-1-2016-10-17.gpx"),
    MonitorDemoRoute("GRP563-d", 1L, "GRP563_Contournement-2-2016-10-17.gpx"),
    MonitorDemoRoute("GRP563-e", 1L, "GRP563_GR573_Liaison-Verviers-2016-10-17.gpx"),
    MonitorDemoRoute("GRP563-f", 1L, "GRP563_Liaison Aix-2018-02-06.gpx"),
    MonitorDemoRoute("GRP563-g", 1L, "GRP563_Liaison_Aix-2018-02-06.gpx"),
    MonitorDemoRoute("GRP563-h", 1L, "GRP563_Var-La-Chapelle-2018-02-06.gpx"),
    MonitorDemoRoute("GRP563-i", 1L, "GRP563_Var-Soiron-Gd-Rechain-2018-02-06.gpx"),

    MonitorDemoRoute("GRP571-a", 1L, "GRP571_Parcours-principal_2019-07-03.gpx"),
    MonitorDemoRoute("GRP571-b", 1L, "GRP571-GR015_Liaison_2019-07-03.gpx"),
    MonitorDemoRoute("GRP571-c", 1L, "GRP571-GR015_Liaison-Var-Crue_2019-07-03.gpx"),
    MonitorDemoRoute("GRP571-d", 1L, "GRP571-GR057_Liaison_2019-07-03.gpx"),
    MonitorDemoRoute("GRP571-e", 1L, "GRP571-Variante-Crue_2019-07-03.gpx"),

    MonitorDemoRoute("GRP577-a", 1L, "GRP577_Parcours-principal_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-b", 1L, "GRP577-GR017_Liaison_2021-05-05.gpx"),
    MonitorDemoRoute("GRP577-c", 1L, "GRP577-Liaison-Gare_Barvaux_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-d", 1L, "GRP577-Liaison-Gare_Beauraing_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-e", 1L, "GRP577-Liaison-Gare_Jemelle_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-f", 1L, "GRP577-Liaison-gare_Marche-en-Famenne_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-g", 1L, "GRP577-Liaison_Monastere-de-Chevetogne_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-h", 1L, "GRP577_Variante-Chevetogne_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-i", 1L, "GRP577-Variante_Geopark_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-j", 1L, "GRP577-Variante_Les-Pairees_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-k", 1L, "GRP577_Variante-Revogne_2021-05-06.gpx"),
    MonitorDemoRoute("GRP577-l", 1L, "GRP577-Variante_Sinsin_2021-05-06.gpx"),

    MonitorDemoRoute("GTFPC-1", 1L, "GTFPC-1_Macquenoise-Virelles_2018-03-07.gpx"),
    MonitorDemoRoute("GTFPC-2", 1L, "GTFPC-2_Sivry-Virelles_2018-03-07.gpx"),
    MonitorDemoRoute("GTFPC-3", 1L, "GTFPC-3_Virelles-Hastiere_2018-03-07.gpx"),
    MonitorDemoRoute("GTFPC-4", 1L, "GTFPC-4_Olloy-Philippeville_2018-03-07.gpx"),
    MonitorDemoRoute("GTFPC-5", 1L, "GTFPC-4_Olloy-Philippeville_2021-09-11.gpx"),

    MonitorDemoRoute("LRE", 0L, "LRE-3Troncons-complet.gpx"), // 3 tracks in file
    MonitorDemoRoute("OBR-a", 1L, "OBR_Liaison-AJ-Eupen_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-b", 1L, "OBR_Liaison_Honsfeld-St Vith_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-c", 1L, "OBR-Partie-commune_départ_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-d", 1L, "OBR_Partie-commune-fin_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-e", 1L, "OBR-Partie-commune_Route de la frontière-Route des lacs_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-f", 1L, "OBR-Partie-commune_Route des lacs-Route de l_ouest_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-g", 1L, "OBR_Route-de-la-frontière_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-h", 1L, "OBR_Route de l_ouest_2020-02-28.gpx"),
    MonitorDemoRoute("OBR-i", 1L, "OBR_Route des lacs_2020-02-28.gpx"),

    MonitorDemoRoute("SAT-a", 0L, "SAT.gpx"), // 2 tracks in file
    MonitorDemoRoute("SAT-b", 1L, "SAT_Chimay-Rochefort_2021-05-29.gpx"),
    MonitorDemoRoute("SAT-c", 1L, "SAT_Chimay-Rochefort_VarianteCrue-Furfooz_2020-06-05.gpx"),
    MonitorDemoRoute("SAT-d", 1L, "SAT_Rochefort-Orval_2021-05-29.gpx"),
    MonitorDemoRoute("SAT-e", 1L, "SAT_Rochefort-Orval_VarianteCrue_Daverdisse-Lesse_2020-06-04.gpx"),

    MonitorDemoRoute("SMA-a", 1L, "SMA_Parcours-complet_2021-02-04.gpx"),
    MonitorDemoRoute("SMA-b", 1L, "SMA_Parcours-complet_2021-10-12.gpx"),
    MonitorDemoRoute("SMA-c", 1L, "SMA_Déviation-provisoire_2019-05-10.gpx"),

    MonitorDemoRoute("TDA-a", 1L, "TDA-Boucle-01_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-b", 1L, "TDA-Boucle-02_16-02-2021.gpx"),
    MonitorDemoRoute("TDA-c", 1L, "TDA-Boucle-02_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-d", 1L, "TDA-Boucle-03_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-e", 1L, "TDA-Boucle-03-crue_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-f", 1L, "TDA-Boucle-04_16-02-2021.gpx"),
    MonitorDemoRoute("TDA-g", 1L, "TDA-Boucle-04_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-h", 1L, "TDA-Boucle-05_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-i", 1L, "TDA-Boucle-06_16-02-2021.gpx"),
    MonitorDemoRoute("TDA-j", 1L, "TDA-Boucle-06_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-k", 1L, "TDA-Boucle-06_Var-castors_16-02-2021.gpx"),
    MonitorDemoRoute("TDA-l", 1L, "TDA-Boucle-06_Var-castors_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-m", 1L, "TDA-Boucle-07_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-n", 1L, "TDA-Boucle-08_16-02-2021.gpx"),
    MonitorDemoRoute("TDA-o", 1L, "TDA-Boucle-08_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-p", 1L, "TDA-Boucle-09_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-q", 1L, "TDA-Boucle-10_16-02-2021.gpx"),
    MonitorDemoRoute("TDA-r", 1L, "TDA-Boucle-10_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-s", 1L, "TDA_Parcours-principal_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-t", 1L, "TDA_Parcours-principal_Var-castors_30-12-2019.gpx"),
    MonitorDemoRoute("TDA-u", 1L, "TDA_Parcours-principal_Var-crue_30-12-2019.gpx"),

    MonitorDemoRoute("Vesdre", 0L, "Tour-de-la-Vesdre_20170213.gpx") // 19 tracks in file
  )
}

case class MonitorDemoRoute(id: String, routeId: Long, filename: String) {
}
