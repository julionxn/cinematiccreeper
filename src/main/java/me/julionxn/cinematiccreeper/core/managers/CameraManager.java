package me.julionxn.cinematiccreeper.core.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.core.Interpolation;
import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.camera.CameraRecordingPlayer;
import me.julionxn.cinematiccreeper.core.camera.CameraSettings;
import me.julionxn.cinematiccreeper.core.camera.Snap;
import me.julionxn.cinematiccreeper.core.camera.paths.PathType;
import me.julionxn.cinematiccreeper.core.camera.targets.BlockPosTarget;
import me.julionxn.cinematiccreeper.core.camera.targets.CameraTarget;
import me.julionxn.cinematiccreeper.core.camera.targets.EntityTarget;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.util.MathHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CameraManager extends SerializableJsonManager<CameraManager> {

    private static final double TWO_PI = 6.283185;
    private static final float HALF_PI = 1.570796f;
    private Camera camera;
    private State state = State.NONE;
    private double zoom;
    private Vec3d anchorPos = new Vec3d(0, 0, 0);
    private Vec3d actualPos = new Vec3d(0, 0, 0);
    private float targetYaw;
    private float targetPitch;
    private double anchorFov;
    private double deltaFov;
    private double targetDeltaFov;
    private double fov;
    private double targetFov;
    private boolean recording;
    @Nullable
    private CameraTarget cameraTarget;
    private boolean selectingTarget;
    @Nullable
    private CameraRecording currentCameraRecording;
    @Expose
    private final CameraSettings settings = new CameraSettings();
    @Expose
    private List<CameraRecording> cameraRecordings = new ArrayList<>();
    private CameraRecordingPlayer recordingPlayer;
    private boolean playingRecording = false;

    protected CameraManager(String path, float version, Class<CameraManager> clazz) {
        super(path, version, clazz);
    }

    public void update(Camera camera){
        if(camera == null) return;
        this.camera = camera;
        float deltaFrame = MinecraftClient.getInstance().getLastFrameDuration();
        double t = deltaFrame * (1 - settings.getSmoothness());
        //Interpolate pos
        Vec3d pos = camera.getPos();
        Vec3d interpolatedPos = pos.multiply(1 - t).add(actualPos.multiply(t));
        camera.setPos(interpolatedPos.x, interpolatedPos.y, interpolatedPos.z);
        //Interpolate zoom fov
        fov = Interpolation.LINEAR.interpolate(t, fov, targetFov);
        //Interpolate extra fov
        deltaFov = Interpolation.LINEAR.interpolate(t, deltaFov, targetDeltaFov);
        //Interpolate angles
        interpolateAngles(deltaFrame);
    }

    private void interpolateAngles(float deltaFrame){
        float tRotation = deltaFrame * (1 - settings.getRotationSmoothness());
        if (cameraTarget == null){
            float interpolatedYaw = Interpolation.LINEAR.interpolate(tRotation, camera.getYaw(), targetYaw);
            float interpolatedPitch = Interpolation.LINEAR.interpolate(tRotation, camera.getPitch(), targetPitch);
            camera.setRotation(interpolatedYaw, interpolatedPitch);
            return;
        }
        Vec3d targetPos = cameraTarget.getPos();
        Vec3d direction = targetPos.subtract(camera.getPos()).normalize().rotateY(1.570796326f);
        double yaw = clampRadAngle(Math.atan2(direction.z, direction.x));
        double pitch = clampRadAngle(Math.asin(-direction.y));
        float cameraYaw = clampDegAngle(camera.getYaw());
        float cameraPitch = clampDegAngle(camera.getPitch());
        float interpolatedYaw  = Interpolation.interpolateCyclic(Interpolation.LINEAR, tRotation, cameraYaw, (float) Math.toDegrees(yaw));
        float interpolatedPitch = Interpolation.interpolateCyclic(Interpolation.LINEAR, tRotation, cameraPitch, (float) Math.toDegrees(pitch));
        camera.setRotation(interpolatedYaw, interpolatedPitch);
    }

    public void setPlayerAsTarget(MinecraftClient client){
        PlayerEntity player = client.player;
        if (player == null) return;
        cameraTarget = new EntityTarget(player);
        setSelectingTarget(false);
    }

    public void handleTargetSelection(MinecraftClient client){
        PlayerEntity player = client.player;
        if (player == null) return;
        World world = player.getWorld();
        if (world == null) return;
        EntityHitResult entityHitResult = raycastEntitiesInDirection(player);
        if (entityHitResult == null){
            BlockHitResult blockHitResult = raycastInDirection(world, player);
            if (blockHitResult != null){
                setCameraTarget(new BlockPosTarget(blockHitResult.getBlockPos()));
                NotificationManager.getInstance().add(Notification.SAVED);
            }
        } else {
            Entity entity = entityHitResult.getEntity();
            setCameraTarget(new EntityTarget(entity));
            NotificationManager.getInstance().add(Notification.SAVED);
        }
        setSelectingTarget(false);
    }

    public void setCameraTarget(@Nullable CameraTarget target){
        cameraTarget = target;
    }

    public void setSelectingTarget(boolean state){
        selectingTarget = state;
    }

    public boolean isSelectingTarget(){
        return selectingTarget;
    }

    public State getState(){
        return state;
    }

    public void setState(State newState){
        if (selectingTarget) return;
        state = newState;
    }

    public boolean isActive(){
        return state == State.MOVING || state == State.STATIC;
    }

    public CameraSettings getSettings(){
        return settings;
    }

    public void startRecording(String id, PathType type){
        state = State.MOVING;
        currentCameraRecording = new CameraRecording(id, type);
        recording = true;
    }

    public void stopRecording(){
        recording = false;
        currentCameraRecording = null;
    }

    public void addRecording(CameraRecording record){
        cameraRecordings.add(record);
    }

    public void removeCameraRecording(CameraRecording recording){
        cameraRecordings.removeIf(cameraRecording -> cameraRecording.id.equals(recording.id));
    }

    public boolean isRecording(){
        return recording;
    }

    public @Nullable CameraRecording getCurrentCameraRecording(){
        return currentCameraRecording;
    }

    public List<CameraRecording> getCameraRecordings() {
        return cameraRecordings;
    }

    public void tick(){
        if (recordingPlayer != null) recordingPlayer.tick();
    }

    public void playRecording(CameraRecording recording){
        playingRecording = true;
        recordingPlayer = new CameraRecordingPlayer(this, recording);
        CameraRecordingPlayer.startCameraRecording(recordingPlayer);
    }

    public void setPlayingRecording(boolean state){
        playingRecording = state;
    }

    public void setAnchorValues(double x, double y, double z, float yaw, float pitch){
        this.camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        zoom = 0;
        anchorPos = new Vec3d(x, y, z);
        actualPos = new Vec3d(x, y, z);
        camera.setPos(x, y, z);
        this.targetYaw = yaw;
        this.targetPitch = pitch;
        camera.setRotation(yaw, pitch);
        anchorFov = MinecraftClient.getInstance().options.getFov().getValue().doubleValue();
        fov = anchorFov;
        targetFov = anchorFov;
        deltaFov = 0;
        targetDeltaFov = 0;
        applyZoomToPos();
    }

    public void resetToAnchor(){
        zoom = 0;
        targetFov = anchorFov;
        fov = anchorFov;
        targetDeltaFov = 0;
        deltaFov = 0;
        actualPos = anchorPos;
        camera.setPos(anchorPos.x, anchorPos.y, actualPos.z);
    }

    public double getFov(){
        return MathHelper.clamp(fov + deltaFov, 1, 200);
    }

    public void incrementFov(double in){
        targetDeltaFov += in;
    }

    public void setActualFov(double fov){
        deltaFov = fov;
        targetDeltaFov = fov;
    }

    public void updateFov(double fov){
        targetDeltaFov = fov;
    }

    public void changeDirectionByMouse(double dx, double dy){
        if (playingRecording || cameraTarget != null) return;
        if (dx == 0 && dy == 0) return;
        if (camera == null) return;
        float sensibility = settings.getRotationSensibility();
        updateRotation((float) dx * sensibility + targetYaw,
                (float) dy * sensibility + targetPitch);
    }

    public void updateRotation(float yaw, float pitch){
        if (cameraTarget != null) return;
        this.targetYaw = yaw;
        this.targetPitch = pitch;
        applyZoomToPos();
    }

    public void incrementZoom(double in){
        if (zoom + in > 15) {
            zoom = 15;
        } else if (zoom + in < -15){
            zoom = -15;
        } else {
            zoom += in;
        }
        applyZoomToPos();
        targetFov = Interpolation.LINEAR.interpolate((zoom + 15) / 30f, anchorFov + 80, anchorFov - 80);
    }

    public void setZoom(double zoom){
        this.zoom = zoom;
        applyZoomToPos();
        fov = Interpolation.LINEAR.interpolate((zoom + 15) / 30f, anchorFov + 80, anchorFov - 80);
    }

    private Vec3d getDirection(){
        double standardPitch = -clampRadAngle(Math.toRadians(camera.getPitch()));
        double standardYaw = -clampRadAngle(Math.toRadians(camera.getYaw()));
        return new Vec3d(Math.sin(standardYaw) * Math.cos(standardPitch),
                Math.sin(standardPitch), Math.cos(standardYaw) * Math.cos(standardPitch))
                .normalize();
    }

    public EntityHitResult raycastEntitiesInDirection(PlayerEntity player){
        return ProjectileUtil.raycast(player, camera.getPos(), camera.getPos().add(getDirection().multiply(10)),
                new Box(camera.getPos().subtract(0.5, 0.5, 0.5), camera.getPos().add(0.5, 0.5 ,0.5)).stretch(getDirection().multiply(10)),
                entity -> true, 40);
    }

    public BlockHitResult raycastInDirection(World world, PlayerEntity player){
        return world.raycast(new RaycastContext(camera.getPos(),
                camera.getPos().add(getDirection().multiply(10)),
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
    }

    public void moveByKeyboard(float sideways, float forwards, boolean jumping, boolean sneaking){
        if (playingRecording) return;
        Vec3d direction = getDirection();
        double sensibility = settings.getMovementSensibility();
        anchorPos = anchorPos.add(direction.multiply(forwards, 0, forwards).multiply(sensibility));
        anchorPos = anchorPos.add(direction.rotateY(HALF_PI).multiply(sideways, 0, sideways).multiply(sensibility));
        if (jumping) anchorPos = anchorPos.add(0, sensibility, 0);
        if (sneaking) anchorPos = anchorPos.add(0, -sensibility, 0);
        applyZoomToPos();
    }

    public void moveTo(Vec3d vec3d){
        anchorPos = vec3d;
        applyZoomToPos();
    }

    public void setActualPos(Vec3d pos){
        anchorPos = pos;
        actualPos = anchorPos.add(getDirection().multiply(zoom));
        camera.setPos(actualPos.x, actualPos.y, actualPos.z);
    }

    public void setActualAngles(float yaw, float pitch){
        if (cameraTarget != null) {
            Vec3d targetPos = cameraTarget.getPos();
            Vec3d direction = targetPos.subtract(camera.getPos()).normalize().rotateY(1.570796326f);
            double Nyaw = clampRadAngle(Math.atan2(direction.z, direction.x));
            double Npitch = clampRadAngle(Math.asin(-direction.y));
            Nyaw = Math.toDegrees(Nyaw);
            Npitch = Math.toDegrees(Npitch);
            camera.setRotation((float) Nyaw, (float) Npitch);
            return;
        }
        targetYaw = yaw;
        targetPitch = pitch;
        camera.setRotation(yaw, pitch);
    }

    private void applyZoomToPos(){
        actualPos = anchorPos.add(getDirection().multiply(zoom));
    }

    private float clampDegAngle(float angle){
        angle = angle % 360;
        if (angle > 180){
            angle -= 360;
        } else if (angle < -180){
            angle += 360;
        }
        return angle;
    }

    private double clampRadAngle(double angle) {
        angle = angle % (TWO_PI);
        if (angle > Math.PI) {
            angle -= TWO_PI;
        } else if (angle < -Math.PI) {
            angle += TWO_PI;
        }
        return angle;
    }

    public Snap takeSnap(){
        return new Snap(anchorPos, targetYaw, targetPitch, zoom, targetDeltaFov);
    }

    @Override
    protected CameraManager getCurrentInstance() {
        return this;
    }

    @Override
    protected void afterLoad() {

    }
    private static class SingletonHolder {
        private static final CameraManager INSTANCE = new CameraManager("cc_camera.json", 2.2f, CameraManager.class);

    }

    public static CameraManager getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public enum State {
        NONE, STATIC, MOVING

    }

}
