package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.JsonUtil;
import util.PropertiesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    String title;
    Map<String, String> properties;

    @Override
    public String toString() {
        if(properties != null) {
            StringBuffer sb = new StringBuffer();
            properties.entrySet().stream().forEach(entry -> {
                sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
            });
            return sb.toString();
        }
        return "";
    }

    public Map<String, Object> toJson() {
        if(properties != null) {
            Map<String, Object> mapOfJson = new TreeMap<>();
            properties.entrySet().stream().forEach(entry -> {
                PropertiesUtil.recurseCreateMaps(mapOfJson, entry.getKey(), entry.getValue());
            });
            return mapOfJson;
        }
        return null;
    }

    public String toJsonString() throws IOException {
        if(properties != null) {
            return JsonUtil.mapper.writeValueAsString(this.toJson());
        }
        return "";
    }
}
