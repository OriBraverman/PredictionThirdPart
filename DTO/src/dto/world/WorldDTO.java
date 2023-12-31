package dto.world;

import java.util.List;

public class WorldDTO {
    private final String name;
    private final List<PropertyDefinitionDTO> environment;
    private final List<EntityDefinitionDTO> entities;
    private final List<RuleDTO> rules;
    private final int gridWidth;
    private final int gridHeight;

    public WorldDTO(String name, List<PropertyDefinitionDTO> environment, List<EntityDefinitionDTO> entities, List<RuleDTO> rules, int gridWidth, int gridHeight) {
        this.name = name;
        this.environment = environment;
        this.entities = entities;
        this.rules = rules;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    public String getName() {
        return name;
    }

    public List<PropertyDefinitionDTO> getEnvironment() {
        return environment;
    }

    public List<EntityDefinitionDTO> getEntities() {
        return entities;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }
}
