/*
 * Copyright 2018 CodeCrafting.net
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.context;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.codecrafting.springfx.annotation.ViewLink;
import net.codecrafting.springfx.util.Mipmap;
import net.codecrafting.springfx.util.MipmapLevel;


/**
 * The extension class of {@link Stage} that is the top level JavaFX container.
 * The goal to extends the traditional Stage is to provide a view system handler
 * for a easy use of multiple screens. This class also provide a way to load view
 * controllers as Spring beans. Since the Stage is the top level o JavaFX container
 * each view controller will have a reference to his ViewStage allowing that all
 * controllers have access to load another views and query view components. 
 * 
 * To use the view system handling and to load others views, is necessary to initialize
 * the ViewStage with a {@link StageContext}. The StageContext is a root JavaFX
 * controller that has means to create a replaceable view. All views are part of
 * the root view of StageContext.
 * 
 * To create a new ViewStage is necessary to pass a {@link ConfigurableApplicationContext} to
 * allow get the view controllers as Spring beans. View controllers are JavaFX 
 * controllers that extends a {@link ViewContext}.
 *
 * To load new views is necessary to create a {@link Intent}. Intent are just a class
 * that holds the transaction elements to enable to switch views. They are basically inspired
 * by Android Intent system. The Intent has the following elements:
 * 
 * <ul>
 * <li>CallerContext: Instance of {@link ViewContext} that call to load a new view.</li>
 * <li>View class: The {@link ViewContext} class that will be loaded.</li>
 * <li>Extra Data: The transaction data that will be passed to the new view controller.</li>
 * </ul>
 * 
 * @author Lucas Marotta
 * @see #getViewPath()
 * @see #isInitialized()
 * @see #isCacheLoadedNode()
 * @see #setCacheLoadedNode(boolean)
 * @see #setOnMinimizeRequest(EventHandler)
 * @see #setOnMaximizeRequest(EventHandler)
 * @see #setIcon(String)
 * @see #setIconByMipmap(Mipmap)
 * @see #getIconMipmap()
 * @see #getStageContext()
 * @see #getIntent()
 * @see #querySelector(String)
 * @see #querySelectorAll(String)
 * @see #show(boolean)
 * @see #init(Class)
 * @see #loadIntent(Intent)
 * @see #isViewCached(Class)
 * @see #removeViewCache(Class)
 * @see #clearViewCache()
 */
public class ViewStage extends Stage
{
	/**
	 * Default view FXML path
	 */
	public static final String DEFAULT_VIEW_PATH = "/views/";
	
	/**
	 * The view FXML path
	 */
	private String viewPath;
	
	/**
	 * Spring application context to load controllers as beans
	 */
	private ConfigurableApplicationContext springContext;
	private EventHandler<ActionEvent> minimizeEvent;
	private EventHandler<ActionEvent> maximizeEvent;
	
	/**
	 * Simple abstraction of Mipmap images
	 */
	private Mipmap iconMipmap;
	
	/**
	 * Flag that indicates the ViewStage initialization
	 */
	private boolean initialized = false;
	
	/**
	 * Flag that indicates the loaded nodes will be cached
	 */
	private boolean cacheLoadedNode = false;
	private StageContext stageContext;
	
	/**
	 * The view cache of loaded Intents
	 */
	private HashMap<String, ViewContext> viewCache;
	private Intent intent;
	private static final Log LOGGER = LogFactory.getLog(ViewStage.class);
	
	/**
	 * Create a new instance of {@link ViewStage}. By default all loaded views
	 * will use the {@link ViewStage#DEFAULT_VIEW_PATH} for load FXML files.
	 * @param springContext the spring {@link ConfigurableApplicationContext} used to initialize SpringFX
	 * @throws IllegalArgumentException if springContext is null.
	 */
	public ViewStage(ConfigurableApplicationContext springContext)
	{
		this(springContext, DEFAULT_VIEW_PATH);
	}
	
	/**
	 * Create a new instance of {@link ViewStage} using a custom view path 
	 * for load FXML files.
	 * @param springContext the spring {@link ConfigurableApplicationContext} used to initialize SpringFX
	 * @param viewPath the view root path to load FXML files
	 * @throws IllegalArgumentException if springContext or viewPath are null.
	 */
	public ViewStage(ConfigurableApplicationContext springContext, String viewPath)
	{
		super();
		if(viewPath != null && springContext != null) {
			this.springContext = springContext;
			viewCache = new HashMap<String, ViewContext>();
			this.viewPath = viewPath;
			
			iconifiedProperty().addListener((onMinimize) -> {
				ReadOnlyBooleanProperty min = (ReadOnlyBooleanProperty) onMinimize;
				if(min.get() && minimizeEvent != null) {
					minimizeEvent.handle(new ActionEvent());
				}
			});
			maximizedProperty().addListener((onMaximize) -> {
				ReadOnlyBooleanProperty max = (ReadOnlyBooleanProperty) onMaximize;
				if(max.get() && maximizeEvent != null) {
					maximizeEvent.handle(new ActionEvent());
				}
			});
		} else {
			throw new IllegalArgumentException("springContext and viewPath must not be null");
		}
	}
	
