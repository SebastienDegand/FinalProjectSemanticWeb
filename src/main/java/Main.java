import fr.inria.corese.core.load.LoadException;
import spark.Spark;

public class Main {
  public static void main(String[] args) throws LoadException {

    Engine engine = new Engine();

    Spark.get("/hello", (req, res) -> {
      String query = req.queryParams("query");
      String result = engine.doQuery(query);
      return result;
    });

  }


}