package csci307.theGivingChild.CleanWaterGame.scene;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.util.adt.color.Color;

import csci307.theGivingChild.CleanWaterGame.CleanWaterGame;
import csci307.theGivingChild.CleanWaterGame.GameLauncher;
import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;
import csci307.theGivingChild.CleanWaterGame.scene.GameScene.PausedType;

public class AnimationScene extends BaseScene implements IOnMenuItemClickListener {
	
	public Animation currentAnimation;
	
	private final int MENU_NEXT = 0;
	private final int MENU_PREV = 1;
	private final int MENU_SKIP = 2;
	private final int MENU_QUIT = 3;
	
	public enum Animation {
		SCENE_ONE,
		SCENE_TWO,
		SCENE_THREE,
		SCENE_FOUR,
		SCENE_FIVE,
		SCENE_SIX
	}

	public AnimationScene(Animation animation) {
    	this.resourcesManager = ResourceManager.getInstance();
    	this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        currentAnimation = animation;
        createScene();
		
	}
	

	@Override
	public void createScene() {
		
		loadAnimation(currentAnimation);		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_ANIMATION;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	private void loadAnimation(Animation animation) {
		switch (animation) {
			case SCENE_ONE:
				displaySceneOne();
				break;
			case SCENE_TWO:
				break;
			case SCENE_THREE:
				break;
			case SCENE_FOUR:
				break;
			case SCENE_FIVE:
				break;
			case SCENE_SIX:
				break;
			
		}		
	}
	
	private void createMenuChildScene() {
		final MenuScene navigation = new MenuScene(camera);

		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, resourcesManager.font, "QUIT", vbom), Color.RED, Color.WHITE);
		final IMenuItem skipMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_SKIP, resourcesManager.font, "SKIP", vbom), Color.RED, Color.WHITE);
		final IMenuItem nextMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_NEXT, resourcesManager.font, "NEXT", vbom), Color.RED, Color.WHITE);
		final IMenuItem previousMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_PREV, resourcesManager.font, "PREV", vbom), Color.RED, Color.WHITE);

//		gameOver.attachChild(new Text(400, 400, resourcesManager.game_font, "GAME OVER", vbom));
		nextMenuItem.setPosition(750, 20);
		previousMenuItem.setPosition(50, 20);

		navigation.addMenuItem(skipMenuItem);
		navigation.addMenuItem(quitMenuItem);
		navigation.addMenuItem(nextMenuItem);
		navigation.addMenuItem(previousMenuItem);
		
		navigation.setBackgroundEnabled(false);
		navigation.setOnMenuItemClickListener(this);
//		return navigation;
	}
	
	private void displaySceneOne() {
		
	}


	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_SKIP:
				break;
			case MENU_QUIT:
				break;
			case MENU_PREV:
				break;
			case MENU_NEXT:
				break;
			default:
				return false;
		}
		return false;
	}

}
