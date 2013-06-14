/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/22/13
 * Version: 1.0
 * Description: ResourceManager is responsible for loading the resources that an AndEngine Scene needs.
 * 				When a Scene is initiated, it will load the necessary resources required in the Scene.
 * 				When a resource is no longer needed by the Scene, it will be unloaded.
 */

package csci307.theGivingChild.CleanWaterGame.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import csci307.theGivingChild.CleanWaterGame.CleanWaterGame;
import csci307.theGivingChild.CleanWaterGame.GameLauncher;

import android.app.Activity;
import android.graphics.Color;

public class ResourceManager {
	private static final ResourceManager INSTANCE = new ResourceManager();

	public Engine engine;
	public BaseGameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Font font, game_font;
    private BitmapTextureAtlas spriteAtlas;

	//------------------------------------------------
	//TEXTURE & TEXTURE REGIONS
	//------------------------------------------------

	//splash textures
	public ITextureRegion splash_icon_TR;

	//menu textures
	public ITextureRegion act_menu_background_TR;
	public ITextureRegion menu_background_TR;
	public ITextureRegion scene_one_TR, scene_two_TR, scene_three_TR, scene_four_TR, scene_five_TR;
	public ITextureRegion act_one_TR;

	//game textures
	public ITiledTextureRegion player_TR;
	public ITextureRegion collectable_TR,twine_TR,wood_TR,stone_TR, mud_TR;
	public ITextureRegion scene_background_TR;
	public ITextureRegion scene_foreground_TR;
	public ITextureRegion pause_TR, hitpoints_TR;
	public ITextureRegion ground_TR, floating_platform_ground_TR, hill_TR, falling_platform_2_TR, falling_platform_TR;

	//animation textures
	public ITextureRegion animation_one_one, animation_one_two, animation_one_three, animation_one_four, animation_one_five, animation_one_six, animation_one_seven, animation_one_eight;
	public ITextureRegion animation_two_one, animation_two_two, animation_two_three, animation_two_four;

	//texture atlas
	private BuildableBitmapTextureAtlas menuTA;
	private BuildableBitmapTextureAtlas gameTA;
	private BuildableBitmapTextureAtlas groundTA;
	private BuildableBitmapTextureAtlas animationOneTA, animationTwoTA, animationThreeTA, animationFourTA;

	//sounds
	public Sound jumpSound;
	public Sound dashSound;
	public Sound duckSound;

	//music
	//public Music backgroundMusic;

	//------------------------------------------------
	//CALLS TO LOAD ALL RESOURCES
	//------------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuFonts();
	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	public void loadAnimationResources() {
		loadAnimationGraphics();
		loadMenuFonts();
		loadGameAudio();
	}

	//------------------------------------------------
	//MENU RESOURCES
	//------------------------------------------------