	/**
	 * Get the view path to load FXML files.
	 * @return the view root path to load FXML files 
	 */
	public String getViewPath()
	{
		return viewPath;
	}
	
	/**
	 * Check if the ViewStage was initialized @see {@link #init(Class)}
	 * @return {@literal true} if the ViewStage is initialized.
	 */
	public boolean isInitialized()
	{
		return initialized;
	}
	
	/**
	 * Check if the {@link ViewStage} will cache the nodes for {@link #init(Class)} 
	 * or {@link #loadIntent(Intent)}.
	 * @return {@literal true} if the cacheNode is being utilized
	 */
	public boolean isCacheLoadedNode() 
	{
		return cacheLoadedNode;
	}

	/**
	 * Set the cacheLoadedNode to be utilized on the {@link #init(Class)} or {@link #loadIntent(Intent)}.
	 * This cache it's useful to optimize performance for
	 * weak or non present GPU. On practice this will will indicate that on loading new 
	 * views the {@link ViewStage} will use {@link Node#setCache(boolean)} to {@literal true}
	 * and {@link Node#setCacheHint(CacheHint)} to {@link CacheHint#SPEED}.
	 * @param cacheLoadedNode the flag that indicates that loaded nodes will be cached
	 */
	public void setCacheLoadedNode(boolean cacheNode) 
	{
		this.cacheLoadedNode = cacheNode;
	}

	/**
	 * Configure a window minimize event handler
	 * @param minimizeEvent the minimize event handler action
	 */
	public void setOnMinimizeRequest(EventHandler<ActionEvent> minimizeEvent)
	{
		this.minimizeEvent = minimizeEvent;
	}
	
	/**
	 * Configure a window maximize event handler
	 * @param maximizeEvent the maximize event handler action
	 */
	public void setOnMaximizeRequest(EventHandler<ActionEvent> maximizeEvent)
	{
		this.maximizeEvent = maximizeEvent;
	}
	
	/**
	 * Configured a ViewStage icon to show on Taskbar and the decorated {@link Window}
	 * @param iconPath the icon resource path to be loaded
	 * @throws IllegalArgumentException if iconPath is null
	 */
	public void setIcon(String iconPath)
	{
		if(iconPath != null) {
			try {
				Image icon = new Image(getClass().getResourceAsStream(iconPath), 256, 256, true, true);
				getIcons().clear();
				getIcons().add(icon);
			} catch(Exception e) {
				throw new RuntimeException(e);
			}	
		} else {
			throw new IllegalArgumentException("iconPath must not be null");
		}
	}
	
	/**
	 * Configured the ViewStage icons to show on Taskbar and the decorated {@link Window}.
	 * The multiple icons will be used depending on their resolution. All levels
	 * of {@link Mipmap} image levels will be loaded to JavaFX {@link Stage}
	 * 
	 * Previous configured icons will be deleted by calling this method.
	 * @param mipmap the {@link Mipmap} icons to be loaded
	 * @throws IllegalArgumentException if mipmap is null
	 */
	public void setIconByMipmap(Mipmap mipmap)
	{
		if(mipmap != null) {
			List<MipmapLevel> levels = mipmap.getAllLevels();
			getIcons().clear();
			for (MipmapLevel mipmapLevel : levels) {
				getIcons().add(mipmapLevel.getImage());
			}
			iconMipmap = mipmap;
		} else {
			throw new IllegalArgumentException("mipmap must not be null");
		}
	}
	
	/**
	 * Get the last loaded mipmap
	 * @return the {@link Mipmap} loaded mipmap
	 */
	public Mipmap getIconMipmap()
	{
		return iconMipmap;
	}
	
	/**
	 * Get the {@link StageContext} root JavaFX Controller
	 * @return the {@link StageContext} of initialization
	 */
	public StageContext getStageContext()
	{
		return stageContext;
	}
	
	/**
	 * Get the last {@link Intent} used to load views
	 * @return the last loaded {@link Intent}
	 */
	public Intent getIntent()
	{
		return intent;
	}
	
	/**
     * Finds the first sub-node from root {@link Scene} node ({@link Scene#getRoot()}), which match
     * the given CSS selector. If no matches are found returns {@literal null}.
	 * @param selector The css selector of the nodes to find
     * @return The first subnode starting from root {@link Scene} node, which match
     *         the CSS {@code selector}.
     * @throws IllegalStateException if the ViewStage is not initialized.
     * @throws IllegalArgumentException if selector is null
	 */
	public Node querySelector(String selector)
	{
		ObservableList<Node> results  = querySelectorAll(selector);
		return (!results.isEmpty()) ? results.get(0) : null;
	}

