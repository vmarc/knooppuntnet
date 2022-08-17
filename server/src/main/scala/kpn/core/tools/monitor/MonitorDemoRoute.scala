package kpn.core.tools.monitor

object MonitorDemoRoute {

  val routes: Seq[MonitorDemoRoute] = Seq(

    // GR 5 - Mer du Nord - Méditerranée - Tronçon Wallonie et Grand-Duché de Luxembourg

    MonitorDemoRoute("GR-5-E7", 3121668L, "GR 5 - Parcours Principal", "GR005_Parcours-principal_2022-07-31"),
    MonitorDemoRoute("GR-5-E7-L1", 1L, "GR 5 - Liaison GR 576", "GR005_Liaison_GR576_2016-01-07"),
    MonitorDemoRoute("GR-5-E7-L2", 1L, "GR 5 - Liaison GR 573 et GR 576", "GR005_Liaison_GR573-GR576_2015-12-29"),

    // GR 12 - Amsterdam - Bruxelles - Paris

    // 12825429L is parent relation for:
    //   150957    GR12 - Heide
    //   13632     GR12 Amsterdam-Paris
    //   6296002   GR 12 Variant West
    //   12825428  Amsterdam-Paris, France
    // MonitorDemoRoute("GR-12-E4", 12825429L, "GR 12 - Parcours Principal", "GR012_Parcours-principal_2021-09-08"),
    MonitorDemoRoute("GR-12-E4", 13632L, "GR 12 - Parcours Principal", "GR012_Parcours-principal_2022-07-30"),
    MonitorDemoRoute("GR-12-E4-L1", 1L, "GR 12 - Liaison GR 126", "GR012_Liaison_GR126_2020-02-01"),

    // Monschau - Bouillon - Sedan

    MonitorDemoRoute("GR-14-E3", 2929186L, "GR 14 - Parcours Principal", "GR014_Parcours-principal_2022-06-23"),
    MonitorDemoRoute("GR-14-E3-L1", 1L, "GR 14 - Liaison AJ Champlon - Nord de Journal", "GR014_Liaison-AJ-Champlon-Nord-Journal_2022-06-23"),
    MonitorDemoRoute("GR-14-E3-L2", 1L, "GR 14 - Liaison AJ Champlon - Sud de Journal", "GR014_Liaison-AJ-Champlon-Sud-Journal_2022-06-23"),
    MonitorDemoRoute("GR-14-E3-V1", 1L, "GR 14 - Variante Crue Reinhardstein", "GR014_Variante-Crue-Reinhardstein_2022-06-23"),
    MonitorDemoRoute("GR-14-E3-V2", 1L, "GR 14 - Variante Réserve Naturelle Orti", "GR014_Variante-Réserve-Naturelle-Orti_2022-06-23"),
    MonitorDemoRoute("GR-14-E3-V3", 10285544L /* not in spreadsheet */ , "GR 14 - Variante Crue Semois", "GR014_Variante-Crue-Semois_2022-06-23"),
    // MonitorDemoRoute("GR-14-E3-a", 10291197L, "GR014 Liaison AJ Champlon 1 (not in spreadsheet, delete?)", "GR014_Liaison-AJ-Champlon-1_2018-04-04"), // OK
    // MonitorDemoRoute("GR-14-E3-b", 10291210L, "GR014 Liaison AJ Champlon 2 (not in spreadsheet, delete?)", "GR014_Liaison-AJ-Champlon-2_2018-04-04"), // OK

    // De la Lorraine belge à l’Eifel

    MonitorDemoRoute("GR-15-E4", 8613893L, "GR 15 - De la Lorraine belge à l’Eifel", "GR015_Parcours-principal_2022-07-19"),
    MonitorDemoRoute("GR-15-E4-L1", 1L, "GR 15 - Liaison entre le GR 15 et le GR 151, E3", "GR015_Liaison-GR151,E3_2020-04-28"),
    MonitorDemoRoute("GR-15-E4-L2", 1L, "GR 15 - Liaison entre le GR 15 et le GR 571", "GR015_Liaison-GRP571_2019-07-03"),
    MonitorDemoRoute("GR-15-E4-L3", 9157328L /* not in spreadsheet */ , "GR 15 - Liaison Gare Arlon", "GR015-GR129_Liaison-Gare-Arlon_2021-02-17"),
    MonitorDemoRoute("GR-15-E4-V1", 4163012L /* not in spreadsheet */ , "GR 15 - Variante Mardasson", "GR015_Var-Mardasson_2020-04-28"),
    MonitorDemoRoute("GR-15-E4-V2", 9156234L /* not in spreadsheet */ , "GR 15 - Variante Charmille", "GR015_Var-Charmille_2019-03-03"),
    MonitorDemoRoute("GR-15-E4-V3", 8636396L, "GR 15 - Variante Fagne de Nahtsief", "GR015_Var-Fagne-de-Nahtsief_2019-03-03"),

    // GR 16 - Sentier de la Semois

    MonitorDemoRoute("GR-16-E7", 197843L, "GR 16 - Parcours principal", "GR016_Parcours-principal_2022-06-03"),
    MonitorDemoRoute("GR-16-E7-L1", 1L, "GR 16 - Liaison gare Arlon", "GR016-Liaison-gare-Arlon_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-L2", 1L, "GR 16 - Liaison gare Florenville", "GR016-Liaison-gare-Florenville_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-L3", 7854056L /* not in spreadsheet */ , "GR 16 - Liaison GR151", "GR016-Liaison-GR151_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V1", 1L, "GR 16 - Variante source Semois", "GR016-Variante-source-Semois_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V2", 1L, "GR 16 - Variante Les Bulles", "GR016_Variante-Les-Bulles_2022-06-03"),
    MonitorDemoRoute("GR-16-E7-V3", 7854108L, "GR 16 - Variante Roche Lenel", "GR016-Variante-Roche-Lenel_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V4", 1L, "GR 16 - Variante Herbeumont", "GR016-Variante-Herbeumont_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V5", 7857070L, "GR 16 - Variante Mortehan", "GR016-Variante-Mortehan_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V6", 7862661L /* not in spreadsheet */ , "GR 16 - Variante Les Hayons", "GR016-Variante-Les-Hayons_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V7", 4161833L /* not in spreadsheet */ , "GR 16 - Variante Les échelles", "GR016-Variante-Les-Echelles_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V8", 7860298L /* not in spreadsheet */ , "GR 16 - Variante Frahan", "GR016-Variante-Frahan_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V9", 7860671L, "GR 16 - Variante Laforet", "GR016-Variante-Laforet_2021-03-18"),

    // GR 17 - Lesse et Lomme",

    MonitorDemoRoute("GR-17-E1-P1", 14339565L, "GR17 Lesse Parcours principal", "GR017_Lesse-Parcours-principal_2022-07-27"),
    MonitorDemoRoute("GR-17-E1-P2", 14399260L, "GR17 Lomme Parcours principal", "GR017-Lomme-Parcours-principal_2022-07-12"),
    MonitorDemoRoute("GR-17-E1-L1", 1L, "GR17 Lesse Liaison GR577", "GR017-Lesse_Liaison-GR577_2021-04-27"),
    MonitorDemoRoute("GR-17-E1-L2", 1L, "GR17 Lomme Liaison gare de Jemelle", "GR017-Lomme_Liaison-gare-de-Jemelle_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-L3", 1L, "GR17 Lomme Liaison GRP151", "GR017-Lomme_Liaison-GRP151_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V1", 1L, "GR17 Lesse Variante de la Bouchaille", "GR017-Lesse_Variante-de-la-Bouchaille_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V2", 1L, "GR17 Lesse Variante crue Lesse-Daverdisse", "GR017-Lesse_Variante-crue-Lesse-Daverdisse_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V3", 1L, "GR17 Lesse Variante des Pairées", "GR017-Lesse_Variante-des-Pairées_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V4", 1L, "GR17 Lesse Variante Crue Résurgence de la Lomme", "GR017-Lesse_Variante-crue-résurgence-Lomme_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V5", 1L, "GR17 Lomme Variante Variante de Bure", "GR017-Lomme_Variante-de-Bure_2021-04-09"),

    // GR-56-E8  GR 56 - Cantons de l'Est et Parc Naturel Hautes Fagnes
    MonitorDemoRoute("GR-56-E8", 13818271L, "GR 56 - Sentier des Frontières (Grenzpfad)", "GR056_Sentier-des-Frontieres-(Grenzpfad)_2022-07-29"),
    MonitorDemoRoute("GR-56-E8-L1", 8608381L, "GR 56 - Liaison Bayehon", "GR056_Liaison-Bayehon_2022-07-17"),
    MonitorDemoRoute("GR-56-E8-L2", 14405426L, "GR 56 - Liaision Aix-La-Chapelle (Aachen)", "GR056_Liaison-Aix-La-Chapelle-(Aachen)_2022-07-17"),
    MonitorDemoRoute("GR-56-E8-V1", 14388249L, "GR 56 - Variante Sentier des Fagnes (Vennpfad)", "GR056_Variante-Sentier-des-Fagnes-(Vennpfad)_2022-07-29"),
    MonitorDemoRoute("GR-56-E8-V2", 13807655L, "GR 56 - Variante Amblève (Amel)", "GR056_Variante-Ambleve_2022-07-17"),
    MonitorDemoRoute("GR-56-E8-V3", 8516113L, "GR 56 - Variante Warche", "GR056_Variante-Warche_2022-07-17"),
    MonitorDemoRoute("GR-56-E8-V4", 14405675L, "GR 56 - Variante crue Lontzen", "GR056_Variante-crue-Lontzen_2022-07-17"),
    MonitorDemoRoute("GR-56-E8-V5", 14405754L, "GR 56 - Variante drapeau rouge Nahtsief", "GR056_Variante-drapeau-rouge-Nahtsief_2022-07-17"),
    MonitorDemoRoute("GR-56-E8-V6", 14407153L, "GR 56 - Variante crue Reinhardstein", "GR056_Variante-crue-Reinhardstein_2022-07-17"),
    MonitorDemoRoute("GR-56-E8-V7", 14407281L, "GR 56 - Variante crue Herzogenhügel", "GR056_Variante-crue-Herzogenhügel_2022-07-31"),

    // GR-57-E9			GR 57 - Sentiers de l'Ourthe (Nouvelle Edition 2021)

    MonitorDemoRoute("GR-57-E9-P1", 9294292L, "GR 57 - Sentier de l'Ourthe - Parcours Principal", "GR057_Sentier-de-l_Ourthe_Parcours-principal_2022-02-22"),
    MonitorDemoRoute("GR-57-E9-P2", 157527L, "GR 57 - Sentier de l'Ourthe Orientale - Parcours Principal", "GR057_Sentier-de-l_Ourthe-Orientale-Parcours-principal_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-P3", 9294292L, "GR 57 - Sentier de l'Ourthe Occidentale - Parcours Principal", "GR057_Sentier-de-l_Ourthe-Occidentale-Parcours-principal_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-P4", 1L, "GR 57 - Escapardenne - Parcours Principal", "GR057_Escapardenne-Parcours-principal_2022-05-23"),
    MonitorDemoRoute("GR-57-E9-L1", 9135812L, "GR 57 - Liaison Barchon", "GR057_Liaison-Barchon_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L2", 1L, "GR 57 - Liaison Tilff", "GR057_Liaison-Tilff_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L3", 1L, "GR 57 - Liaison Poulseur", "GR057_Liaison-Poulseur_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L4", 1L, "GR 57 - Liaison GR 571", "GR057_Liaison-GR571_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L5", 1L, "GR 57 - Liaison Barvaux", "GR057_Liaison-Barvaux_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L6", 1L, "GR 57 - Liaison Melreux", "GR057_Liaison-Melreux_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L7", 1L, "GR 57 - Liaison gare de Hamoir", "GR057_Liaison-gare-Hamoir_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L8", 1L, "GR 57 - Liaison gare de Sy", "GR057_Liaison-gare-Sy_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-L9", 1L, "GR 57 - Liaison Gare Troisvierges", "GR057_Liaison-gare-Troisvierges_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-V1", 1L, "GR 57 - Variante Hérou", "GR057_Variante-Herou_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-V2", 1L, "GR 57 - Variante Crue", "GR057_Var_crue_La-Nasse_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-V3", 1L, "GR 57 - Variante Réserve Naturelle Orti", "GR057_Variante-Reserve-Naturelle-Orti_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-V4", 1L, "GR 57 - Variante Crue Cheslé", "GR057_Variante-Crue-Chesle_2022-02-06"),
    MonitorDemoRoute("GR-57-E9-V5", 1L, "GR 57 - Variante Bernistap", "GR057_Variante-Bernistap_2022-02-06"),

    // GR 121 - Wavre - Boulogne-sur-Mer",

    // 12447698L is the  parent relation
    //   2133138 De Arras à Boulogne-sur-Mer
    //   6263294 De Bon-Secours à Arras
    //   1320213 GR 121: Wavre - Bon-Secours

    // MonitorDemoRoute("GR-121-E4",12447698L,"GR 121 - Parcours Principal", "NO GPX FILE"),
    MonitorDemoRoute("GR-121-E4", 1320213L, "GR 121: Wavre - Bon-Secours", "GR121_Parcours complet_2019-07-09"), // OK

    // Brussegem - Bruxelles - Namur - Dinant - Membre-sur-Semois

    // 12513965L parent route
    //   33768    GR 126 Bruxelles - Membre-sur-Semois
    //   12513964 GR 126 Brussels - Brussegem
    //   107242   GR 126 Brussegem - Mariekerke
    // MonitorDemoRoute("GR-126-E5", 12513965L, "GR 126 - Parcours Principal", "GR126_Parcours-principal_2021-08-10"),
    MonitorDemoRoute("GR-126-E5", 33768L, "GR 126 - Parcours Principal", "GR126_Parcours-principal_2022-05-28"),
    MonitorDemoRoute("GR-126-E5-L1", 1L, "GR 126 - Liaison AJ Namur", "GR126_liaison-AJ-Rops_2017-10-25"),
    MonitorDemoRoute("GR-126-E5-L2", 1437761L /* not in spreadsheet */ , "GR 126 - Liaison Gare Namur", "GR126_liaison-Gare-Namur_2018-02-19"),
    MonitorDemoRoute("GR-126-E5-L3", 1L, "GR 126 - Liaison GR 579", "GR126-579_Liaison_2016-08-11"),
    MonitorDemoRoute("GR-126-E5-L4", 1L, "GR 126 - Liaison GR 12", "GR012_Liaison_GR126_2020-02-01"),
    MonitorDemoRoute("GR-126-E5-L5", 1L, "GR 126 - Liaison Gare Mollem", "GR126_Liaison-Gare-Mollem_2016-08-11"),
    MonitorDemoRoute("GR-126-E5-V1", 1L, "GR 126 - Variante Crue Furfooz", "GR126_Variante Crue-Furfooz_2020-06-14"),
    MonitorDemoRoute("GR-126-E5-V2", 1L, "GR 126 - Variante Raccourci Meuse Freyr", "GR126_Var-Meuse-Freyr_2016-08-11"),

    // GR 129 Sud - La Belgique en diagonale ! Tronçon  Dinant - Arlon

    // relation 12204688L (referenced in spreadsheet) is deleted in OSM
    // MonitorDemoRoute("GR-129S-E1", 12204688L, "GR 129 Sud - Parcours Principal", "GR129S_Parcours-principal_2020-08-08"),
    MonitorDemoRoute("GR-129S-E1", 2459527L, "GR 129 Sud - Parcours Principal", "GR129-Sud_Parcours-principal_2022-07-31"),
    MonitorDemoRoute("GR-129S-E1-L1", 1L, "GR 129 Sud - Liaison Gare Florenville", "GR129S_Liaison-gare-Florenville_2017-11-17"),
    MonitorDemoRoute("GR-129S-E1-L2", 1L, "GR 129 Sud - Liaison Gare Arlon", "GR129-GRP125_Liaison-Beaumont_2021-02-05"),
    MonitorDemoRoute("GR-129S-E1-L3", 1L, "GR 129 Sud - Liaison Gare Bertrix", "GR129S_Liaison-gare-Bertrix_2022-01-11"),
    MonitorDemoRoute("GR-129S-E1-L4", 1L, "GR 129 Sud - Liaison Gare Paliseul", "GR129S_Liaison-gare-Paliseul_2022-01-11"),
    MonitorDemoRoute("GR-129S-E1-V1", 1L, "GR 129 Sud - Variante Crue entre Daverdisse et Lesse", "GR129S_VarianteCrue-Daverdisse-Lesse_2020-06-26"),

    // GR 129 - La Belgique en diagonale ! Tronçon Ellezelles - Dinant

    // GR-129W-E3: relation 12204688L referenced in spreadsheet has been deleted, using 2521808 instead
    MonitorDemoRoute("GR-129W-E3", 2521808L, "GR 129 - Parcours Principal", "GR129_Parcours-principal_2022-07-13"),
    MonitorDemoRoute("GR-129W-E3-L1", 1L, "GR 129 - Liaison GR 129 et GRP 125", "GR129-GRP125_Liaison-Beaumont_2021-02-05"),
    MonitorDemoRoute("GR-129W-E3-L2", 1L, "GR 129 - Liaison GR 129 et GR 122 dite Liaison-des-Haut-Pays", "GR129-GR122_Liaison-Haut-Pays_2022-03-01"),

    // GR 412 - Sentier des Terrils",

    MonitorDemoRoute("GR-412-E2", 157373L, "GR412 - Parcours Principal", "GR412_Parcours-principal_2022-07-19"),
    MonitorDemoRoute("GR-412-E2-B1", 9644845L /* not in spreadsheet */ , "GR412 - Boucle 1 - Boucle Noire", "GR412_Boucle-Noire_2021-11-13"),
    MonitorDemoRoute("GR-412-E2-B2", 9994009L /* not in spreadsheet */ , "GR412 - Boucle 2 - Les Coteaux", "GR412_Boucle_des_Coteaux_2021-09-29"),
    MonitorDemoRoute("GR-412-E2-B3", 9994110L /* not in spreadsheet */ , "GR412 - Boucle 3 -  Hasard-Bas Bois", "GR412_Boucle-Hasard_Bas-Bois_2021-09-29"),
    MonitorDemoRoute("GR-412-E2-L1", 13481799L /* not in spreadsheet */ , "GR412 - Liaison gare Angleur", "GR412_Liaison-Gare-Angleur_2021-02-12"),
    // MonitorDemoRoute("GR-412-E2-V1",1L, "GR 412 Var Crue Moignelée", ""),
    MonitorDemoRoute("GR-412-E2-a", 12420102L, "GR412 - Liaison Gare Namur (not in spreadsheet, delete?)", "GR412_Liaison-Gare-Namur_2021-02-12"), // OK

    // GR 573 - Vesdre et Hautes Fagnes

    MonitorDemoRoute("GR-573-E6", 166219L, "GR 573 - Parcours Principal", "GR573_Parcours-principal_2022-07-30"),
    MonitorDemoRoute("GR-573-E6-L1", 1L, "GR 573 - Liaison GR 5 et GR 576", "GR573_Liaison_GR5-GR576_2015-12-29"),
    MonitorDemoRoute("GR-573-E6-L2", 1L, "GR 573 - Liaison GRP 563 et Verviers", "GR573_Liaison_GR563-Verviers_2016-10-17"),
    MonitorDemoRoute("GR-573-E6-L3", 1L, "GR 573 - Liaison GR 412", "GR573-412_Liaison_2015-12-29"),
    MonitorDemoRoute("GR-573-E6-V1", 1L, "GR 573 - Variante Spa", "GR573_Variante-Spa_2022-07-30"),
    MonitorDemoRoute("GR-573-E6-V2", 1L, "GR 573 - Itinéraire de substitution", "GR573_Itineraire-de-substitution_2015-12-29"),

    // GR 575-576 - A travers le Condroz

    MonitorDemoRoute("GR-576-E1", 8617677L, "GR 575-576 - Parcours Principal", "GR575-576_Pacours-principal_2020-02-26"),
    MonitorDemoRoute("GR-576-E1-L1", 1L, "GR 575-576 - Liaison AJ Namur", "GR575_Liaison-AJ-Namur_2022-01-18"),
    MonitorDemoRoute("GR-576-E1-L2", 1L, "GR 575-576 - Liaison Mozet, Gesves, Porcheresse", "GR575_Liaison_Mozet-Gesves-Porcheresse_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L3", 1L, "GR 575-576 - Liaison Les Avins, Grand Marchin", "GR575-576_Liaison_Les-Avins-Grand-Marchin_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L4", 1L, "GR 575-576 - Liaison Gare de Huy", "GR576_Liaison-Gare-Huy_2020-02-25"),
    // MonitorDemoRoute("GR-576-E1-L5", 1L, "GR 575-576 - Liaison GR 579 - Amay", "" /*"GR576-579_Liaison_2020-02-25" no longer in spreadsheet*/),
    MonitorDemoRoute("GR-576-E1-L6", 1L, "GR 575-576 - Liaison GR 5", "GR576-005_Liaison_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L7", 1L, "GR 575-576 - Liaison GR 5 et GR 573", "GR576-005-573_Liaison_2020-02-25"),

    // GR 579 - Bruxelles - Liège

    MonitorDemoRoute("GR-579-E2", 53358L, "GR 579 - Parcours Principal", "GR579_Parcours-principal_2022-03-22"),
    MonitorDemoRoute("GR-579-E2-L1", 1L, "GR 579 - Liaison Jesus-Eik", "GR579_Liaison-Jesus-Eik_2022-03-20"),
    MonitorDemoRoute("GR-579-E2-L2", 5470220L /* not in spreadsheet */ , "GR 579 - Liaison Amay", "GR579_Liaison-Amay_2022-03-20"),

    // La Grande Traversée de la Forêt du Pays de Chimay

    MonitorDemoRoute("GR-GTFPC-E2-P1", 1L, "GTFPC - Tronçon n°1 Macquenoise - Virelles", "GTFPC_Tronçon-n°1-Macquenoise-Virelles_2022-07-24"),
    MonitorDemoRoute("GR-GTFPC-E2-P2", 1L, "GTFPC - Tronçon n°2 Sivry - Virelles", "GTFPC_Tronçon-n°2-Sivry-Virelles_2022-07-30"),
    MonitorDemoRoute("GR-GTFPC-E2-P3", 1L, "GTFPC - Tronçon n°3 Virelles - Hastière", "GTFPC_Tronçon-n°3-Virelles-Hastière_2022-07-24"),
    MonitorDemoRoute("GR-GTFPC-E2-P3-V1", 1L, "GTFPC - Tronçon n°3 - Variante bivouac de Boussu", "GTFPC_Tronçon-n°3-Variante-bivouac-Boussu_2022-07-24"),
    MonitorDemoRoute("GR-GTFPC-E2-P4", 1L, "GTFPC - Tronçon n°4 Olloy - Philippeville", "GTFPC_Tronçon-n°4-Olloy-Philippeville_2022-07-24"),

    // L'Amblève par les GR

    MonitorDemoRoute("GR-TDA-E2", 1L, "TDA - Parcours Principal", "TDA_Parcours-principal_30-12-2019"),
    MonitorDemoRoute("GR-TDA-E2-V1", 1L, "TDA - Parcours principal variante castors", "TDA_Parcours-principal_Var-castors_30-12-2019"),
    MonitorDemoRoute("GR-TDA-E2-V2", 1L, "TDA - Parcours principal variante crue", "TDA_Parcours-principal_Var-crue_30-12-2019"),

    // Tour de la Wallonie Picarde

    MonitorDemoRoute("GRP-123-E2", 10494349L, "GRP 123 - Tour de la Wallonie Picarde", "GRP123_Parcours-principal_2022-02-18"),
    MonitorDemoRoute("GRP-123-E2-L1", 10493121L /* not in spreadsheet */ , "GRP 123 - Liaison Gare de Tournai", "GRP123_Liaison-Gare-de-Tournai_2021-04-15"),
    MonitorDemoRoute("GRP-123-E2-L2", 1L, "GRP 123 - Liaison GR5A - Deux Acren", "GRP123_Liaison_GR5A_Deux-Acren_2022-02-18"),
    MonitorDemoRoute("GRP-123-E2-L3", 1L, "GRP 123 - Liaison GR5A - Saint-Léger", "GRP123_Liaison_GR5A_Saint-Leger_2021-10-29"),
    MonitorDemoRoute("GRP-123-E2-V1", 10514878L /* not in spreadsheet */ , "GRP 123 - Variante de Silly", "GRP123_Variante-de-Silly_2021-04-15"),

    // GRP 125 - Tour de l'Entre-Sambre-et-Meuse

    MonitorDemoRoute("GRP-125-E3", 2927577L, "GRP 125 - Parcours-Principal", "GRP125_Parcours-principal_ 2022-02-10"),
    MonitorDemoRoute("GRP-125-E3-L1", 3472967L /* not in spreadsheet */ , "GRP 125 - Liaison G129 Beaumont", "GRP125-GR129-N_Liaison-Beaumont_2020-04-05"),
    MonitorDemoRoute("GRP-125-E3-L2", 1L, "GRP 125 - Liaison Lac du Valjoly (France)", "GRP125_Liaison-Lac-du-Valjoly_2018-02-19"),
    MonitorDemoRoute("GRP-125-E3-L3", 1L, "GRP 125 - Liaison AJ Namur", "GRP125_liaison-AJ-Rops_2017-10-25"),
    MonitorDemoRoute("GRP-125-E3-L4", 1L, "GRP 125 - Liaison Gare Namur", "GRP125_liaison-Gare-Namur_2018-02-19"),
    MonitorDemoRoute("GRP-125-E3-V1", 1L, "GRP 125 - Variante Senzeille", "GRP125_Var.Senzeille_2017-10-22"),
    MonitorDemoRoute("GRP-125-E3-V3", 1L, "GRP 125 - Variante Meuse à Freyr", "GRP125_Var-Meuse-Freyr_2016-08-11"),
    MonitorDemoRoute("GRP-125-E3-V4", 1L, "GRP 125 - Variante Fraire", "GRP125_Var.Fraire_2017-10-25"),

    // GRP 127 - Tour du Brabant Wallon

    MonitorDemoRoute("GRP-127-E1", 34122L, "GRP 127 - Parcours Principal", "GRP127_Parcours-principal_2022-04-28"),
    MonitorDemoRoute("GRP-127-E1-V1", 10965514L /* not in spreadsheet */ , "GRP 127 - Variante Pluie Quenast", "GRP127_Var.Pluie-Quenast_2018-01-25"),

    // GRP 151 - Tour du Luxembourg Belge

    MonitorDemoRoute("GRP-151-E2", 6447988L, "GRP 151 - Parcours Principal", "GRP151_Parcours-principal_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L1", 1L, "GRP 151 - Liaison gare de Libramont", "GRP151_Liaison-gare-Libramont_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L10", 1L, "GRP 151 - Liaison gare de Marche", "GRP151_Liaison-gare-Marche_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L11", 1L, "GRP 151 - Liaison gare de Neufchateau", "GRP151_Liaison-gare-Neufchateau_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L2", 1L, "GRP 151 - Liaison gare de Jemelle", "GRP151_Liaison-gare-Jemelle_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L3", 1L, "GRP 151 - Liaison gare de Marloie", "GRP151_Liaison-gare-Marloie_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L4", 1L, "GRP 151 - Liaison AJ Champlon - Nord de Journal", "GRP151_Liaison-AJ-Champlon-Nord-Journal_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L5", 1L, "GRP 151 - Liaison AJ Champlon - Est de Journal", "GRP151_Liaison-AJ-Champlon-Est-Journal_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L6", 1L, "GRP 151 - Liaison Martelange", "GRP151_Liaison-Martelange_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L7", 1L, "GRP 151 - Liaison gare de Habay", "GRP151_Liaison-gare-Habay_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L8", 1L, "GRP 151 - Liaison GR 16", "GRP151_Liaison-GR16_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-L9", 1L, "GRP 151 - Liaison gare de Forrières", "GRP151_Liaison-gare-Forrières_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-V1", 14006104L, "GRP 151 - Variante Chenogne - Marbehan", "GRP151_Variante-Chenogne-Marbehan_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-V3", 1L, "GRP 151 - Variante de Presseux", "GRP151_Variante-Presseux_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-V4", 14006040L, "GRP 151 - Variante Bodange - Neufchateau", "GRP151_Variante-Bodange-Neufchateau_2021-09-07"),

    // GRP 161 - Tour du Pays de Bouillon

    MonitorDemoRoute("GRP-161-E1", 14285420L, "GRP 161 - Parcours Principal", "GRP161_Parcours-principal_2022-05-14"),
    MonitorDemoRoute("GRP-161-E1-L1", 1L, "GRP 161 - Liaison gare de Bertrix", "GRP161_Liaison-gare-Bertrix_2022-05-14"),
    MonitorDemoRoute("GRP-161-E1-L2", 1L, "GRP 161 - Liaison gare Paliseul", "GRP161_Liaison-gare-Paliseul_2022-05-14"),
    MonitorDemoRoute("GRP-161-E1-L3", 1L, "GRP 161 - Liaison gare Graide", "GRP161_Liaison-gare-Graide_2022-05-14"),
    MonitorDemoRoute("GRP-161-E1-L4", 1L, "GRP 161 - Laison AJ Bouillon", "GRP161_Laison-AJ-Bouillon_2022-05-14"),
    MonitorDemoRoute("GRP-161-E1-R1", 1L, "GRP 161 - Raccourci évitant Herbeumont", "GRP161_Raccourci-évitant-Herbeumont_2022-05-14"),
    MonitorDemoRoute("GRP-161-E1-V1", 1L, "GRP 161 - Variante Les Hayons", "GRP161_Variante-Les-Hayons_2022-05-14"),
    MonitorDemoRoute("GRP-161-E1-V2", 1L, "GRP 161 - Variante Laforêt - Pont de claies", "GRP161_Variante-Laforêt-Pont-claies_2022-05-14"),

    // GRP 563 - Tour du pays de Herve

    MonitorDemoRoute("GRP-563-E3", 1696127L, "GRP 563 - Parcours Principal", "GRP563_Parcours-principal_2022-02-21 (1)"),
    MonitorDemoRoute("GRP-563-E3-L1", 1L, "GRP 563 - Liaison Verviers", "GRP563_GR573_Liaison-Verviers-2016-10-17"),
    MonitorDemoRoute("GRP-563-E3-L2", 1L, "GRP 563 - Liaison Aix-La-Chapelle", "GRP563_Liaison_Aix-2018-02-06"),
    MonitorDemoRoute("GRP-563-E3-V1", 1L, "GRP 563 - Variante Soiron - Grand Rechain", "GRP563_Var-Soiron-Gd-Rechain-2018-02-06"),

    // GRP 571 - Tour des Vallées des Légendes - Amblève - Salm - Lienne

    MonitorDemoRoute("GRP-571-E7", 3672059L, "GRP 571 - Parcours Principal", "GRP571_Parcours-principal_2022-03-05"),
    MonitorDemoRoute("GRP-571-E7-L1", 1L, "GRP 571 - Liaison GR 15", "GRP571_Liaison-GR015_2022-03-05"),
    MonitorDemoRoute("GRP-571-E7-L2", 1L, "GRP 571 - Liaison GR 15 - Variante Crue", "GRP571_Liaison-GR015-Variante-Crue_2022-03-05"),
    MonitorDemoRoute("GRP-571-E7-L3", 1L, "GRP 571 - Liaison GR 57", "GRP571_Liaison-GR057_2022-03-05"),
    MonitorDemoRoute("GRP-571-E7-L4", 1L, "GRP 571 - Liaison Gare Rivage", "GRP571_Liaison-Gare-Rivage_2022-03-05"),
    MonitorDemoRoute("GRP-571-E7-L5", 1L, "GRP 571 - Liaison Gare Gouvy", "GRP571_Liaison-Gare-Gouvy_2022-03-05"),
    MonitorDemoRoute("GRP-571-E7-L6", 1L, "GRP 571 - Liaison Gare Vielsalm", "GRP571_Liaison-Gare-Vielsalm_2022-03-05"),
    MonitorDemoRoute("GRP-571-E7-V1", 1L, "GRP 571 - Variante Crue", "GRP571_Variante-crue-Fds-Quarreux_2022-03-05"),

    // GRP 577 - Tour de la Famenne - no longer in spreadsheet

    MonitorDemoRoute("GRP-577-E4", 2301540L, "GRP 577 - Parcours Principal", "GRP577_Parcours-principal_2022-03-21"),
    // no longer in spreadsheet: MonitorDemoRoute("GRP-577-E4-L1", 1L, "GRP 577 - AR Monastère Chevetogne", "GRP577-Liaison_Monastere-de-Chevetogne_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L2", 1L, "GRP-577 - Liaison GR 17", "GRP577-GR017_Liaison_2021-05-05"),
    MonitorDemoRoute("GRP-577-E4-L3", 1L, "GRP-577 - Liaison gare de Marche", "GRP577-Liaison-gare_Marche-en-Famenne_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L4", 1L, "GRP-577 - Liaison gare de Beauraing", "GRP577-Liaison-Gare_Beauraing_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L5", 1L, "GRP-577 - Liaison gare de Barvaux", "GRP577-Liaison-Gare_Barvaux_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L6", 1L, "GRP-577 - Liaison gare de Jemelle", "GRP577-Liaison-Gare_Jemelle_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V1", 1L, "GRP 577 - Variante Sinsin", "GRP577-Variante_Serinchamp_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V2", 1L, "GRP 577 - Variante Revogne", "GRP577_Variante-Revogne_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V3", 1L, "GRP 577 - Variante Chevetogne", "GRP577_Variante-Chevetogne_2022-02-02"),
    MonitorDemoRoute("GRP-577-E4-V4", 1L, "GRP 577 - Variante Géopark", "GRP577-Variante_Geopark_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V5", 1L, "GRP 577 - Variante Les Pairées", "GRP577-Variante_Les-Pairees_2021-05-06"),

    // SAT - Sentier des Abbayes Trappistes de Wallonie

    MonitorDemoRoute("GRT-SAT-E3-P1", 13950957L, "SAT - Tronçon Chimay - Rochefort", "SAT_Tronçon-Chimay-Rochefort_2022-05-03"),
    MonitorDemoRoute("GRT-SAT-E3-P2", 13950957L, "SAT - Tronçon Rochefort - Orval", "SAT_Tronçon-Rochefort-Orval_2022-05-03"),
    MonitorDemoRoute("GRT-SAT-E3-L1", 1L, "SAT - Liaison Gare Paliseul", "SAT_Liaison-gare-Paliseul_2022-01-11"),
    MonitorDemoRoute("GRT-SAT-E3-L2", 1L, "SAT - Liaison Gare Bertrix", "SAT_Liaison-Gare-Bertrix_2022-04-13"),
    MonitorDemoRoute("GRT-SAT-E3-L3", 1L, "SAT - Liaison Gare Florenville", "SAT_Liaison-Gare-Florenville_2022-04-13"),
    MonitorDemoRoute("GRT-SAT-E3-V1", 1L, "SAT - Variante de crue Furfooz", "SAT_Chimay-Rochefort_VarianteCrue-Furfooz_2020-06-05"),
    MonitorDemoRoute("GRT-SAT-E3-V2", 1L, """SAT - Variante crue entre "Daverdisse et Lesse"""", "SAT_Rochefort-Orval_VarianteCrue_Daverdisse-Lesse_2020-06-04"),

    // SMA - Sentier des Monts Ardenne

    MonitorDemoRoute("GRT-SMA-E2", 1L, "SMA - Parcours Complet", "SMA_Parcours-Complet_2022-05-30"),
  )

  val grVlaanderenRoutes: Seq[MonitorDemoRoute] = Seq(

    MonitorDemoRoute("P-E2-verbinding", 13638L, "E2 - Verbinding GR 5A – GR 565 Kruibeke – Deurne", "routeyou-e2verbinding"),
    MonitorDemoRoute("P-GR12A-vl", 13632L, "GR 12 Amsterdam – Parijs – Hoofdtraject", "routeyou-gr12a-damparijs-vl"),
    MonitorDemoRoute("P-GR12-west", 6296002L, "GR 12 Amsterdam – Parijs – Variant West	", "routeyou-gr12-variantwest"),
    MonitorDemoRoute("P-GR-12-kleetbos", 112820L, "GR 12 Amsterdam – Parijs – Variant Kleetbos	", "routeyou-gr-12-variant-kleetbos"),
    MonitorDemoRoute("P-GR-122-scheldeland", 166227L, "GR 122 Scheldeland	", "routeyou-gr-122-scheldeland"),
    MonitorDemoRoute("P-GR126", 107242L, "GR 126 Brussegem – Mariekerke	", "routeyou-gr126"),
    MonitorDemoRoute("P-GR128", 2067765L, "GR 128 Vlaanderenroute – Hoofdtraject	", "routeyou-gr128"),
    MonitorDemoRoute("P-GR128-schoonaarde", 11558558L, "GR 128 Vlaanderenroute – Aanlooproute Schoonaarde	", "routeyou-gr128naarschoonaarde"),
    MonitorDemoRoute("P-GR128-teuven-aken", 11659292L, "GR 128 Vlaanderenroute – Duits deeltraject: Teuven-Aken	", "routeyou-gr128teuvenaken"),
    MonitorDemoRoute("P-GR128-bailleul-kemmel", 11659239L, "GR 128 Vlaanderenroute – Frans deeltraject: Bailleul-Kemmel	", "routeyou-gr128bailleulkemmel"),
    MonitorDemoRoute("P-GR128-gent", 11558750L, "GR 128 Vlaanderenroute – Variant Gent-Destelbergen	", "routeyou-gr128vargent"),
    MonitorDemoRoute("P-GR128-leuven", 11581538L, "GR 128 Vlaanderenroute – Variant Leuven	", "routeyou-gr128variantleuven"),
    MonitorDemoRoute("P-GR128-sttruiden", 5182874L, "GR 128 Vlaanderenroute – Variant St-Truiden	", "routeyou-gr128varsttruiden"),
    MonitorDemoRoute("P-GR128-tongeren", 11558514L, "GR 128 Vlaanderenroute – Variant Tongeren	", "routeyou-gr128varianttongeren"),
    MonitorDemoRoute("P-GR129", 204077L, "GR 129 Dwars door België | Vlaanderen	", "routeyou-gr129"),
    MonitorDemoRoute("P-GR-130-ijzer", 2583905L, "GR 130 IJzer – Hoofdtraject	", "routeyou-gr-130-ijzer"),
    MonitorDemoRoute("P-GR130-oostcappel", 4210125L, "GR 130 IJzer – Wintervariant Oostcappel	", "routeyou-gr130varoostcappel"),
    MonitorDemoRoute("P-GR130-roesbrugge", 9769052L, "GR 130 IJzer – Wintervariant Roesbrugge	", "routeyou-gr130varroesbrugge"),
    MonitorDemoRoute("P-GR131", 2459560L, "GR 131 Brugse Ommeland – Ieperboog – Hoofdtraject	", "routeyou-gr131"),
    MonitorDemoRoute("P-GR131-ieper", 1691376L, "GR 131 Brugse Ommeland – Ieperboog – Aanlooproute station Ieper	", "routeyou-gr131aanloopieper"),
    MonitorDemoRoute("P-GR5-1", 3121667L, "GR 5 Noordzee - Middellandse Zee | Vlaanderen	", "routeyou-gr5noordzeemz"),
    MonitorDemoRoute("P-GR5-2", 13918842L, "GR 5 Noordzee - Middellandse Zee | Vlaanderen	", "routeyou-gr5noordzeemz"),
    MonitorDemoRoute("P-GR5-genk", 5951316L, "GR 5 Noordzee - Middellandse Zee | Vlaanderen – Variant Genk	", "routeyou-gr5vargenk"),
    MonitorDemoRoute("P-GR5-hasselt", 2907324L, "GR 5 Noordzee - Middellandse Zee | Vlaanderen – Variant Hasselt	", "routeyou-gr5varhasselt"),
    MonitorDemoRoute("P-GR5-hogefronten", 0L, "GR 5 Noordzee - Middellandse Zee | Vlaanderen – Variant Maastricht (Hoge Fronten)	", "routeyou-gr5varhogefronten"),
    MonitorDemoRoute("P-GR5-testelt-zichem", 2923308L, "GR 5 Noordzee - Middellandse Zee | Vlaanderen – Variant Testelt-Zichem (Demervariant)	", "routeyou-gr5vartesteltzichem"),
    MonitorDemoRoute("P-GR512", 1860840L, "GR 512 Brabantse Heuvelroute – Hoofdtraject	", "routeyou-gr512"),
    MonitorDemoRoute("P-GR512-bivak", 6315832L, "GR 512 Brabantse Heuvelroute – Variant Bivakzone Meerdaalwoud	", "routeyou-gr512varbivak"),
    MonitorDemoRoute("P-GR561", 144503L, "GR 561 Kempen – Maaspad – Hoofdtraject	", "routeyou-gr561"),
    MonitorDemoRoute("P-GR561-bree", 0L, "GR 561 Kempen – Maaspad – Aanlooproute Bree	", "routeyou-gr561bree"),
    MonitorDemoRoute("P-GR561-balen", 3351080L, "GR 561 Kempen – Maaspad – Aanlooproute station Balen	", "routeyou-gr561stationbalen"),
    MonitorDemoRoute("P-GR561-geel", 3231956L, "GR 561 Kempen – Maaspad – Aanlooproute station Geel	", "routeyou-gr561stationgeel"),
    MonitorDemoRoute("P-GR561-hamont", 0L, "GR 561 Kempen – Maaspad – Aanlooproute station Hamont	", "routeyou-gr561stationhamont"),
    MonitorDemoRoute("P-GR561-neerpelt", 9822961L, "GR 561 Kempen – Maaspad – Aanlooproute station Neerpelt	", "routeyou-gr561stationneerpelt"),
    MonitorDemoRoute("P-GR561-fronten", 0L, "GR 561 Kempen – Maaspad – Variant Hoge Fronten	", "routeyou-gr561fronten"),
    MonitorDemoRoute("P-GR564-loonse", 2099432L, "GR 564 De Loonse Route	", "routeyou-gr564loonse"),
    MonitorDemoRoute("P-GR565", 2361924L, "GR 565 Sniederspad	", "routeyou-gr565"),
    MonitorDemoRoute("P-GR565-apen", 9823786L, "GR 565 Sniederspad – Aanlooproute station Antwerpen	", "routeyou-gr565stationa-pen"),
    MonitorDemoRoute("P-GR565-arendonk", 12324169L, "GR 565 Sniederspad – Variant Arendonk	", "routeyou-gr565vararendonk"),
    MonitorDemoRoute("P-GR565-watermolen", 0L, "GR 565 Sniederspad – Variant Watermolen	", "routeyou-gr565varwatermolen"),
    MonitorDemoRoute("P-GR5A-vl1", 13658L, "GR 5A Wandelronde van Vlaanderen	", "routeyou-gr5a-wandelrondevl"),
    MonitorDemoRoute("P-GR5A-vl2", 2629186L, "GR 5A Wandelronde van Vlaanderen	", "routeyou-gr5a-wandelrondevl"),
    MonitorDemoRoute("P-GR5A-poperinge", 0L, "GR 5A Wandelronde van Vlaanderen – Aanlooproute Poperinge	", "routeyou-gr5aaanlooppoperinge"),
    MonitorDemoRoute("P-GR5A-haven", 3194117L, "GR 5A Wandelronde van Vlaanderen – Havenvariant	", "routeyou-gr5avarhaven"),
    MonitorDemoRoute("P-GR5a-variantbrugge", 0L, "GR 5A Wandelronde van Vlaanderen – Variant Brugge	", "routeyou-gr5a-variantbrugge"),
    MonitorDemoRoute("P-GR-apen-noord-zuid", 0L, "Stads-GR Antwerpen Noord-Zuid	", "routeyou-stads-gr-antwerpen-noord-zuid"),
    MonitorDemoRoute("P-GR-apen-oost-west", 0L, "Stads-GR Antwerpen Oost-West	", "routeyou-stads-gr-antwerpen-oost-west"),
    MonitorDemoRoute("P-GR-apen-rondom", 2436557L, "Stads-GR Antwerpen Rondom	", "routeyou-stads-gr-antwerpen-rondom"),
    MonitorDemoRoute("P-SGR-dijleland", 71268L, "Streek-GR Dijleland	", "routeyou-sgrdijleland"),
    MonitorDemoRoute("P-SGR-dijlelandlus1", 0L, "Streek-GR Dijleland – Lus 1: Rond Leuven	", "routeyou-sgrdijlelandlus1"),
    MonitorDemoRoute("P-SGR-dijlelandlus2", 0L, "Streek-GR Dijleland – Lus 2: Zuid-Dijleland vanuit Neerijse	", "routeyou-sgrdijlelandlus2"),
    MonitorDemoRoute("P-SGR-dijlelandlus3", 0L, "Streek-GR Dijleland – Lus 3: Zuid-Dijleland vanuit Tervuren	", "routeyou-sgrdijlelandlus3"),
    MonitorDemoRoute("P-SGR-dijlelandlus4", 0L, "Streek-GR Dijleland – Lus 4: Zuid-Dijleland uitgebreid vanuit Tervuren	", "routeyou-sgrdijlelandlus4"),
    MonitorDemoRoute("P-SGR-dijlelandlus5", 0L, "Streek-GR Dijleland – Lus 5: Noord-Dijleland vanuit Mechelen	", "routeyou-sgrdijlelandlus5"),
    MonitorDemoRoute("P-SGR-dijlelandvar", 1781593L, "Streek-GR Dijleland – Variant Leefdaal-Neerijse	", "routeyou-sgrdijlelandvar"),
    MonitorDemoRoute("P-SGR-GG", 8204223L, "Streek-GR Groene Gordel	", "routeyou-sgrgroenegordel"),
    MonitorDemoRoute("P-SGR-GG-huizingen", 8216694L, "Streek-GR Groene Gordel –  Aanlooproute Huizingen	", "routeyou-sgrgghuizingen"),
    MonitorDemoRoute("P-SGR-hageland", 2067796L, "Streek-GR Hageland	", "routeyou-streek-gr-hageland"),
    MonitorDemoRoute("P-SGR-hageland-leuven", 0L, "Streek-GR Hageland – Aanlooproute Leuven	", "routeyou-streek-gr-hageland-aanlooproute-leuven"),
    MonitorDemoRoute("P-SGR-haspengouw", 0L /*5150605L super relation */ , "Streek-GR Haspengouw	", "routeyou-sgrhaspengouw"),
    MonitorDemoRoute("P-SGR-haspengouw-n", 0L, "Streek-GR Haspengouw – Noordelijke doorsteek	", "routeyou-sgrhaspengouwn"),
    MonitorDemoRoute("P-SGR-haspengouw-ow", 0L, "	Streek-GR Haspengouw – Variant Oost-West	", "routeyou-sgrhaspengouwvarow"),
    MonitorDemoRoute("P-SGR-haspengouw-z", 0L, "Streek-GR Haspengouw – Zuidelijke doorsteek	", "routeyou-sgrhaspengouwz"),
    MonitorDemoRoute("P-SGR-heuvelland", 5543597L, "Streek-GR Heuvelland	", "routeyou-sgrheuvelland"),
    MonitorDemoRoute("P-SGR-kempen", 0L /*9514644L super relation*/ , "Streek-GR Kempen	", "routeyou-sgrkempen"),
    MonitorDemoRoute("P-SGR-kempen-geel", 9514643L, "Streek-GR Kempen – Aanlooproute Geel	", "routeyou-sgrkempengeel"),
    MonitorDemoRoute("P-SGR-kempen-herentals", 3958805L, "Streek-GR Kempen – Aanlooproute Herentals	", "routeyou-sgrkempenherentals"),
    MonitorDemoRoute("P-SGR-kempen-to", 163234L, "Streek-GR Kempen – Variant Turnhout-Oostmalle	", "routeyou-sgrkempenvarto"),
    MonitorDemoRoute("P-SGR-kempen-winter", 9514642L, "Streek-GR Kempen – Variant Winter	", "routeyou-sgrkempenvarwinter"),
    MonitorDemoRoute("P-SGR-molom", 7791375L, "Streek-GR Mol Om	", "routeyou-sgrmolom"),
    MonitorDemoRoute("P-SGR-uilenspiegel", 2524951L, "Streek-GR Uilenspiegel	", "routeyou-sgruilenspiegel"),
    MonitorDemoRoute("P-SGR-uilenspiegel-kreken", 0L, "Streek-GR Uilenspiegel – Krekenvariant	", "routeyou-sgruilenspiegelvar"),
    MonitorDemoRoute("P-SGR-va", 1286694L, "Streek-GR Vlaamse Ardennen	", "routeyou-sgrvlardennen"),
    MonitorDemoRoute("P-SGR-va-oudenaarde", 0L, "Streek-GR Vlaamse Ardennen – Aanlooproute Oudenaarde	", "routeyou-sgrvlardoudenaarde"),
    MonitorDemoRoute("P-SGR-wr", 16904L, "Streek-GR Waas- en Reynaertland	", "routeyou-sgrwaas-enreynaert"),
    MonitorDemoRoute("P-SGR-wr-lokeren", 0L, "Streek-GR Waas- en Reynaertland – Variant Lokeren-Daknam	", "routeyou-sgrwrvarlokeren")
  )
}

case class MonitorDemoRoute(name: String, routeId: Long, description: String, filename: String) {
}
