import fr.inria.corese.core.Graph;
import fr.inria.corese.core.load.Load;
import fr.inria.corese.core.load.LoadException;
import fr.inria.corese.core.print.JSONLDFormat;
import fr.inria.corese.core.print.ResultFormat;
import fr.inria.corese.core.query.QueryProcess;
import fr.inria.corese.core.rule.RuleEngine;
import fr.inria.corese.kgram.core.Mappings;
import fr.inria.corese.sparql.exceptions.EngineException;
import org.json.JSONObject;
import org.json.XML;

public class Engine {

  private Graph graph;

  public Engine() throws LoadException {
    this.graph = Graph.create();
    Load ld = Load.create(graph);

    ld.parse(
        getClass().getResource("wikidataVideoGameOntology.owl").getPath());
    ld.parse(
        getClass().getResource("videoGameRules.rul").getPath());
    ld.parse(
        getClass().getResource("outInstances.rdf").getPath());

    RuleEngine re = ld.getRuleEngine();
    re.process();
    graph.addEngine(re);
    graph.process();
  }

  public String doQuery(String query) throws EngineException {
    QueryProcess exec = QueryProcess.create(graph);
    Mappings map = exec.query(query);
    ResultFormat resultFormat = ResultFormat.create(map);
    return xmlToJson(resultFormat.toString());
  }

  private String xmlToJson(String xml) {
    JSONObject xmlJSONObj = XML.toJSONObject(xml);
    String jsonPrettyPrintString = xmlJSONObj.toString(4);
    return jsonPrettyPrintString;
  }

}