	public void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);
		scene_one_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_one_button.png");
		scene_two_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_two_button.png");
		scene_three_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_three_button.png");
		scene_four_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_four_button.png");
		scene_five_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_five_button.png");
		menu_background_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "act_one_background.png");
		act_menu_background_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "act_menu_background.png");
		act_one_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "act_one_button.png");

		try {
			this.menuTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			this.menuTA.load();
		}
		catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadMenuFonts() {
		FontFactory.setAssetBasePath("fonts/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	//------------------------------------------------
	//GAME RESOURCES
	//------------------------------------------------

	/**
	 * This toggles the mute value in the shared preferences object
	 * It toggles the global setting to mute all sounds
	 */
	public void toggleMute()
	{
		boolean efx = CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, Activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_MUTE, false);
		CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, Activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_MUTE, (efx ? false : true)).commit();
	}

	/**
	 * Returns the value currently saved in the shared preferences listener regarding mute
	 * @return true if it is muted, false if not muted
	 */
	public boolean isMuted()
	{
		return CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, Activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_MUTE, false);
	}

	/**
	 * Loads the necessary graphics for the GameScene
	 */
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");		//Set the directory path for the all GameScene graphics relative to the 'assets' folder

        gameTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);	//A map that holds textures/graphics for the game character, collectables, backgrounds, and pause button
        //Player textures
        player_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTA, activity, "player_run_sprite.png", 7, 1);

        //HUD Textures Regions
        hitpoints_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "heart.png");
        pause_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "pause_button.png");

        //Collectable Texture Regions
        collectable_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "water.png");
        twine_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "twine.png");
        wood_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "wood.png");
        stone_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "stone.png");
        mud_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "mud.png");

        //Background Texture Regions
        scene_background_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "gradient_background.png");
        scene_foreground_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "clouds.png");


        groundTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT); //A map that holds textures/graphics for the game's walkables
        //Level building Texture Regions
        ground_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "ground.png");
        floating_platform_ground_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "floating_platform_ground.png");
        hill_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "hill.png");
        falling_platform_2_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "falling_platform_small.png");
        falling_platform_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "falling_platform_large.png");

        try {
        	this.gameTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
        	this.groundTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
        } catch (final TextureAtlasBuilderException e) {
        	Debug.e(e);
        }
        gameTA.load();
        groundTA.load();
    }
	/**
	 * Loads the necessary Graphics for the AnimationScene (the cut scene at the beginning of the GameScene)
	 */
	private void loadAnimationGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/animation/");

		animationOneTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
		animation_one_one = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_1.png");
		animation_one_two = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_2.png");
		animation_one_three = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_3.png");
		animation_one_four = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_4.png");
		animation_one_five = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_5.png");
		animation_one_six = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_6.png");
		animation_one_seven = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_7.png");
		animation_one_eight = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationOneTA, activity, "Scene1_8.png");
		
		animationTwoTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);
		animation_two_one = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTwoTA, activity, "Scene2_1.png");
		animation_two_two = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTwoTA, activity, "Scene2_2.png");
		animation_two_three = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTwoTA, activity, "Scene2_3.png");
		animation_two_four = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTwoTA, activity, "Scene2_4.png");

		try {
			this.animationOneTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			this.animationTwoTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}

		animationOneTA.load();
		animationTwoTA.load();
	}

	/**
	 * Loads the necessary Font for use in the GameScene
	 */
	private void loadGameFonts() {
		FontFactory.setAssetBasePath("fonts/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 40, true, Color.WHITE, 2, Color.BLACK);
		game_font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "NeverwinterNights.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
		game_font.load();
	}

	/**
	 * Loads the necessary Sound and Music resources
	 */
	private void loadGameAudio() {
		//SOUND
		SoundFactory.setAssetBasePath("sfx/");
		try {
			this.jumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity, "jump.mp3");
			this.jumpSound.setVolume(.5f);
			this.dashSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity, "dash.mp3");
			this.dashSound.setVolume(.7f);

			/* UNCOMMENT TO LOAD DUCKING SOUND
			this.duckSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity, "duck.mp3");
			this.duckSound.setVolume(.3f);
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}

		//MUSIC
		//MusicFactory.setAssetBasePath("sfx/");
//		try {
//			if(null != this.backgroundMusic && this.backgroundMusic.isPlaying()) this.backgroundMusic.stop();
//			this.backgroundMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(),activity, "gameMusic.mp3");
//			this.backgroundMusic.setLooping(true);
//			this.backgroundMusic.setVolume(.9f);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	//------------------------------------------------
	//UNLOADING RESOURCES
	//------------------------------------------------

	public void unloadSplashScreen() {
	//	splashTA.unload();
	//	splash_icon_TR = null;
	}

	public void unloadMenuGraphics() {
		menuTA.unload();
	}

	private void unloadMenuFonts() {
		font.unload();
	}

	public void unloadGameGraphics() {
		gameTA.unload();
	}

	public void unloadAnimationGraphics() {
		animationOneTA.unload();
		animationTwoTA.unload();
	}

	//------------------------------------------------
	//MANAGER FUNCTIONS
	//------------------------------------------------

	public static void prepareManager(Engine engine, BaseGameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	public static ResourceManager getInstance() {
		return INSTANCE;
	}

}
