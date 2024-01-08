package me.julionxn.cinematiccreeper.core.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.core.Interpolation;
import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.camera.Snap;
import me.julionxn.cinematiccreeper.util.MathHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class CameraManager extends SerializableJsonManager<CameraManager> {

    private static final double TWO_PI = 6.283185;
    private static final float HALF_PI = 1.570796f;
    private Camera camera;
    private State state = State.NONE;
    private double zoom;
    private Vec3d anchorPos = new Vec3d(0, 0, 0);
    private Vec3d actualPos = new Vec3d(0, 0, 0);
    private float yaw;
    private float pitch;
    private double anchorFov;
    private double targetDeltaFov;
    private double deltaFov;
    private double fov;
    private double targetFov;
    private boolean recording;
    @Expose
    private HashMap<String, CameraRecording> cameraRecordings = new HashMap<>();
    @Nullable
    private CameraRecording cameraRecording;
    @Expose
    private double smoothness = 0.95;

    protected CameraManager(String path, float version, Class<CameraManager> clazz) {
        super(path, version, clazz);
    }

    public State getState(){
        return state;
    }

    public void setState(State newState){
        state = newState;
    }

    public boolean isActive(){
        return state == State.MOVING || state == State.STATIC;
    }

    public double getSmoothness(){
        return smoothness;
    }

    public void setSmoothness(double smoothness){
        this.smoothness = smoothness;
    }

    public void startRecording(String id){
        state = State.MOVING;
        cameraRecording = new CameraRecording(id);
        recording = true;
    }

    public void stopRecording(){
        recording = false;
        cameraRecording = null;
    }

    public void addRecording(CameraRecording record){
        cameraRecordings.put(record.id, record);
    }

    public boolean isRecording(){
        return recording;
    }

    public @Nullable CameraRecording getCameraRecording(){
        return cameraRecording;
    }

    public void update(Camera camera){
        if(camera == null) return;
        this.camera = camera;
        float deltaFrame = MinecraftClient.getInstance().getLastFrameDuration();
        double t = deltaFrame * (1 - smoothness);
        //Interpolate pos
        Vec3d pos = camera.getPos();
        Vec3d interpolatedPos = pos.multiply(1 - t).add(actualPos.multiply(t));
        camera.setPos(interpolatedPos.x, interpolatedPos.y, interpolatedPos.z);
        //Interpolate zoom fov
        double tFov = interpolatedPos.distanceTo(pos);
        double interpolatedFov = Interpolation.LINEAR.interpolate(tFov, fov, targetFov);
        //Interpolate extra fov
        deltaFov = Interpolation.LINEAR.interpolate(t, deltaFov, targetDeltaFov);
        fov = interpolatedFov;
    }

    public double getFov(){
        return fov + deltaFov;
    }

    public void setPos(double x, double y, double z){
        if (camera == null) return;
        anchorPos = new Vec3d(x, y, z);
    }

    public void setAnchorPos(double x, double y, double z, float yaw, float pitch){
        this.camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        zoom = 0;
        anchorPos = new Vec3d(x, y, z);
        actualPos = new Vec3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        camera.setPos(x, y, z);
        anchorFov = MinecraftClient.getInstance().options.getFov().getValue().doubleValue();
        fov = anchorFov;
        targetFov = fov;
        deltaFov = 0;
        targetDeltaFov = 0;
        updateCurrentPos();
    }

    public void adjustFov(double in){
        targetDeltaFov += in;
    }

    public void adjustZoom(double in){
        if (zoom + in > 15) {
            zoom = 15;
        } else if (zoom + in < -15){
            zoom = -15;
        } else {
            zoom += in;
        }
        updateCurrentPos();
        targetFov = Interpolation.LINEAR.interpolate((float) ((zoom + 15) / 30f), (float) (anchorFov + 80), (float) (anchorFov - 80));
        targetFov = MathHelper.clamp(targetFov, 1, 110);
    }

    public void changeDirection(double dx, double dy){
        if (dx == 0 && dy == 0) return;
        if (camera == null) return;
        float sensibility = (float) (0.15f / Math.max(1, Math.abs(zoom / 2)));
        yaw = (float) dx * sensibility + camera.getYaw();
        pitch = (float) dy * sensibility + camera.getPitch();
        updateRotation(yaw, pitch);
    }

    private void updateRotation(float yaw, float pitch){
        camera.setRotation(yaw, pitch);
        updateCurrentPos();
    }

    private void updateCurrentPos(){
        actualPos = anchorPos.add(getDirection().multiply(zoom));
    }

    private Vec3d getDirection(){
        double standardPitch = -clampAngle(Math.toRadians(pitch));
        double standardYaw = -clampAngle(Math.toRadians(yaw));
        return new Vec3d(Math.sin(standardYaw) * Math.cos(standardPitch),
                Math.sin(standardPitch), Math.cos(standardYaw) * Math.cos(standardPitch))
                .normalize();
    }

    public void move(float sideways, float forwards, boolean jumping, boolean sneaking){
        Vec3d direction = getDirection();
        anchorPos = anchorPos.add(direction.multiply(forwards, 0, forwards));
        anchorPos = anchorPos.add(direction.rotateY(HALF_PI).multiply(sideways, 0, sideways));
        if (jumping) anchorPos = anchorPos.add(0, 1, 0);
        if (sneaking) anchorPos = anchorPos.add(0, -1, 0);
        updateCurrentPos();
    }

    public void move(Vec3d vec3d){
        anchorPos = anchorPos.add(vec3d);
    }

    private double clampAngle(double angle) {
        angle = angle % (TWO_PI);
        if (angle > Math.PI) {
            angle -= TWO_PI;
        } else if (angle < -Math.PI) {
            angle += TWO_PI;
        }
        return angle;
    }

    @Override
    protected CameraManager getCurrentInstance() {
        return this;
    }

    @Override
    protected void afterLoad() {

    }

    private static class SingletonHolder {
        private static final CameraManager INSTANCE = new CameraManager("cc_camera.json", 1.0f, CameraManager.class);
    }

    public static CameraManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public enum State {
        NONE, STATIC, MOVING
    }

    public Snap takeSnap(){
        return new Snap(anchorPos, yaw, pitch, zoom, fov);
    }

}
