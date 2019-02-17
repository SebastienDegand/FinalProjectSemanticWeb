import fr.inria.corese.core.load.LoadException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import spark.Spark;

public class Main {
  public static void main(String[] args) throws LoadException {

    Engine engine = new Engine();
    System.out.println("instances loaded !");

    Spark.get("/hello", (req, res) -> {
      String query = req.queryParams("query");
      String result = engine.doQuery(query);
      return result;
    });

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

    Spark.get("/recommendation/:videogame", (req, res) -> {
      res.type("application/json");
      String idVideoGame = req.params(":videogame");
      String level = req.queryParams("level");
      String query = "PREFIX vg: <http://www.videogame-project.fr/2019/videoGameOntology.owl#> \n"
          + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
          + "SELECT ?recomGame ?recomGameName \n"
          + "WHERE \n"
          + "{\n"
          + "vg:"+ idVideoGame +" wdt:hasGenre ?genre .\n"
          + "?recomGame wdt:hasGenre ?genre .\n"
          + "?recomGame wdt:hasUserScore ?userScore .\n"
          + "?recomGame wdt:hasName ?recomGameName .\n"
          + "} order by desc(?userScore)";
      String result = engine.doQuery(query);
      return xmlToJson(result);
    });

  }

  private static String xmlToJson(String xml) {
      JSONObject xmlJSONObj = XML.toJSONObject(xml);
      String jsonPrettyPrintString = xmlJSONObj.toString(4);
      return jsonPrettyPrintString;
  }


}