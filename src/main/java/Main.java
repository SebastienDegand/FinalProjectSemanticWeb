import fr.inria.corese.core.load.LoadException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import spark.Spark;

public class Main {
  public static void main(String[] args) throws LoadException {

    Engine engine = new Engine();
    System.out.println("instances loaded !");

    QueryGenerator queryGenerator = new QueryGenerator();

    Spark.get("/hello", (req, res) -> {
      String query = req.queryParams("query");
      String result = engine.doQuery(query);
      return result;
    });

    /**
     * GET /videogames
     * Get all videogames in the system with their name
     */
    Spark.get("/videogames", (req, res) -> {
      res.type("application/json");
      String query = "PREFIX vg: <http://www.videogame-project.fr/2019/videoGameOntology.owl#> \n"
          + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
          + "SELECT ?game ?name \n"
          + "WHERE \n"
          + "{\n"
          + "?game a vg:VideoGame .\n"
          + "?game wdt:hasName ?name .\n"
          + "}";
      String result = engine.doQuery(query);
      return xmlToJson(result);
    });

    /**
     * GET /videogames/:videogames
     * param:
     * - videogame: id of a video game to get info
     */
    Spark.get("/videogames/:videogame", (req, res) -> {
      res.type("application/json");
      String query = "PREFIX vg: <http://www.videogame-project.fr/2019/videoGameOntology.owl#> \n"
          + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
          + "SELECT ?p ?o \n"
          + "WHERE \n"
          + "{\n"
          + "vg:"+ req.params(":videogame") +" ?p ?o .\n"
          + "}";
      String result = engine.doQuery(query);
      return xmlToJson(result);
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
     */
    Spark.get("/recommendation/:videogame", (req, res) -> {
      res.type("application/json");
      String idVideoGame = req.params(":videogame");
      String levelParam = req.queryParams("level");
      String query;
      if(isNumber(levelParam)) {
        query = queryGenerator.generateRecommendationQuery(idVideoGame, Integer.parseInt(levelParam));
      } else {
        query = queryGenerator.generateRecommendationQuery(idVideoGame);
      }
      System.out.println(query);
      String result = engine.doQuery(query);
      return xmlToJson(result);
    });

  }

  private static String xmlToJson(String xml) {
      JSONObject xmlJSONObj = XML.toJSONObject(xml);
      String jsonPrettyPrintString = xmlJSONObj.toString(4);
      return jsonPrettyPrintString;
  }

  private static boolean isNumber(String s) {
    try{
      int n = Integer.parseInt(s);
      return true;
    } catch (NullPointerException | NumberFormatException e) {
      return false;
    }
  }


}