package net.satisfy.skillcraft.json;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.satisfy.skillcraft.skill.Skills;
import net.satisfy.skillcraft.skill.Skillset;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkillLoader implements ResourceReloader {
    public List<RegistrySkills> REGISTRY_SKILLS = Lists.newArrayList();

    public SkillLoader() {
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        List<CompletableFuture<RegistrySkills>> completableFutures = Skills.SKILLSETS.stream().map(
                (skillset) -> this.buildSkillGroup(manager, prepareExecutor, skillset)).toList();
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new));
        Objects.requireNonNull(synchronizer);
        return completableFuture.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(
                (void_) -> this.REGISTRY_SKILLS = completableFutures.stream().map(CompletableFuture::join).toList(), applyExecutor);
    }

    private CompletableFuture<RegistrySkills> buildSkillGroup(ResourceManager resourceManager, Executor prepareExecutor, Identifier path) {
        SkillReader skillGroupLoader = new SkillReader(path);
        return CompletableFuture.supplyAsync(() -> new RegistrySkills(skillGroupLoader.load(resourceManager)), prepareExecutor);
    }

    public record RegistrySkills(Skillset skills) {

    }
}
