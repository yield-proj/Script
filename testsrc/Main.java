import com.xebisco.yieldscript.interpreter.Script;
import com.xebisco.yieldscript.interpreter.info.ProjectInfo;
import com.xebisco.yieldscript.interpreter.utils.Pair;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Script script = ScriptUtils.createScript(Main.class.getResourceAsStream("test.ys"), new ProjectInfo("/"));
        script.createInstructions();
        script.execute();
        /*script.getBank().getFunctions().get(new Pair<>("main", List.of(new Class<?>[0]))).execute(script.getBank());
        System.out.println(script.getBank().getFunctions().get(new Pair<>("main", List.of(new Class<?>[0]))).getInstructions());*/
    }
}
