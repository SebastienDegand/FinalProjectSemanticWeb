import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class WikidataQuery {

  public String generateSamePublisherQuery(String idVideoGame) {
    String query = "SELECT ?game \n"
        + "WHERE {\n"
        + "wd:" + idVideoGame + " wdt:P123 ?publisher .\n"
        + "?game wdt:P123 ?publisher .\n"
        + "}";
    return query;
  }

  public String wikidataRequest(String query) throws UnirestException {
    return Unirest.get("https://query.wikidata.org/sparql")
        .queryString("format", "json")
        .queryString("query", query)
        .asString().getBody();
  }

}
