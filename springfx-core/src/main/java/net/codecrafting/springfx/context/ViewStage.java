/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2019 The SpringFX Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
 * @see #setNodeCacheHint(CacheHint)
 * @see #getNodeCacheHint()
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
 * @see #init(Class, ResourceBundle)
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
	private CacheHint nodeCacheHint = CacheHint.DEFAULT;
	private StageContext stageContext;
	
	/**
	 * The view cache of loaded Intents
	 */
	private Set<String> viewCache;
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
			viewCache = new HashSet<String>();
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
	 * Set the {@link CacheHint} for all loaded nodes, both by {@link #init(Class)} or {@link #loadIntent(Intent)}.
	 * @return the current {@literal CacheHint}
	 * @default {@link CacheHint#DEFAULT}
	 */
	public CacheHint getNodeCacheHint() 
	{
		return nodeCacheHint;
	}

	/**
	 * Set the nodeCacheHint to be utilized on the {@link #init(Class)} or {@link #loadIntent(Intent)} methods.
	 * This cache it's useful to optimize performance for weak or non present GPU. On practice this will will 
	 * indicate that on loading new views, the {@link ViewStage} will use {@link Node#setCache(boolean)} 
	 * to {@literal true} and {@link #nodeCacheHint} value.
	 * @param cacheNode the flag that indicates that loaded nodes will be cached
	 */
	public void setNodeCacheHint(CacheHint cacheHint) 
	{
		nodeCacheHint = cacheHint;
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
     * the window to match {@link Scene} dimensions. By default this method also perform
     * {@link #toFront()} and {@link #centerOnScreen()}
	 * @param sizeToScene indicates if it is to force resize the window to match {@link Scene} dimensions
     * @throws IllegalStateException if this method is called on a thread
     * other than the JavaFX Application Thread.
	 */
	public void show(boolean sizeToScene)
	{
		show();
		if(sizeToScene) sizeToScene();
		toFront();
		centerOnScreen();
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
	 * By default this method bind the dimensions of the loaded node to the {@link Stage} dimensions
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
			Region loadedNode = null;
			try {
				injectViewStage(stageContext);
				loadedNode = loader.load();
			} catch (Exception e) {
				initialized = false;
				throw new RuntimeException(e);
			}
			loadedNode.setVisible(false);
			
			//Some CSS rules doesn't render properly on startup, so this forces a reload
			ObservableList<String> styles = FXCollections.observableArrayList(loadedNode.getStylesheets());
			loadedNode.getStylesheets().clear();
			loadedNode.getStylesheets().addAll(styles);
			loadedNode.applyCss();
			loadedNode.setCacheHint(nodeCacheHint);
			if(nodeCacheHint.equals(CacheHint.SPEED)) loadedNode.setCache(true);
			setViewLinks(stageContext);
			loadedNode.setVisible(true);
			loadedNode.autosize();
			setScene(new Scene(loadedNode, loadedNode.getWidth(), loadedNode.getHeight()));
			if(stageContext.isFitWidth()) {
				loadedNode.prefWidthProperty().bind(widthProperty());
			}
			if(stageContext.isFitHeight()) {
				loadedNode.prefHeightProperty().bind(heightProperty());
			}
			
			//Required for the proper loading of elements
			Platform.runLater(() -> {
				stageContext.onCreate();
				stageContext.onStart();
			});
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
		
		final ViewContext viewContext;
		try {
			viewContext = springContext.getBean(intent.getViewClass());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		if(viewContext != null) {
			if(! isViewCached(intent.getViewClass())) {
				String viewFilePath = viewPath+viewContext.getViewName()+".fxml";
				URL viewURL = getResourceURL(viewFilePath);
				if(viewURL != null) {
					FXMLLoader loader = new FXMLLoader(viewURL);
					loader.setController(viewContext);
					if(intent.getResources() != null) loader.setResources(intent.getResources());
					Parent loadedNode = null;
					try {
						injectViewStage(viewContext);
						this.intent = intent;
						loadedNode = loader.load();
					} catch (Exception e) {
						this.intent = null;
						throw new RuntimeException(e);
					}
					loadedNode.setVisible(false);
					
					/*
					 * I notice that this is a BIG convenient. If you use Scene Builder to develop your screens you will 
					 * want to add your CSS file to the root node, but that element is not the root element for that ViewStage.
					 * Removing the CSS from the loaded node you can still apply a CSS file to develop your screens with
					 * Scene Builder but have confidence that will be removed by the framework
					 */
					loadedNode.getStylesheets().clear();
					loadedNode.applyCss();
					loadedNode.setCacheHint(nodeCacheHint);
					if(nodeCacheHint.equals(CacheHint.SPEED)) loadedNode.setCache(true);
					setViewLinks(viewContext);
					viewCache.add(intent.getViewClass().getName());
					loadedNode.setVisible(true);
					Platform.runLater(() -> {
						viewContext.onCreate();
					});
				} else {
					throw new RuntimeException("Could not load \""+viewFilePath+"\"");
				}
			} else {
				this.intent = intent;
			}
			stageContext.setViewStageTitle(viewContext.getViewTitle());
			stageContext.swapContent(viewContext);
			Platform.runLater(() -> {
				viewContext.onStart();
			});
		}
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
			return viewCache.contains(viewClass.getName());
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
			if(viewCache.contains(viewClass.getName())) {
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
