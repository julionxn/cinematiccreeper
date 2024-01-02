package me.julionxn.cinematiccreeper.entity.ai;

import me.julionxn.cinematiccreeper.managers.paths.Path;
import me.julionxn.cinematiccreeper.managers.paths.PathAction;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class PerformPathGoal extends Goal {

    private final PathAwareEntity entity;
    private Path path;
    private List<PathAction> pathActions;
    private int currentAction = 0;
    private int tryingTicks;
    private boolean forwardDirection = true;

    public PerformPathGoal(PathAwareEntity entity) {
        this.entity = entity;
    }

    public void reset() {
        path = ((PathAwareData) entity).cinematiccreeper$getPath();
        if (path == null) return;
        pathActions = path.getActions();
        currentAction = 0;
        tryingTicks = 0;
        forwardDirection = true;
    }


    @Override
    public boolean canStart() {
        path = ((PathAwareData) entity).cinematiccreeper$getPath();
        if (path == null) return false;
        pathActions = path.getActions();
        return reachedCurrentAction();
    }

    @Override
    public void start() {
        currentAction = 0;
        tryingTicks = 0;
        performAction(0);
    }

    private void performAction(double yOffset) {
        PathAction pathAction = pathActions.get(currentAction);
        Vec3d pos = pathAction.getPos();
        this.entity.getNavigation().startMovingTo(pos.x, pos.y + yOffset, pos.z, 1);
    }

    private boolean reachedCurrentAction() {
        return entity.getPos().isInRange(path.getActions().get(currentAction).getPos(), 1.75);
    }

    @Override
    public void stop() {
        reset();
    }

    @Override
    public void tick() {
        if (path == null) return;
        if (!reachedCurrentAction()) {
            tryingTicks++;
            if (shouldResetPath()) {
                performAction(1);
            }
        } else {
            tryingTicks = 0;
            Path.Type type = path.getType();
            if ((forwardDirection && currentAction + 1 == pathActions.size()) ||
                    (!forwardDirection && currentAction - 1 < 0)) {
                if (type == Path.Type.LOOP) {
                    currentAction = -1;
                } else if (type == Path.Type.PING_PONG) {
                    forwardDirection = !forwardDirection;
                }
            }
            currentAction += forwardDirection ? 1 : -1;
            performAction(0);
        }
    }

    public boolean shouldResetPath() {
        return tryingTicks % 40 == 0;
    }

    @Override
    public boolean shouldContinue() {
        if (path == null) return false;
        return tryingTicks < 1200;
    }


}
