
import model.Argument;
import model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ManifestService;
import util.JsonUtil;

import java.util.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * main
     *
     * @param args - [{"type":"resource", "location":"src/main/resources/config", "file":"manifest.json"}]
     * */
    public static void main(String[] args) throws Exception {
        args = new String[] {
                "{\"type\":\"document\", \"location\":\"src/main/resources/config\", \"file\":\"manifest.json\"}",
                /*"{\"type\":\"resource\", \"location\":\"src/main/resources/config\", \"file\":\"manifestB.json\"}"*/
        };

        logger.debug("process start...");
        ManifestService manifestService = ManifestService.getInstance();
        for(String arg : args) {
            Argument argument = JsonUtil.getArgument(arg);
            List<Config> configs = manifestService.getManifest(
                    argument.getLocation(),
                    argument.getFile());
            switch (argument.getType()) {
                case "document":
                    manifestService.loadDocumentManifest(configs);
                    break;
                case "resource":
                    manifestService.loadResourceManifest(configs);
                    break;
                default:
                    throw new Exception("not found args type...");
            }
        }
        logger.debug("process end...");
    }

}
