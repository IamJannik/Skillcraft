package net.bmjo.skillcraft.json;

import com.google.gson.JsonObject;
import dev.architectury.registry.ReloadListenerRegistry;
import net.bmjo.skillcraft.Skillcraft;
import net.bmjo.skillcraft.skill.Skill;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkillLoader implements ResourceReloader {

    private static final String SKILL_PATH = "skills";

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        Skillcraft.LOGGER.debug("Reload skills for skillcraft :)");
        List<CompletableFuture<Skill>> completableFutures = this.buildSkills(manager, prepareExecutor);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new));
        Objects.requireNonNull(synchronizer);
        return completableFuture.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(
                void_ -> completableFutures.forEach((completableFutureSkill) -> completableFutures.stream().map(CompletableFuture::join).forEach(skill -> Skillcraft.SKILLS.put(skill.getId(), skill))), applyExecutor);
    }

    private List<CompletableFuture<Skill>> buildSkills(ResourceManager resourceManager, Executor prepareExecutor) {
        List<CompletableFuture<Skill>> skills = Lists.newArrayList();

        Map<Identifier, List<Resource>> skillJsons = getJsons(resourceManager);

        for (Identifier identifier : skillJsons.keySet()) {
            if (skillJsons.get(identifier).isEmpty()) continue;
            if (skillJsons.get(identifier).size() == 1) {
                JsonObject jsonObject = SkillReader.read(skillJsons.get(identifier).get(0));
                skills.add(CompletableFuture.supplyAsync(
                        () -> SkillConvertor.convertSkill(jsonObject)));
            } else {
                Skill skill = this.combineMultipleSkills(skillJsons.get(identifier));
                skills.add(CompletableFuture.supplyAsync(() -> skill, prepareExecutor));
            }
        }
        return skills;
    }

    private Skill combineMultipleSkills(List<Resource> resources) {
        List<JsonObject> skillJsons = Lists.newArrayList();
        for (Resource resource : resources) {
            skillJsons.add(SkillReader.read(resource));
        }
        return SkillReader.combineSkills(skillJsons);
    }

    private static Map<Identifier, List<Resource>> getJsons(ResourceManager resourceManager) {
        return resourceManager.findAllResources(SKILL_PATH, (identifier) -> identifier.getNamespace().equals(Skillcraft.MOD_ID));
    }

    public static void init() {
        ReloadListenerRegistry.register(ResourceType.SERVER_DATA, new SkillLoader());
    }
}
