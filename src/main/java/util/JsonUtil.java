package util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static ObjectMapper mapper = new ObjectMapper();

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
     * @param strOfJson
     * */
    public static List<Config> getArrConfig(String strOfJson) throws JsonMappingException, JsonParseException, IOException {
        List<Config> listOfResult = mapper.readValue(strOfJson, new TypeReference<List<Config>>(){});
        return listOfResult;
    }

}