    /**
     * Finds all sub-nodes children from root {@link Scene} node ({@link Scene#getRoot()}), 
     * which match the given CSS selector. If no matches are found, an empty unmodifiable set is
     * returned.
     *
     * @param selector The css selector of the nodes to find
     * @return All nodes, starting from root {@link Scene} node, which match
     *         the CSS {@code selector}. The returned set is always unmodifiable, and never null.
     * @throws IllegalStateException if the ViewStage is not initialized.
     * @throws IllegalArgumentException if selector is null
     */	
	public ObservableList<Node> querySelectorAll(String selector)
	{
		if(initialized) {
			if(selector != null) {
				ObservableList<Node> results  = FXCollections.observableArrayList();
				results.addAll(this.getScene().getRoot().lookupAll(selector));
				int i = -1;
				final int size = results.size();
				for (int j = 0; j < size; j++) {
					if(results.get(j).equals(this.getScene().getRoot())) {
						i = j;
						break;
					}
				}
				if(i != -1) results.remove(i);
				return results;
			} else {
				throw new IllegalArgumentException("selector must not be null");
			}
		} else {
			throw new IllegalStateException("ViewStage is not initialized");
		}
	}
	
    /**
     * Attempts to show this Stage Window by setting visibility to true. 
     * Optionally it's possible to pass a parameter to indicate if it is to force resize
     * the window to match {@link Scene} dimensions.
	 * @param sizeToScene indicates if it is to force resize the window to match {@link Scene} dimensions
     * @throws IllegalStateException if this method is called on a thread
     * other than the JavaFX Application Thread.
	 */
	public void show(boolean sizeToScene)
	{
		show();
		if(sizeToScene) sizeToScene();
	}
	
	/**
	 * Initialize this instance to load {@link Intent}s.
	 * @param contextClass the root view controller class to initialize the ViewStage.
	 * @throws IllegalStateException if ViewStage is already initialized
	 * @throws IllegalArgumentException if StageContext is null
	 * @throws RuntimeException if the Spring bean or JavaFX FXML load fails
	 */
	public void init(Class<? extends StageContext> contextClass)
	{
		init(contextClass, null);
	}
	
