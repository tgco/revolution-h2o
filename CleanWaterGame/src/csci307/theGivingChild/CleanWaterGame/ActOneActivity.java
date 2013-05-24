package csci307.theGivingChild.CleanWaterGame;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;

import android.view.KeyEvent;


public class ActOneActivity extends BaseGameActivity {
	
	private BoundCamera camera;
	private ResourceManager resourceManager;
	private static Sound jumpSound;
	private static Music backgroundMusic;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new BoundCamera(0, 0, 800, 480);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws IOException {
		ResourceManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		resourceManager = ResourceManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
		//MUSIC
		MusicFactory.setAssetBasePath("sfx/");
		try {
			this.backgroundMusic = MusicFactory.createMusicFromAsset(getMusicManager(),this, "nyanmusic.mp3");
			this.backgroundMusic.setLooping(true);
			this.backgroundMusic.setVolume(.7f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)	throws IOException {
//		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		SceneManager.getInstance().createMenuScene(pOnCreateSceneCallback);
		
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
//		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
//			
//			@Override
//			public void onTimePassed(TimerHandler pTimerHandler) {
//				mEngine.unregisterUpdateHandler(pTimerHandler);
//				SceneManager.getInstance().createMenuScene();				
//			}
//		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		this.backgroundMusic.play();
	}    
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (this.isGameLoaded())
		{
			System.exit(0);	
		}	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		return false;
	}
}
