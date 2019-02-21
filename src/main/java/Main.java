import fr.inria.corese.core.load.LoadException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import spark.Spark;

public class Main {
  public static void main(String[] args) throws LoadException {

    Engine engine = new Engine();
    System.out.println("instances loaded !");

    QueryGenerator queryGenerator = new QueryGenerator();
    WikidataQuery wikidataQuery = new WikidataQuery();

    /**
     * GET /query
     * queryParams:
     * - query: sparql query to compute
     * - format: select format of response (json or xml). xml by default
     */
    Spark.get("/query", (req, res) -> {
      String query = req.queryParams("query");
      String result = engine.doQuery(query);
      if(req.queryParams("format") != null && req.queryParams("format").equals("json")) {
        res.type("application/json");
        return result;
      } else {
        return jsonToXml(result);
      }
    });

    /**
     * GET /videogames
     * Get all videogames in the system with their name
     * queryParams:
     * - format: select format of response (json or xml). xml by default
     */
    Spark.get("/videogames", (req, res) -> {
      res.header("Access-Control-Allow-Origin", "*");

      String query = "PREFIX vg: <http://www.videogame-project.fr/2019/videoGameOntology.owl#> \n"
          + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
          + "SELECT ?game ?name \n"
          + "WHERE \n"
          + "{\n"
          + "?game a vg:VideoGame .\n"
          + "?game wdt:hasName ?name .\n"
          + "}";
      String result = engine.doQuery(query);
      if(req.queryParams("format") != null && req.queryParams("format").equals("json")) {
        res.type("application/json");
        return result;
      } else {
        return jsonToXml(result);
      }
    });

    /**
     * GET /videogames/:videogames
     * param:
     * - videogame: id of a video game to get info
     * queryParams:
     * - format: select format of response (json or xml). xml by default
     */
    Spark.get("/videogames/:videogame", (req, res) -> {
          res.header("Access-Control-Allow-Origin", "*");
      String query = "PREFIX vg: <http://www.videogame-project.fr/2019/videoGameOntology.owl#> \n"
          + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
          + "SELECT ?p ?o \n"
          + "WHERE \n"
          + "{\n"
          + "vg:"+ req.params(":videogame") +" ?p ?o .\n"
          + "}";
      String result = engine.doQuery(query);
      if(req.queryParams("format") != null && req.queryParams("format").equals("json")) {
        res.type("application/json");
        return result;
      } else {
        return jsonToXml(result);
      }
    });

    /**
     * GET /recommendation/:videogame
     * param:
     * - videogame: id of a video game to which we will
     *              find similar game
     * queryParams:
     * - level: number corresponding of the level of abstraction of categories.
     *          Categories are in a hierarchy. More this parameter is high, more we
     *          search games with categories which are high in the hierarchy.
     *          (level 0 is same as nothing and search only games with exactly the same
     *          categories)
     * - format: select format of response (json or xml). xml by default
     * - samePublisher: (true or false) If true, select game which have the same publisher
     */
    Spark.get("/recommendation/:videogame", (req, res) -> {
          res.header("Access-Control-Allow-Origin", "*");

      String idVideoGame = req.params(":videogame");
      String levelParam = req.queryParams("level");
      String samePublisher = req.queryParams("samePublisher");
      String query;
      if(isNumber(levelParam)) {
        query = queryGenerator.generateRecommendationQuery(idVideoGame, Integer.parseInt(levelParam));
      } else {
        query = queryGenerator.generateRecommendationQuery(idVideoGame);
      }

      String result = engine.doQuery(query);

      if(Boolean.parseBoolean(samePublisher)) {
        String wikidataQueryPublisher = wikidataQuery.generateSamePublisherQuery(idVideoGame);
        String wikidataResult = wikidataQuery.wikidataRequest(wikidataQueryPublisher);

        result = getIntersection(result, wikidataResult);
      }

      if(req.queryParams("format") != null && req.queryParams("format").equals("json")) {
        res.type("application/json");
        return result;
      } else {
        return jsonToXml(result);
      }
    });

  }

  private static String jsonToXml(String jsonString) {
    JSONObject json = new JSONObject(jsonString);
    String xml = XML.toString(json);
    return xml;
  }

  private static boolean isNumber(String s) {
    try{
      int n = Integer.parseInt(s);
      return true;
    } catch (NullPointerException | NumberFormatException e) {
      return false;
    }
  }

  private static String getIntersection(String localResult, String wikidataResult) {
    JSONObject jsonLocalResult = new JSONObject(localResult);
    JSONObject jsonWikidataResult = new JSONObject(wikidataResult);

    JSONArray localResultArray = jsonLocalResult.getJSONObject("sparql").getJSONObject("results").getJSONArray("result");
    JSONArray wikidataResultArray = jsonWikidataResult.getJSONObject("results").getJSONArray("bindings");
    JSONArray resultIntersection = new JSONArray();

    for(int i = 0; i<localResultArray.length(); i++) {
      String game1 = localResultArray.getJSONObject(i).getJSONArray("binding").getJSONObject(0).getString("uri");
      for(int j = 0; j<wikidataResultArray.length(); j++) {
          String game2 = wikidataResultArray.getJSONObject(j).getJSONObject("game").getString("value");
          if(game1.split("http://www.videogame-project.fr/2019/videoGameOntology.owl#")[1].equals(game2.split("http://www.wikidata.org/entity/")[1])) {
            resultIntersection.put(localResultArray.getJSONObject(i));
            break;
          }
      }
    }
    jsonLocalResult.getJSONObject("sparql").getJSONObject("results").remove("result");
    jsonLocalResult.getJSONObject("sparql").getJSONObject("results").put("result", resultIntersection);
    return jsonLocalResult.toString();
  }


}