	/**
	 * Initialize this instance to load {@link Intent}s with a {@link ResourceBundle}.
	 * @param contextClass the root view controller class to initialize the ViewStage.
	 * @param resources the {@link ResourceBundle} to be passed to {@link FXMLLoader}
	 * @throws IllegalStateException if ViewStage is already initialized
	 * @throws IllegalArgumentException if StageContext is null
	 * @throws RuntimeException if the Spring bean or JavaFX FXML load fails
	 */
	public void init(Class<? extends StageContext> contextClass, ResourceBundle resources)
	{
		if(initialized) 
			throw new IllegalStateException("ViewStage already initialized");
		if(contextClass == null) 
			throw new IllegalArgumentException("StageContext class must not be null");
		try {
			stageContext = springContext.getBean(contextClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String viewFilePath = viewPath+stageContext.getViewName()+".fxml";
		URL viewURL = getResourceURL(viewFilePath);
		if(viewURL != null) {
			initialized = true;
			FXMLLoader loader = new FXMLLoader(viewURL);
			if(resources != null) loader.setResources(resources);
			loader.setController(stageContext);
			Parent rootNode = null;
			try {
				injectViewStage(stageContext);
				rootNode = loader.load();
			} catch (Exception e) {
				initialized = false;
				throw new RuntimeException(e);
			}
			ObservableList<String> styles = FXCollections.observableArrayList(rootNode.getStylesheets());
			rootNode.getStylesheets().clear();
			rootNode.getStylesheets().addAll(styles);
			if(cacheLoadedNode) {
				rootNode.setCache(true);
				rootNode.setCacheHint(CacheHint.SPEED);	
			}
			setScene(new Scene(rootNode));
			stageContext.onCreate();
		} else {
			throw new IllegalStateException("Could not initialize with \""+viewFilePath+"\"");
		}
	}
	
	/**
	 * Load a Intent to swap the current view. The {@link Intent} viewClass will be used
	 * to find the {@link ViewContext} controller as a Spring bean and will be cached for
	 * future loads. This method will execute the {@link ViewContext#swapAnimation(Node)}
	 * to swap the {@link StageContext} main node child for the loaded controller node.
	 * The node to be loaded is found by using the FXML file name as the configured view path 
	 * and {@link ViewContext#viewName}.
	 * 
	 * This method also configure any mapped {@link ViewLink} annotations
	 * 
	 * <br><b>NOTE:</b> This method does not pass a {@link ResourceBundle} to views that has
	 * already been loaded. Any new {@link ResourceBundle} will be only present at the {@link Intent}
	 * 
	 * @param intent the container for execute the transaction to switch the current view
	 * @throws IllegalStateException if ViewStage is not initialized
	 * @throws IllegalArgumentException if Intent is null
	 * @throws RuntimeException if the Spring bean or JavaFX FXML load fails
	 */
	public void loadIntent(Intent intent)
	{
		if(!initialized) 
			throw new IllegalStateException("ViewStage is not initialized");
		if(intent == null) 
			throw new IllegalArgumentException("Intent must not be null");
		ViewContext viewController = viewCache.get(intent.getViewClass().getName());
		if(viewController == null) {
			try {
				viewController = springContext.getBean(intent.getViewClass());
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
			String viewFilePath = viewPath+viewController.getViewName()+".fxml";
			URL viewURL = getResourceURL(viewFilePath);
			if(viewURL != null) {
				FXMLLoader loader = new FXMLLoader(viewURL);
				loader.setController(viewController);
				if(intent.getResources() != null) loader.setResources(intent.getResources());
				Parent loadedNode = null;
				try {
					injectViewStage(viewController);
					this.intent = intent;
					loadedNode = loader.load();
				} catch (Exception e) {
					this.intent = null;
					throw new RuntimeException(e);
				}
				loadedNode.getStylesheets().clear();
				if(cacheLoadedNode) {
					loadedNode.setCache(true);
					loadedNode.setCacheHint(CacheHint.SPEED);	
				}
				loadedNode.setDisable(false);
				loadedNode.setVisible(true);
				setViewLinks(viewController);
				viewCache.put(intent.getViewClass().getName(), viewController);
				viewController.onCreate();
			} else {
				throw new RuntimeException("Could not load \""+viewFilePath+"\"");
			}
		} else {
			this.intent = intent;
		}
		stageContext.setViewStageTitle(viewController.getViewTitle());
		stageContext.swapContent(viewController);
		viewController.onStart();
	}
	
	/**
	 * Check if the a {@link ViewContext} is on cache.
	 * @param viewClass the {@link ViewContext} controller class to found on cache.
	 * @return {@literal true} if the {@link ViewContext} controller class was found on cache
	 * @throws IllegalArgumentException if ViewContext class is null
	 */
	public boolean isViewCached(Class<? extends ViewContext> viewClass)
	{
		if(viewClass != null) {
			return viewCache.containsKey(viewClass.getName());
		} else {
			throw new IllegalArgumentException("ViewContext class must not be null");
		}	
	}
	
	/**
	 * Remove a {@link ViewContext} controller class from cache
	 * @param viewClass the {@link ViewContext} controller class to be removed
	 * @throws IllegalArgumentException if ViewContext class is null
	 */
	public void removeViewCache(Class<? extends ViewContext> viewClass)
	{
		if(viewClass != null) {
			if(viewCache.containsKey(viewClass.getName())) {
				viewCache.remove(viewClass.getName());
			}	
		} else {
			throw new IllegalArgumentException("ViewContext class must not be null");
		}
	}
	
	/**
	 * Remove all {@link ViewContext} controller classes from cache
	 */
	public void clearViewCache()
	{
		viewCache.clear();
	}
	
	//Configured any ViewLinks annotation mapped on ViewContext controller class. Touch events are supported.
	private void setViewLinks(ViewContext context)
	{
		Field[] fields = context.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(Region.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(ViewLink.class)) {
				ViewLink viewLinkAnnotation = field.getAnnotation(ViewLink.class);
				Region region = null;
				try {
					boolean access = field.isAccessible();
					field.setAccessible(true);
					region = (Region) field.get(context);
					field.setAccessible(access);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				if(region != null) {
					if(Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
						region.setOnTouchReleased((event) -> {
							loadIntent(new Intent(context, viewLinkAnnotation.value()));
						});
					} else {
						region.setOnMouseClicked((event) -> {
							loadIntent(new Intent(context, viewLinkAnnotation.value()));
						});
					}
				}
			}
		}
	}
	
	private URL getResourceURL(String path)
	{
		if(!path.startsWith("/")) path = "/"+path;
		return getClass().getResource(path);
	}
	
	//Inject this ViewStage into a ViewContext.
	private void injectViewStage(ViewContext context) throws Exception
	{
		Field stageField = ReflectionUtils.findField(context.getClass(), "viewStage");
		boolean access = stageField.isAccessible();
		stageField.setAccessible(true);
		ReflectionUtils.setField(stageField, context, this);
		stageField.setAccessible(access);
	}
}
