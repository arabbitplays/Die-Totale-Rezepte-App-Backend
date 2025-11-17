package oschdi.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import oschdi.model.Item;
import oschdi.util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ItemSaver {
    private ObjectMapper mapper = new ObjectMapper();

    public void safeItems(List<Item> items) throws IOException {
        ArrayNode root = mapper.createArrayNode();
        for (Item item: items) {
            ObjectNode itemNode = mapper.createObjectNode();
            itemNode.put("name", item.getName());
            itemNode.put("unitString", item.getDefaultUnit());
            itemNode.put("amount", Float.toString(item.getDefaultAmount()));
            itemNode.put("fresh", item.isFresh());
            root.add(itemNode);
        }

        File file = new File(FileUtil.getResourcePath() + "/items.json");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(mapper.writeValueAsString(root));
        fileWriter.close();
    }
}
