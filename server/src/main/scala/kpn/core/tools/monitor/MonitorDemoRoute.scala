package kpn.core.tools.monitor

object MonitorDemoRoute {

  val routes: Seq[MonitorDemoRoute] = Seq(

    // GR 5 - Mer du Nord - Méditerranée - Tronçon Wallonie et Grand-Duché de Luxembourg

    MonitorDemoRoute("GR-5-E7", 3121668L, "GR 5 - Parcours Principal", "GR005_Parcours-principal_2021-05-06"),
    MonitorDemoRoute("GR-5-E7-L1", 1L, "GR 5 - Liaison GR 576", "GR005_Liaison_GR576_2016-01-07"),
    MonitorDemoRoute("GR-5-E7-L2", 1L, "GR 5 - Liaison GR 573 et GR 576", "GR005_Liaison_GR573-GR576_2015-12-29"),

    // GR 12 - Amsterdam - Bruxelles - Paris

    // 12825429L is parent relation for:
    //   150957    GR12 - Heide
    //   13632     GR12 Amsterdam-Paris
    //   6296002   GR 12 Variant West
    //   12825428  Amsterdam-Paris, France
    // MonitorDemoRoute("GR-12-E4", 12825429L, "GR 12 - Parcours Principal", "GR012_Parcours-principal_2021-09-08"),
    MonitorDemoRoute("GR-12-E4", 13632L, "GR 12 - Parcours Principal", "GR012_Parcours-principal_2021-09-08"),
    MonitorDemoRoute("GR-12-E4-L1", 1L, "GR 12 - Liaison GR 126", "GR012_Liaison_GR126_2020-02-01"),

    // Monschau - Bouillon - Sedan

    MonitorDemoRoute("GR-14-E3", 2929186L, "GR 14 - Parcours Principal", "GR014_Parcours-principal_2021-07-31"),
    MonitorDemoRoute("GR-14-E3-L1", 1L, "GR 14 - Liaison AJ Champlon - Nord de Journal", "GR014-GR151_Liaison-AJ-Champlon-Av-Journal_2018-04-04"),
    MonitorDemoRoute("GR-14-E3-L2", 1L, "GR 14 - Liaison AJ Champlon - Est de Journal", "GR014-GR151_Liaison-AJ-Champlon-Apr-Journal_2018-04-04"),
    MonitorDemoRoute("GR-14-E3-V1", 1L, "GR 14 - Variante Crue Reinhardstein", "GR014-GR056_Var-Crue-Reinhardstein_2019-10-26"),
    MonitorDemoRoute("GR-14-E3-V2", 1L, "GR 14 - Variante Réserve Naturelle Orti", "GR014-GR057_Var-ResNat-Orti_2020-06-01"),
    MonitorDemoRoute("GR-14-E3-V3", 10285544L /* not in spreadsheet */ , "GR 14 - Variante Crue Semois", "GR014_Var-Crue-Semois_2018-04-04"),
    MonitorDemoRoute("GR-14-E3-a", 10291197L, "GR014 Liaison AJ Champlon 1 (not in spreadsheet, delete?)", "GR014_Liaison-AJ-Champlon-1_2018-04-04"), // OK
    MonitorDemoRoute("GR-14-E3-b", 10291210L, "GR014 Liaison AJ Champlon 2 (not in spreadsheet, delete?)", "GR014_Liaison-AJ-Champlon-2_2018-04-04"), // OK

    // De la Lorraine belge à l’Eifel

    MonitorDemoRoute("GR-15-E4", 8613893L, "GR 15 - De la Lorraine belge à l’Eifel", "GR015_Parcours-principal_2021-02-17"),
    MonitorDemoRoute("GR-15-E4-L1", 1L, "GR 15 - Liaison entre le GR 15 et le GR 151, E3", "GR015_Liaison-GR151,E3_2020-04-28"),
    MonitorDemoRoute("GR-15-E4-L2", 1L, "GR 15 - Liaison entre le GR 15 et le GR 571", "GR015_Liaison-GRP571_2019-07-03"),
    MonitorDemoRoute("GR-15-E4-L3", 9157328L /* not in spreadsheet */ , "GR 15 - Liaison Gare Arlon", "GR015-GR129_Liaison-Gare-Arlon_2021-02-17"),
    MonitorDemoRoute("GR-15-E4-V1", 4163012L /* not in spreadsheet */ , "GR 15 - Variante Mardasson", "GR015_Var.Mardasson_2020-04-28"),
    MonitorDemoRoute("GR-15-E4-V2", 9156234L /* not in spreadsheet */ , "GR 15 - Variante Charmille", "GR015_Var-Charmille_2019-03-03"),
    MonitorDemoRoute("GR-15-E4-V3", 1L, "GR 15 - Variante Fagne de Nahtsief", "GR015_Var-Fagne-de-Nahtsief_2019-03-03"),

    // GR 16 - Sentier de la Semois

    MonitorDemoRoute("GR-16-E7", 197843L, "GR 16 - Parcours principal", "GR016-Parcours-principal_2021-03-24"),
    MonitorDemoRoute("GR-16-E7-L1", 1L, "GR 16 - Liaison gare Arlon", "GR016-Liaison-gare-Arlon_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-L2", 1L, "GR 16 - Liaison gare Florenville", "GR016-Liaison-gare-Florenville_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-L3", 7854056L /* not in spreadsheet */ , "GR 16 - Liaison GR151", "GR016-Liaison-GR151_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V1", 1L, "GR 16 - Variante source Semois", "GR016-Variante-source-Semois_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V2", 1L, "GR 16 - Variante Les Bulles", "GR016-Variante-Les-Bulles_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V3", 7854108L, "GR 16 - Variante Roche Lenel", "GR016-Variante-Roche-Lenel_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V4", 1L, "GR 16 - Variante Herbeumont", "GR016-Variante-Herbeumont_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V5", 7857070L, "GR 16 - Variante Mortehan", "GR016-Variante-Mortehan_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V6", 7862661L /* not in spreadsheet */ , "GR 16 - Variante Les Hayons", "GR016-Variante-Les-Hayons_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V7", 4161833L /* not in spreadsheet */ , "GR 16 - Variante Les échelles", "GR016-Variante-Les-Echelles_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V8", 7860298L /* not in spreadsheet */ , "GR 16 - Variante Frahan", "GR016-Variante-Frahan_2021-03-18"),
    MonitorDemoRoute("GR-16-E7-V9", 7860671L /* not in spreadsheet */ , "GR 16 - Variante Laforet", "GR016-Variante-Laforet_2021-03-18"),

    // GR 17 - Lesse et Lomme",

    MonitorDemoRoute("GR-17-E1-P1", 1L, "GR17 Lesse Parcours principal", "GR017-Lesse_Parcours-principal_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-P2", 1L, "GR17 Lomme Parcours principal", "GR017-Lomme_Parcours-principal_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-L1", 1L, "GR17 Lesse Liaison GR577", "GR017-Lesse_Liaison-GR577_2021-04-27"),
    MonitorDemoRoute("GR-17-E1-L2", 1L, "GR17 Lomme Liaison gare de Jemelle", "GR017-Lomme_Liaison-gare-de-Jemelle_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-L3", 1L, "GR17 Lomme Liaison GRP151", "GR017-Lomme_Liaison-GRP151_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V1", 1L, "GR17 Lesse Variante de la Bouchaille", "GR017-Lesse_Variante-de-la-Bouchaille_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V2", 1L, "GR17 Lesse Variante crue Lesse-Daverdisse", "GR017-Lesse_Variante-crue-Lesse-Daverdisse_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V3", 1L, "GR17 Lesse Variante des Pairées", "GR017-Lesse_Variante-des-Pairées_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V4", 1L, "GR17 Lesse Variante Crue Résurgence de la Lomme", "GR017-Lesse_Variante-crue-résurgence-Lomme_2021-04-09"),
    MonitorDemoRoute("GR-17-E1-V5", 1L, "GR17 Lomme Variante Variante de Bure", "GR017-Lomme_Variante-de-Bure_2021-04-09"),

    // GR 56 - Cantons de l'Est et Parc Naturel Hautes Fagnes - Eifel",

    MonitorDemoRoute("GR-56-E7", 1477696L, "GR 56 - Parcours Principal", "GR056_Parcours-principal_2020-09-20"),
    MonitorDemoRoute("GR-56-E7-L1", 1L, "GR 56 - Liaison Variante Warche-Krinkelt", "GR056_Liaison-Variante_Warche-Krinkelt_2016-08-11"),
    MonitorDemoRoute("GR-56-E7-L2", 1L, "GR 56 - Liaison Bayehon", "GR056_Liaison-Bayehon_2018-03-25"),
    MonitorDemoRoute("GR-56-E7-V1", 1L, "GR 56 - Variante Amblève", "GR056_Var.Amblève_2016-08-11"),
    MonitorDemoRoute("GR-56-E7-V2", 1L, "GR 56 - Variante Warche", "GR056_Var.Warche_2018-03-25"),

    //    MonitorDemoRoute("GR057-a", 9308683L, "GR057_Sentier-de-l_Ourthe-Occidentale_Parcours-principal_2021-05-02"), // OK
    //    MonitorDemoRoute("GR057-b", 157527L, "GR057_Sentier-de-l_Ourthe-Orientale_Parcours-principal_2021-05-02"), // OK
    //    MonitorDemoRoute("GR057-c", 9294292L, "GR057_Sentier-de-l_Ourthe_Parcours-principal_2021-05-04"), // OK


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
    MonitorDemoRoute("GR-126-E5", 33768L, "GR 126 - Parcours Principal", "GR126_Parcours-principal_2021-08-10"),
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
    MonitorDemoRoute("GR-129S-E1", 2459527L, "GR 129 Sud - Parcours Principal", "GR129S_Parcours-principal_2020-08-08"),
    MonitorDemoRoute("GR-129S-E1-L1", 1L, "GR 129 Sud - Liaison Gare Florenville", "GR129S_Liaison-gare-Florenville_2017-11-17"),
    MonitorDemoRoute("GR-129S-E1-L2", 1L, "GR 129 Sud - Liaison Gare Arlon", "GR129-GRP125_Liaison-Beaumont_2021-02-05"),
    MonitorDemoRoute("GR-129S-E1-V1", 1L, "GR 129 Sud - Variante Crue entre Daverdisse et Lesse", "GR129S_VarianteCrue-Daverdisse-Lesse_2020-06-26"),

    // GR 129 - La Belgique en diagonale ! Tronçon Ellezelles - Dinant

    // GR-129W-E3: relation 12204688L referenced in spreadsheet has been deleted, using 2521808 instead
    MonitorDemoRoute("GR-129W-E3", 2521808L, "GR 129 - Parcours Principal", "GR129_Parcours-Principal_2021-10-11"),
    MonitorDemoRoute("GR-129W-E3-L1", 1L, "GR 129 - Liaison GR 129 et GRP 125", "GR129-GRP125_Liaison-Beaumont_2021-02-05"),
    MonitorDemoRoute("GR-129W-E3-L2", 1L, "GR 129 - Liaison GR 129 et GR 122 dite Liaison-des-Haut-Pays", "GR129-GR122_Liaison-Haut-Pays_2021-02-05"),

    // GR 412 - Sentier des Terrils",

    MonitorDemoRoute("GR-412-E2", 157373L, "GR412 - Parcours Principal", "GR412_Parcours-principal_2021-10-19"),
    MonitorDemoRoute("GR-412-E2-B1", 9644845L /* not in spreadsheet */ , "GR412 - Boucle 1 - Boucle Noire", "GR412_Boucle-Noire_2021-11-13"),
    MonitorDemoRoute("GR-412-E2-B2", 9994009L /* not in spreadsheet */ , "GR412 - Boucle 2 - Les Coteaux", "GR412_Boucle_des_Coteaux_2021-09-29"),
    MonitorDemoRoute("GR-412-E2-B3", 9994110L /* not in spreadsheet */ , "GR412 - Boucle 3 -  Hasard-Bas Bois", "GR412_Boucle-Hasard_Bas-Bois_2021-09-29"),
    MonitorDemoRoute("GR-412-E2-L1", 13481799L /* not in spreadsheet */ , "GR412 - Liaison gare Angleur", "GR412_Liaison-Gare-Angleur_2021-02-12"),
    // MonitorDemoRoute("GR-412-E2-V1",1L, "GR 412 Var Crue Moignelée", ""),
    MonitorDemoRoute("GR-412-E2-a", 12420102L, "GR412 - Liaison Gare Namur (not in spreadsheet, delete?)", "GR412_Liaison-Gare-Namur_2021-02-12"), // OK

    // GR 573 - Vesdre et Hautes Fagnes

    MonitorDemoRoute("GR-573-E6", 166219L, "GR 573 - Parcours Principal", "GR573_Pacours-principal_2021-02-14"),
    MonitorDemoRoute("GR-573-E6-L1", 1L, "GR 573 - Liaison GR 5 et GR 576", "GR573_Liaison_GR5-GR576_2015-12-29"),
    MonitorDemoRoute("GR-573-E6-L2", 1L, "GR 573 - Liaison GRP 563 et Verviers", "GR573_Liaison_GR563-Verviers_2016-10-17"),
    MonitorDemoRoute("GR-573-E6-L3", 1L, "GR 573 - Liaison GR 412", "GR573_Liaison_GR412_2015-12-29"),
    MonitorDemoRoute("GR-573-E6-V1", 1L, "GR 573 - Variante Spa", "GR573_Var-Spa_2021-02-14"),
    MonitorDemoRoute("GR-573-E6-V2", 1L, "GR 573 - Itinéraire de substitution", "GR573_Itineraire-de-substitution_2015-12-29"),

    // GR 575-576 - A travers le Condroz

    MonitorDemoRoute("GR-576-E1", 8617677L, "GR 575-576 - Parcours Principal", "GR575-576_Pacours-principal_2020-02-26"),
    MonitorDemoRoute("GR-576-E1-L1", 1L, "GR 575-576 - Liaison AJ Namur", "GR575_Liaison-AJ-Namur_2020-05-20"),
    MonitorDemoRoute("GR-576-E1-L2", 1L, "GR 575-576 - Liaison Mozet, Gesves, Porcheresse", "GR575_Liaison_Mozet-Gesves-Porcheresse_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L3", 1L, "GR 575-576 - Liaison Les Avins, Grand Marchin", "GR575-576_Liaison_Les-Avins-Grand-Marchin_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L4", 1L, "GR 575-576 - Liaison Gare de Huy", "GR576_Liaison-Gare-Huy_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L5", 1L, "GR 575-576 - Liaison GR 579 - Amay", "GR576-579_Liaison_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L6", 1L, "GR 575-576 - Liaison GR 5", "GR576-005_Liaison_2020-02-25"),
    MonitorDemoRoute("GR-576-E1-L7", 1L, "GR 575-576 - Liaison GR 5 et GR 573", "GR576-005-573_Liaison_2020-02-25"),

    // GR 579 - Bruxelles - Liège

    MonitorDemoRoute("GR-579-E2", 53358L, "GR 579 - Parcours Principal", "GR579-Parcours-principal_2021-09-28"),
    MonitorDemoRoute("GR-579-E2-L1", 1L, "GR 579 - Liaison Jesus-Eik", "GR579_Laison_Jezus-Eik"),
    MonitorDemoRoute("GR-579-E2-L2", 5470220L /* not in spreadsheet */ , "GR 579 - Liaison Amay", "GR579_Liaison-Amay"),

    // La Grande Traversée de la Forêt du Pays de Chimay

    MonitorDemoRoute("GR-GTFPC-E2-P1", 1L, "GTFPC - Tronçon n°1 Macquenoise - Virelles", "GTFPC-1_Macquenoise-Virelles_2018-03-07"),
    MonitorDemoRoute("GR-GTFPC-E2-P2", 1L, "GTFPC - Tronçon n°2 Sivry - Virelles", "GTFPC-2_Sivry-Virelles_2018-03-07"),
    MonitorDemoRoute("GR-GTFPC-E2-P3", 1L, "GTFPC - Tronçon n°3 Virelles - Hastière", "GTFPC-3_Virelles-Hastiere_2018-03-07"),
    MonitorDemoRoute("GR-GTFPC-E2-P4", 1L, "GTFPC - Tronçon n°4 Olloy - Philippeville", "GTFPC-4_Olloy-Philippeville_2021-09-11"),

    // L'Amblève par les GR

    MonitorDemoRoute("GR-TDA-E2", 1L, "TDA - Parcours Principal", "TDA_Parcours-principal_30-12-2019"),
    MonitorDemoRoute("GR-TDA-E2-V1", 1L, "TDA - Parcours principal variante castors", "TDA_Parcours-principal_Var-castors_30-12-2019"),
    MonitorDemoRoute("GR-TDA-E2-V2", 1L, "TDA - Parcours principal variante crue", "TDA_Parcours-principal_Var-crue_30-12-2019"),

    // Tour de la Wallonie Picarde

    MonitorDemoRoute("GRP-123-E2", 10494349L, "GRP 123 - Tour de la Wallonie Picarde", "GRP123_Parcours-principal-temporaire_2021-10-28"),
    MonitorDemoRoute("GRP-123-E2-L1", 10493121L /* not in spreadsheet */ , "GRP 123 - Liaison Gare de Tournai", "GRP123_Liaison-Gare-de-Tournai_2021-04-15"),
    MonitorDemoRoute("GRP-123-E2-L2", 1L, "GRP 123 - Liaison GR5A - Deux Acren", "GRP123_Liaison-temporaire_Deux-Acren-GR005A_2021-10-29"),
    MonitorDemoRoute("GRP-123-E2-L3", 1L, "GRP 123 - Liaison GR5A - Saint-Léger", "GRP123_Liaison_St-Léger-GR005A_2021-10-29"),
    MonitorDemoRoute("GRP-123-E2-V1", 10514878L /* not in spreadsheet */ , "GRP 123 - Variante de Silly", "GRP123_Variante-de-Silly_2021-04-15"),

    // GRP 125 - Tour de l'Entre-Sambre-et-Meuse

    MonitorDemoRoute("GRP-125-E3", 2927577L, "GRP 125 - Parcours-Principal", "GRP125_Parcours-principal_ 2021-10-11"),
    MonitorDemoRoute("GRP-125-E3-L1", 3472967L /* not in spreadsheet */ , "GRP 125 - Liaison G129 Beaumont", "GRP125-GR129-N_Liaison-Beaumont_2020-04-05"),
    MonitorDemoRoute("GRP-125-E3-L2", 1L, "GRP 125 - Liaison Lac du Valjoly (France)", "GRP125_Liaison-Lac-du-Valjoly_2018-02-19"),
    MonitorDemoRoute("GRP-125-E3-L3", 1L, "GRP 125 - Liaison AJ Namur", "GRP125_liaison-AJ-Rops_2017-10-25"),
    MonitorDemoRoute("GRP-125-E3-L4", 1L, "GRP 125 - Liaison Gare Namur", "GRP125_liaison-Gare-Namur_2018-02-19"),
    MonitorDemoRoute("GRP-125-E3-V1", 1L, "GRP 125 - Variante Senzeille", "GRP125_Var.Senzeille_2017-10-22"),
    MonitorDemoRoute("GRP-125-E3-V3", 1L, "GRP 125 - Variante Meuse à Freyr", "GRP125_Var-Meuse-Freyr_2016-08-11"),
    MonitorDemoRoute("GRP-125-E3-V4", 1L, "GRP 125 - Variante Fraire", "GRP125_Var.Fraire_2017-10-25"),

    // GRP 127 - Tour du Brabant Wallon

    MonitorDemoRoute("GRP-127-E1", 34122L, "GRP 127 - Parcours Principal", "GRP127_Parcours-principal_2021-12-09"),
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
    MonitorDemoRoute("GRP-151-E2-V1", 1L, "GRP 151 - Variante Chenogne - Marbehan", "GRP151_Variante-Chenogne-Marbehan_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-V3", 1L, "GRP 151 - Variante de Presseux", "GRP151_Variante-Presseux_2021-09-07"),
    MonitorDemoRoute("GRP-151-E2-V4", 1L, "GRP 151 - Variante Bodange - Neufchateau", "GRP151_Variante-Bodange-Neufchateau_2021-09-07"),

    // GRP 563 - Tour du pays de Herve

    MonitorDemoRoute("GRP-563-E3", 1696127L, "GRP 563 - Parcours Principal", "GRP563_Parcours-principal_2021-11-21"),
    MonitorDemoRoute("GRP-563-E3-L1", 1L, "GRP 563 - Liaison Verviers", "GRP563_GR573_Liaison-Verviers-2016-10-17"),
    MonitorDemoRoute("GRP-563-E3-L2", 1L, "GRP 563 - Liaison Aix-La-Chapelle", "GRP563_Liaison_Aix-2018-02-06"),
    MonitorDemoRoute("GRP-563-E3-V1", 1L, "GRP 563 - Variante Soiron - Grand Rechain", "GRP563_Var-Soiron-Gd-Rechain-2018-02-06"),

    // GRP 571 - Tour des Vallées des Légendes - Amblève - Salm - Lienne

    MonitorDemoRoute("GRP-571-E7", 3672059L, "GRP 571 - Parcours Principal", "GRP571_Parcours-principal_2019-07-03"),
    MonitorDemoRoute("GRP-571-E7-L1", 1L, "GRP 571 - Liaison GR 15", "GRP571-GR015_Liaison_2019-07-03"),
    MonitorDemoRoute("GRP-571-E7-L2", 1L, "GRP 571 - Liaison GR 15 - Variante Crue", "GRP571-GR015_Liaison-Var-Crue_2019-07-03"),
    MonitorDemoRoute("GRP-571-E7-L3", 1L, "GRP 571 - Liaison GR 57", "GRP571-GR057_Liaison_2019-07-03"),
    MonitorDemoRoute("GRP-571-E7-V1", 1L, "GRP 571 - Variante Crue", "GRP571-Variante-Crue_2019-07-03"),

    // GRP 577 - Tour de la Famenne

    MonitorDemoRoute("GRP-577-E4", 2301540L, "GRP 577 - Parcours Principal", "GRP577_Parcours-principal_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L1", 1L, "GRP 577 - AR Monastère Chevetogne", "GRP577-Liaison_Monastere-de-Chevetogne_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L2", 1L, "GRP-577 - Liaison GR 17", "GRP577-GR017_Liaison_2021-05-05"),
    MonitorDemoRoute("GRP-577-E4-L3", 1L, "GRP-577 - Liaison gare de Marche", "GRP577-Liaison-gare_Marche-en-Famenne_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L4", 1L, "GRP-577 - Liaison gare de Beauraing", "GRP577-Liaison-Gare_Beauraing_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L5", 1L, "GRP-577 - Liaison gare de Barvaux", "GRP577-Liaison-Gare_Barvaux_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-L6", 1L, "GRP-577 - Liaison gare de Jemelle", "GRP577-Liaison-Gare_Jemelle_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V1", 1L, "GRP 577 - Variante Sinsin", "GRP577-Variante_Sinsin_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V2", 1L, "GRP 577 - Variante Revogne", "GRP577_Variante-Revogne_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V3", 1L, "GRP 577 - Variante Chevetogne", "GRP577_Variante-Chevetogne_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V4", 1L, "GRP 577 - Variante Géopark", "GRP577-Variante_Geopark_2021-05-06"),
    MonitorDemoRoute("GRP-577-E4-V5", 1L, "GRP 577 - Variante Les Pairées", "GRP577-Variante_Les-Pairees_2021-05-06"),

    // SAT - Sentier des Abbayes Trappistes de Wallonie

    MonitorDemoRoute("GRT-SAT-E2-P1", 1L, "SAT - Tronçon Chimay - Rochefort", "SAT_Chimay-Rochefort_2021-05-29"),
    MonitorDemoRoute("GRT-SAT-E2-P2", 1L, "SAT - Tronçon Rochefort - Orval", "SAT_Rochefort-Orval_2021-05-29"),
    MonitorDemoRoute("GRT-SAT-E2-V1", 1L, "SAT - Variante de crue Furfooz", "SAT_Chimay-Rochefort_VarianteCrue-Furfooz_2020-06-05"),
    MonitorDemoRoute("GRT-SAT-E2-V2", 1L, """SAT - Variante crue entre "Daverdisse et Lesse"""", "SAT_Rochefort-Orval_VarianteCrue_Daverdisse-Lesse_2020-06-04"),

    // SMA - Sentier des Monts Ardenne

    MonitorDemoRoute("GRT-SMA-E1", 1L, "SMA - Parcours Complet", "SMA_Parcours-complet_2021-10-12"),
  )
}

case class MonitorDemoRoute(id: String, routeId: Long, name: String, filename: String) {
}
