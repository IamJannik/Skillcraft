package net.satisfy.skillcraft.json;

import com.google.gson.JsonObject;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.satisfy.skillcraft.Skillcraft;
import net.satisfy.skillcraft.skill.Skillset;
import org.apache.commons.compress.utils.Lists;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkillLoader implements ResourceReloader {
    public static Map<Identifier, Skillset> REGISTRY_SKILLS = new HashMap<>();
    private static final String SKILL_PATH = "skills";

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        List<CompletableFuture<Skillset>> completableFutures = this.buildSkillsets(manager, prepareExecutor);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new));
        Objects.requireNonNull(synchronizer);
        return completableFuture.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(
                void_ -> completableFutures.forEach((completableFutureSkillset) -> completableFutures.stream().map(CompletableFuture::join).forEach(skillset -> REGISTRY_SKILLS.put(skillset.getId(), skillset))), applyExecutor);
    }

    private List<CompletableFuture<Skillset>> buildSkillsets(ResourceManager resourceManager, Executor prepareExecutor) {
        List<CompletableFuture<Skillset>> skills = Lists.newArrayList();

        Map<Identifier, List<Resource>> skillJsons = getJsons(resourceManager);

        for (Identifier identifier : skillJsons.keySet()) {
            if (skillJsons.get(identifier).isEmpty()) continue;
            if (skillJsons.get(identifier).size() == 1) {
                JsonObject jsonObject = SkillReader.read(skillJsons.get(identifier).get(0));
                skills.add(CompletableFuture.supplyAsync(
                        () -> SkillConvertor.convertSkill(jsonObject)));
            } else {
                Skillset skillset = combineMultipleSkillset(skillJsons.get(identifier));
                skills.add(CompletableFuture.supplyAsync(
                        () -> skillset, prepareExecutor));
            }
        }
        return skills;
    }

    private Skillset combineMultipleSkillset(List<Resource> resources) {
        List<JsonObject> skillJsons = Lists.newArrayList();
        for (Resource resource : resources) {
            skillJsons.add(SkillReader.read(resource));
        }
        return SkillReader.combineSkillsets(skillJsons);
    }

    private static Map<Identifier, List<Resource>> getJsons(ResourceManager resourceManager) {
        return resourceManager.findAllResources(SKILL_PATH, (identifier) -> identifier.getNamespace().equals(Skillcraft.MOD_ID));
    }

    public static void init() {
        ReloadListenerRegistry.register(ResourceType.SERVER_DATA, new SkillLoader());
    }
}
