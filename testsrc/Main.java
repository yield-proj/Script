import com.xebisco.yieldscript.interpreter.info.ProjectInfo;
import com.xebisco.yieldscript.interpreter.Script;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        Script script = ScriptUtils.createScript(Main.class.getResourceAsStream("test.ys"), new ProjectInfo("/"));
        System.out.println(Arrays.toString(script.getSource()));
        script.createInstructions();
        System.out.println(script.getInstructions());
        script.execute();
    }
}
