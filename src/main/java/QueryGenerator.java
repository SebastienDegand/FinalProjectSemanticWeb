public class QueryGenerator {

  public String generateRecommendationQuery(String idVideoGame) {
    return this.generateRecommendationQuery(idVideoGame, 0);
  }

  public String generateRecommendationQuery(String idVideoGame, int level) {
    String query = "PREFIX vg: <http://www.videogame-project.fr/2019/videoGameOntology.owl#>\n"
        + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
        + "SELECT DISTINCT ?game ?name\n"
        + "WHERE {\n"
        + "{"
        + "\t?game a vg:VideoGame\n"
        + "\t?game wdt:hasName ?name\n"
        + "\t?game wdt:hasUserScore ?score\n"
        + "\tFILTER NOT EXISTS {\n"
        + "\t\tvg:" + idVideoGame + " wdt:hasGenre ?genreSelectedGame .\n"
        + "\t\t?game wdt:hasGenre ?genreRecom .\n"
        + "\t\tFILTER NOT EXISTS {\n"
        + "\t\t\t?game wdt:hasGenre ?genreSelectedGame .\n"
        + "\t\t\tvg:" + idVideoGame + " wdt:hasGenre ?genreRecom .\n"
        + "\t\t}\n"
        + "\t}\n"
        + "}\n";
      for(int i = 0; i<level; i++) {
        query += "UNION\n"
            + "{"
            + "\t?game a vg:VideoGame\n"
            + "\t?game wdt:hasName ?name\n"
            + "\t?game wdt:hasUserScore ?score\n"
            + "\tFILTER NOT EXISTS {\n"
            + "\t\tvg:" + idVideoGame + " wdt:hasGenre ?genreSelectedGame0 .\n";
        for(int j = 0; j<i+1; j++) {
          query += "\t\t?genreSelectedGame" + j + " skos:broader ?genreSelectedGame" + (j+1) +" .\n";
        }
        query += "\t\t?game wdt:hasGenre ?genreRecom" + (i+1) + " .\n"
            + "\t\tFILTER NOT EXISTS {\n"
            + "\t\t\t?game wdt:hasGenre ?genreSelectedGame" + (i+1) +" .\n"
            + "\t\t\tvg:" + idVideoGame + " wdt:hasGenre ?genreRecom0 .\n";
        for(int j = 0; j<i+1; j++) {
          query += "\t\t\t?genreRecom" + j + " skos:broader ?genreRecom" + (j+1) + " .\n";
        }
        query += "\t\t}\n"
            + "\t}\n"
            + "\n"
            + "}";
      }
    query += "} order by desc(?score)";
      return query;
  }

}
