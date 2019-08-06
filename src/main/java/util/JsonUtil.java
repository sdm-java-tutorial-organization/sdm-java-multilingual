package util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Argument;
import model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static ObjectMapper mapper = new ObjectMapper();

    /**
     * getEmptyListOfJsonMap
     *
     * @param size
     * */
    private static List<Map<String, Object>> getEmptyListOfJsonMap(int size) {
        logger.debug("call getEmptyListOfJsonMap");

        List<Map<String, Object>> listOfJsonMap = new ArrayList<>();
        for(int i=0; i<size; i++) {
            listOfJsonMap.add(new TreeMap<>());
        }
        return listOfJsonMap;
    }

    /**
     * getConfig
     *
     * @param strOfJson
     * */
    public static Config getConfig(String strOfJson) throws JsonMappingException, JsonParseException, IOException {
        Config result = mapper.readValue(strOfJson, Config.class);
        return result;
    }

    /**
     * getConfig
     *
     * @param strOfArgs
     * */
    public static Argument getArgument(String strOfArgs) throws JsonMappingException, JsonParseException, IOException {
        Argument argument = mapper.readValue(strOfArgs, Argument.class);
        return argument;
    }

    /**
     * getConfig
     *
     * @param strOfJson
     * */
    public static List<Config> getArrConfig(String strOfJson) throws JsonMappingException, JsonParseException, IOException {
        List<Config> listOfResult = mapper.readValue(strOfJson, new TypeReference<List<Config>>(){});
        return listOfResult;
    }

}
