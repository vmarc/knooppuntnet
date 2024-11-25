package kpn.core.tools.monitor.support

import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdateContext
import kpn.server.monitor.route.update.MonitorUpdateReporterLogger
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration

case class NewRoute(
  routeId: Long,
  name: String,
  description: String
)

object MonitorCreateFrNwnRoutesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val tool = new MonitorCreateFrNwnRoutesTool(configuration(database))
      tool.run()
    }
  }

  private def configuration(database: Database): MonitorUpdaterConfiguration = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
    val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)
    new MonitorUpdaterConfiguration(
      database,
      monitorRouteRelationRepository,
      monitorRouteStructureLoader
    )
  }
}

class MonitorCreateFrNwnRoutesTool(configuration: MonitorUpdaterConfiguration) {

  def run(): Unit = {

    val group = configuration.monitorGroupRepository.groupByName("fr-nwn").get
    val existingRoutes = configuration.monitorGroupRepository.groupRoutes(group._id).sortBy(_.name)

    val routeCandidates = newRouteDefinitions().sortBy(_.name)
    println(s"${routeCandidates.size} route candidates")
    val newRoutes = routeCandidates.filter { route =>
      !existingRoutes.exists(existingRoute => existingRoute.name == route.name && existingRoute.relationId.contains(route.routeId))
    }
    println(s"${newRoutes.size} new routes")
    newRoutes.foreach { newRoute =>
      println(s"${newRoute.routeId} ${newRoute.name} ${newRoute.description}")
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "create-fr-nwn-routes",
          new MonitorUpdateReporterLogger(),
          MonitorRouteUpdate(
            action = "add",
            groupName = group.name,
            routeName = newRoute.name,
            referenceType = "osm",
            referenceNow = Some(true),
            description = Some(newRoute.description),
            comment = None,
            relationId = Some(newRoute.routeId)
          )
        )
      )
    }
  }

  private def newRouteDefinitions(): Seq[NewRoute] = {
    Seq(
      NewRoute(3390574, "Saint-Guilhem", "Chemin de Saint-Guilhem-le-Désert"),
      NewRoute(1750445, "GTJ", "GR 509 GTJ à pied"),
      NewRoute(2603124, "GR 53", "De Wissembourg au col de l'Engin"),
      NewRoute(2785102, "GR 532", "GR 532"),
      NewRoute(2952375, "GR 534", "Sentier Stanislas-Kléber"),
      NewRoute(3159559, "GR 501", "Chemin de la Moselle"),
      NewRoute(8192507, "GR 533", "GR 533 Belfort-Sarrebourg"),
      NewRoute(9079172, "GR 531", "GR 531"),
      NewRoute(11888292, "GR 5G", "GR 5G"),
      NewRoute(7235528, "GR 861", "Via Garona"),
      NewRoute(15676218, "GR 81", "Chemin d'Amadour"),
      NewRoute(386891, "GR 1", "Tour de Paris"),
      NewRoute(7411272, "GR 10", "La traversée des Pyrénées"),
      NewRoute(4008099, "GR 101", "Chemin de l'Ouest de Bigorre"),
      NewRoute(33270, "GR 105", "GR 105"),
      NewRoute(1112797, "GR 108", "Voie d'Ossau"),
      NewRoute(8320857, "GR 108A", "Voie d'Ossau Est"),
      NewRoute(2913854, "GR 10A", "GR 10A"),
      NewRoute(7407977, "GR 10F", "GR 10F"),
      NewRoute(355981, "GR 11", "GRand Tour de Paris"),
      NewRoute(2568762, "GR 111", "Sentier de l'Essonne"),
      NewRoute(12609586, "GR 113", "De Provins à Auxerre"),
      NewRoute(13910420, "GR 11A", ""),
      NewRoute(13913378, "GR 11B", "GR 11B"),
      NewRoute(2139379, "GR 120", "GR 120 - Sentier du Littoral"),
      NewRoute(5547168, "GR 121B", "GR 121B De Bailleul à Rieulay"),
      NewRoute(6260934, "GR 121C", "GR 121C De Aubigny-au-Bac à Le Quesnoy"),
      NewRoute(5370873, "GR 123", "GR 123 De Contes à Carlepont"),
      NewRoute(18143832, "GR 124 parent", "GR 124"),
      NewRoute(13995186, "GR 124A", "GR 124A"),
      NewRoute(2143275, "GR 125", "De Serans à Saint-Valéry-sur-Somme"),
      NewRoute(2139550, "GR 127", "GR 127"),
      NewRoute(2141317, "GR 127A", "GR 127A"),
      NewRoute(2142646, "GR 127B", "GR 127B"),
      NewRoute(2132778, "GR 128", "GR 128 De Bailleul à Wissant"),
      NewRoute(2139234, "GR 128A", "De Watten à Bourbourg"),
      NewRoute(14000721, "GR 12A", "GR 12A"),
      NewRoute(14460615, "GR 12B", "GR 12B"),
      NewRoute(12798892, "GR 13", "Du Gâtinais au Morvan"),
      NewRoute(14468898, "GR 131", "D'Issy-L'Eveque à Mont Beuvray"),
      NewRoute(3810330, "GR 132", "De Griselles à Chaumot"),
      NewRoute(14075709, "GR 137", "De Nolay à Autun"),
      NewRoute(13912694, "GR 142", "De Laon à Verzenay"),
      NewRoute(15100979, "GR 14B", "De Trois-Fontaines à Lochères"),
      NewRoute(15989093, "GR 15", "La Vallée de la Marne"),
      NewRoute(15622517, "GR 169", "Tour de Lyon par les Forts"),
      NewRoute(1659185, "GR 1C", "Liaison GR 1 - GR 11"),
      NewRoute(6064770, "GR 1D", "Liaison GR 1 - GR 11"),
      NewRoute(1959668, "GR 2", "Au fil de la Seine"),
      NewRoute(101692, "GR 20", "GR 20"),
      NewRoute(14868379, "GR 21", "Du Tréport au Havre"),
      NewRoute(9258722, "GR 210", "Sur les traces du Chasse-marée"),
      NewRoute(12854245, "GR 213", "Du Loire à Vézelay"),
      NewRoute(9628450, "GR 21B", "GR 21B"),
      NewRoute(451519, "GR 22", "De Paris au Mont-Saint-Michel"),
      NewRoute(13524440, "GR 221", "Le Bocage Normand"),
      NewRoute(13529966, "GR 221A", "Saint-Martin-de-Sallen à Saint-Jean-le-Blanc"),
      NewRoute(13530369, "GR 221C", "Caen à Roucamps"),
      NewRoute(14717233, "GR 222", "D'Avre au Seine"),
      NewRoute(1731774, "GR 223", "Littoral de la Normandie"),
      NewRoute(14023215, "GR 224", "De Verneuil-sur-Avre à Berville-sur-mer"),
      NewRoute(14457779, "GR 225", "Entre Epte et Oise"),
      NewRoute(3224273, "GR 226", "De Condé-sur-Noireau à Gué-Rochoux"),
      NewRoute(2785114, "GR 22B", "22B"),
      NewRoute(3437350, "GR 22C", "De Ravigny à Bagnoles-de-l'Orne par le Mont des Avaloirs"),
      NewRoute(14752331, "GR 23", "De La Bouille à Honfleur"),
      NewRoute(14707539, "GR 235", "Du Sarthe au Loir"),
      NewRoute(14465600, "GR 25", "De Sotteville-sous-le-Val à Montville"),
      NewRoute(1731864, "GR 26", "De Villennes-sur-Seine à Villers-sur-Mer"),
      NewRoute(2704569, "GR 3", "La Loire sauvage à pied"),
      NewRoute(16195169, "GR 300", "Chemin de Saint-Michel"),
      NewRoute(15665421, "GR 303", "À travers le Bourbonnais"),
      NewRoute(13046192, "GR 31", "Traversée de la Sologne"),
      NewRoute(13481848, "GR 32", "De La Seine à La Loire"),
      NewRoute(7790332, "GR 34", "Chemin des Douaniers"),
      NewRoute(12923652, "GR 341", "De Lanester au Lac de Guerlédan"),
      NewRoute(12909801, "GR 347", "De Josselin à Redon"),
      NewRoute(14987109, "GR 34A", "Au travers des Côtes-d'Armor"),
      NewRoute(12999518, "GR 34C", "De Néal au barrage de la Rance"),
      NewRoute(5436315, "GR 34I", "De Moëlan-sur-Mer à Quimperlé"),
      NewRoute(7477707, "GR 35", "Du Perche à la Loire"),
      NewRoute(6020693, "GR 351", "Vallée de la Blaise"),
      NewRoute(13046468, "GR 353", "De Vendôme au Loire"),
      NewRoute(10805122, "GR 36", "De la Manche aux Pyrénées"),
      NewRoute(5414606, "GR 360", "Tour de Saintonge"),
      NewRoute(14924818, "GR 364", "Du Poitou à L'Océan"),
      NewRoute(7478039, "GR 365", "De Sillé-le-Guillaume à Durtal"),
      NewRoute(3394595, "GR 367", "Sentier Cathare"),
      NewRoute(4334366, "GR 36B", "36B"),
      NewRoute(8373372, "GR 37", "Cœur de Bretagne"),
      NewRoute(12035546, "GR 38", "De Douarnenez à Redon"),
      NewRoute(1824726, "GR 39", "Du Mont-Saint-Michel à La Grande Brière"),
      NewRoute(14306878, "GR 3A", "GR3A"),
      NewRoute(2949457, "GR 3B", "Forêt d'Orléans"),
      NewRoute(13054417, "GR 3C", "De Gien à Chambord"),
      NewRoute(7457566, "GR 4", "De la Méditerranée à l'Océan"),
      NewRoute(6062350, "GR 406", "Route Napoléon à pied"),
      NewRoute(15706394, "GR 41", "La Vallée du Cher"),
      NewRoute(13472159, "GR 413", "Du Cher à La Loire"),
      NewRoute(2410123, "GR 42", "De Saint-Étienne au Grau-du-Roi"),
      NewRoute(11134148, "GR 422", "Sur les Pas de Charles IX et Catherine de Médicis"),
      NewRoute(10670466, "GR 429", "Du col de Dieu-Grâce à Viviers"),
      NewRoute(2410124, "GR 42A", "42A"),
      NewRoute(6438029, "GR 43", "Sentier du Col des Faïsses à Sainte-Eulalie"),
      NewRoute(13873396, "GR 44", "Des Vans à Champerboux"),
      NewRoute(14496773, "GR 440", "Tour de la Montagne Limousine"),
      NewRoute(14496724, "GR 440B", "De Treignac à Saint-Merd"),
      NewRoute(6439515, "GR 46", "De Tours à Toulouse"),
      NewRoute(15768468, "GR 463", "GRande Traversée du Sud de l'Allier"),
      NewRoute(8600862, "GR 465", "Des monts du Cantal à la vallée du Lot"),
      NewRoute(14310325, "GR 46A", "GR46A"),
      NewRoute(3377491, "GR 470", "Sentier du Haut-Allier"),
      NewRoute(10169937, "GR 48", "De La Ribière à Chinon"),
      NewRoute(1623140, "GR 49", "De Saint-Raphaël à Rougon"),
      NewRoute(11097196, "GR 50", "Tour du Parc National des Écrins"),
      NewRoute(8040816, "GR 507", "De Charmes à Contrexéville"),
      NewRoute(11087038, "GR 51", "Balcons de la Méditerranée"),
      NewRoute(2366584, "GR 510", "Sentier des Huit Vallées"),
      NewRoute(3315112, "GR 52", "Traversée du Mercantour"),
      NewRoute(1782102, "GR 52A", "Le Sentier panoramique du Mercantour"),
      NewRoute(2909096, "GR 54", "Tour d'Oisans et Écrins"),
      NewRoute(377905, "GR 55", "GR 55 La Vanoise"),
      NewRoute(6265097, "GR 559", "GR 559"),
      NewRoute(1738444, "GR 56", "Tour de l'Ubaye"),
      NewRoute(6289458, "GR 56B", "Tour de l'Ubaye (variante)"),
      NewRoute(9711201, "GR 58", "Tour du Queyras"),
      NewRoute(12827833, "GR 59", "Du Ballon d'Alsace à Yenne"),
      NewRoute(3789450, "GR 59a", "GR 59 par Arc-et-Senans"),
      NewRoute(5011474, "GR 5b", "GR 5b"),
      NewRoute(5119656, "GR 5c", "GR 5c"),
      NewRoute(9957802, "GR 5V", "GR 5 Variante La Plantaz"),
      NewRoute(13126456, "GR 5V", "GR 5 Variante Modane Gare"),
      NewRoute(9870817, "GR 6", "Des Alpes à la Nouvelle Aquitaine"),
      NewRoute(13880221, "GR 60", "Chemin du signal de Mailhebiau à Saint-Mathieu-de-Tréviers - Tracé principal"),
      NewRoute(2787404, "GR 62", "De Roque-Rouge à Conques"),
      NewRoute(2868197, "GR 620", "Horizons et Clochers du Rouergue"),
      NewRoute(3538122, "GR 62A", "GR 62A"),
      NewRoute(15662377, "GR 62B", "GR 62B"),
      NewRoute(15647499, "GR 62C", "GR 62C"),
      NewRoute(11206372, "GR 63", "Sentier de Grande Randonnée 63"),
      NewRoute(15459436, "GR 636", "De Montaut à Lacapelle-Biron"),
      NewRoute(13078092, "GR 64", "De Rocamadour aux Eyzies-de-Tayac-Sireuil"),
      NewRoute(11959152, "GR 65", "Chemin de Saint-Jacques via Le Puy"),
      NewRoute(3537850, "GR 651", "Vallée du Célé"),
      NewRoute(15471251, "GR 652", "Voie de Rocamadour"),
      NewRoute(11813130, "GR 653", "La voie d'Arles"),
      NewRoute(12748333, "GR 653A", "Via Aurelia"),
      NewRoute(6132056, "GR 653D", "Via Domitia"),
      NewRoute(14900298, "GR 654", "Voie de Vézelay"),
      NewRoute(5869504, "GR 68", "Tour du Mont Lozère"),
      NewRoute(14234324, "GR 69", "La Routo"),
      NewRoute(2785106, "GR 6A", "6A"),
      NewRoute(3159060, "GR 6A", "Chemin de Brameloup à Aubrac"),
      NewRoute(6476958, "GR 6D", "GR 6D"),
      NewRoute(2748977, "GR 7 parent", "Des Vosges aux Pyrénées"),
      NewRoute(223039, "GR 70", "Chemin de Stevenson"),
      NewRoute(8289243, "GR 700", "Chemin de Régordane"),
      NewRoute(10861384, "GR 703", "Sentier Historique de Jeanne d'Arc"),
      NewRoute(6423545, "GR 71", "De l'Espérou (Gard) au col de Fontfroide (Hérault)"),
      NewRoute(8308066, "GR 714", "De Bar-le-Duc au Haut-de-Suède"),
      NewRoute(10997965, "GR 71C", "Larzac Templier et Hospitalier"),
      NewRoute(10994450, "GR 71D", "Tour du Larzac"),
      NewRoute(2958766, "GR 72", "Du Col du Bez à Barre-des-Cévennes"),
      NewRoute(17637988, "GR 74", "GR 74"),
      NewRoute(13363916, "GR 75", "Les Portes de Paris"),
      NewRoute(14316345, "GR 76", "De Chagny à Affoux"),
      NewRoute(14313152, "GR 76A", "GR 76A"),
      NewRoute(14313151, "GR 76B", "GR 76B"),
      NewRoute(14313047, "GR 76C", "GR 76C"),
      NewRoute(14310467, "GR 76D", "GR 76D"),
      NewRoute(3728404, "GR 77", "Du Saut de Vézoles au Signal de l'Alaric"),
      NewRoute(2785399, "GR 78", "Chemin du Piémont Pyrénéen"),
      NewRoute(2892444, "GR 782", "Chemin Henri IV"),
      NewRoute(12464298, "GR 787", "Chemin de Fontcaude"),
      NewRoute(4043074, "GR 8", "L'Atlantique entre Loire et Bidassoa"),
      NewRoute(6893130, "GR 80", "Le tour de l'île d'Yeu"),
      NewRoute(14292840, "GR 800", "Au fil de la Somme"),
      NewRoute(15034692, "GR 86", "De Toulouse à Bagnères-de-Luchon"),
      NewRoute(15000703, "GR 89", "Chemin de Montaigne"),
      NewRoute(10670467, "GR 9", "Du Jura à la Méditerranée"),
      NewRoute(6338835, "GR 90", "GR 90 - Du Lavandou à Notre-Dame des Anges"),
      NewRoute(9920921, "GR 91", "Traversée du Vercors Nord-Sud"),
      NewRoute(2785107, "GR 93", "Traversée du Vercors"),
      NewRoute(13916397, "GR 94", "De Vaunières au Col des Praux"),
      NewRoute(12754714, "GR 95", "De Saillans à Lus-la-Croix-Haute"),
      NewRoute(6197308, "GR 96", "GR 96"),
      NewRoute(13668305, "GR 965", "Sur les pas des Huguenots en France"),
      NewRoute(2681053, "GR 98", "Sentier de Grande Randonnée 98"),
      NewRoute(9812928, "GR 99", "De Revest-les-Eaux à Trigance"),
      NewRoute(6554584, "GR 99A", "De La Durance Aux Pallières"),
      NewRoute(10385031, "GR 9A", "Sentier de grande randonnée 9A"),
      NewRoute(10692471, "GR G1", "La Trace des Alizés"),
      NewRoute(14910211, "GR M1", ""),
      NewRoute(1796889, "GR NC1", ""),
      NewRoute(7238091, "R1", "GR R1"),
      NewRoute(7238256, "R2", "GR R2"),
      NewRoute(7238289, "R3", "GR R3"),
      NewRoute(7465333, "GR5", "GR5-Var"),
      NewRoute(5183682, "GR BL", "Balcon du Léman"),
      NewRoute(17931787, "GR34-38", "Liaison GR34 - GR 38"),
      NewRoute(2018553, "HRP", "Haute Randonnée Pyrénéenne"),
    )
  }
}
