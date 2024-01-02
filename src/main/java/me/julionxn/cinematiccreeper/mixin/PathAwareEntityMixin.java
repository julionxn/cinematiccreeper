package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.entity.ai.PerformPathGoal;
import me.julionxn.cinematiccreeper.managers.paths.Path;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(PathAwareEntity.class)
public abstract class PathAwareEntityMixin extends MobEntity implements PathAwareData {

    @Unique
    List<Path> pathList = new ArrayList<>();
    @Unique
    private Path path;
    @Unique
    private PerformPathGoal performPathGoal;

    protected PathAwareEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        performPathGoal = new PerformPathGoal((PathAwareEntity) (Object) this);
        goalSelector.add(0, performPathGoal);
    }

    @Override
    public void cinematiccreeper$setPath(Path path) {
        this.path = path;
        performPathGoal.reset();
    }

    @Override
    public @Nullable Path cinematiccreeper$getPath() {
        return path;
    }

    @Override
    public void cinematiccreeper$addPath(Path path) {
        pathList.add(path);
    }

    @Override
    public void cinematiccreeper$removePath(Path path) {
        pathList.removeIf(path1 -> path1.getId().equals(path.getId()));
    }

    @Override
    public List<Path> cinematiccreeper$getPaths() {
        return pathList;
    }
}
