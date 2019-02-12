import fr.inria.corese.core.Graph;
import fr.inria.corese.core.load.Load;
import fr.inria.corese.core.load.LoadException;
import fr.inria.corese.core.print.ResultFormat;
import fr.inria.corese.core.query.QueryProcess;
import fr.inria.corese.core.rule.RuleEngine;
import fr.inria.corese.kgram.core.Mappings;
import fr.inria.corese.sparql.exceptions.EngineException;

public class Engine {

  private Graph graph;

  public Engine() throws LoadException {
    this.graph = Graph.create();
    Load ld = Load.create(graph);

    ld.parse(
        "/Users/sebastien/Documents/Polytech/Si5/Majeurs/Web_Semantique/Projet/FinalProjectSemanticWeb/resources/videoGameOntology.owl");
    ld.parse(
        "/Users/sebastien/Documents/Polytech/Si5/Majeurs/Web_Semantique/Projet/FinalProjectSemanticWeb/resources/videoGameRules.rul");
    ld.parse(
        "/Users/sebastien/Documents/Polytech/Si5/Majeurs/Web_Semantique/Projet/FinalProjectSemanticWeb/resources/instances.rdf");

    RuleEngine re = ld.getRuleEngine();
    re.process();
    graph.addEngine(re);
    graph.process();
  }

  public String doQuery(String query) throws EngineException {
    QueryProcess exec = QueryProcess.create(graph);
    Mappings map = exec.query(query);
    ResultFormat resultFormat = ResultFormat.create(map);
    return resultFormat.toString();
  }

}
