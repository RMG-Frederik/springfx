package net.codecrafting.springfx.controls;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

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
import net.codecrafting.springfx.annotation.ViewLink;
import net.codecrafting.springfx.utils.Mipmap;
import net.codecrafting.springfx.utils.MipmapLevel;

public class ViewStage extends Stage
{
	public static final String DEFAULT_VIEW_PATH = "/views/";
	private String viewPath;
	private boolean initialized = false;
	private EventHandler<ActionEvent> minimizeEvent;
	private EventHandler<ActionEvent> maximizeEvent;
	private StageContext stageContext;
	private HashMap<String, ViewContext> viewCache;
	private Mipmap iconMipmap;
	private Parent rootNode;
	private ApplicationContext springContext;
	private static final Log LOGGER = LogFactory.getLog(ViewStage.class);
	
	public ViewStage(ApplicationContext springContext)
	{
		this(springContext, DEFAULT_VIEW_PATH);
	}
	
	public ViewStage(ApplicationContext springContext, String viewPath)
	{
		super();
		this.springContext = springContext;
		viewCache = new HashMap<String, ViewContext>();
		this.viewPath = viewPath;
		
		iconifiedProperty().addListener((onMinimize) -> {
			if(isIconified() && minimizeEvent != null) {
				minimizeEvent.handle(new ActionEvent());
			}
		});
		
		maximizedProperty().addListener((onMaximize) -> {
			if(isMaximized() && maximizeEvent != null) {
				maximizeEvent.handle(new ActionEvent());
			}
		});
	}
	
	public String getViewPath()
	{
		return viewPath;
	}
	
	public HashMap<String, ViewContext> getViewCache()
	{
		return new HashMap<String, ViewContext>(viewCache);
	}
	
	public boolean isInitialized()
	{
		return initialized;
	}
	
	public void setOnMinimizeRequest(EventHandler<ActionEvent> minimizeEvent)
	{
		this.minimizeEvent = minimizeEvent;
	}
	
	public void setOnMaximizeRequest(EventHandler<ActionEvent> maximizeEvent)
	{
		this.maximizeEvent = maximizeEvent;
	}
	
	public void setIcon(String iconPath)
	{
		try {
			Image icon = new Image(getClass().getResourceAsStream(iconPath), 256, 256, true, true);
			getIcons().add(icon);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	public void setIconByMipmap(Mipmap mipmap)
	{
		iconMipmap = mipmap;
		List<MipmapLevel> levels = mipmap.getAllLevels();
		for (MipmapLevel mipmapLevel : levels) {
			getIcons().add(mipmapLevel.getImage());
		}
	}
	
	public Mipmap getIconMipmap()
	{
		return iconMipmap;
	}
	
	public Parent getRootNode()
	{
		return rootNode;
	}
	
	public StageContext getStageContext()
	{
		return stageContext;
	}
	
	public Node querySelector(String selector)
	{
		return querySelector(this.getScene().getRoot(), selector);
	}
	
	public Node querySelector(Node rootNode, String selector)
	{
		return rootNode.lookup(selector);
	}
	
	public ObservableList<Node> querySelectorAll(String selector)
	{
		return querySelectorAll(rootNode, selector);
	}
	
	public ObservableList<Node> querySelectorAll(Node rootNode, String selector)
	{
		ObservableList<Node> results  = FXCollections.observableArrayList();
		results.addAll(rootNode.lookupAll(selector));
		return results;
	}
	
	public void show(boolean sizeToScene)
	{
		show();
		if(sizeToScene) {
			sizeToScene();
			setMinHeight(getHeight());
			setMinWidth(getWidth());
		}
	}
	
	public void init(Class<? extends StageContext> contextClass)
	{
		if(!initialized) {
			try {
				stageContext = springContext.getBean(contextClass);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if(stageContext != null) {
				String viewFilePath = viewPath+stageContext.getViewName()+".fxml";
				URL viewURL = getResourceURL(viewFilePath);
				if(viewURL != null) {
					FXMLLoader loader = new FXMLLoader(viewURL);
					loader.setController(stageContext);
					stageContext.setViewStage(this);
					try {
						rootNode = loader.load();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					if(rootNode != null) {
						ObservableList<String> styles = FXCollections.observableArrayList(rootNode.getStylesheets());
						rootNode.getStylesheets().clear();
						rootNode.getStylesheets().addAll(styles);
						rootNode.setCache(true);
						rootNode.setCacheHint(CacheHint.SPEED);
						setScene(new Scene(rootNode));
						initialized = true;
					}
				} else {
					throw new RuntimeException("Could not initialize with \""+viewFilePath+"\"");
				}					
			}
		} else {
			throw new RuntimeException("ViewStage already initialized");
		}
	}
	
	public void load(Intent intent)
	{
		if(initialized) {
			ViewContext viewController = viewCache.get(intent.getViewClass().getName());
			if(viewController == null) {
				try {
					viewController = springContext.getBean(intent.getViewClass());
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
				if(viewController != null) {
					String viewFilePath = viewPath+viewController.getViewName()+".fxml";
					URL viewURL = getResourceURL(viewFilePath);
					if(viewURL != null) {
						FXMLLoader loader = new FXMLLoader(viewURL);
						loader.setController(viewController);
						viewController.setViewStage(this);
						Parent loadedNode;
						try {
							viewController.setIntent(intent);
							loadedNode = loader.load();
						} catch (IOException e) {
							throw new RuntimeException(e);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						if(loadedNode != null) {
							loadedNode.getStylesheets().clear();
							loadedNode.setCache(true);
							loadedNode.setCacheHint(CacheHint.SPEED);
							loadedNode.setVisible(true);
							loadedNode.setDisable(false);
							loadedNode.requestFocus();
							setViewLinks(viewController);
							viewCache.put(intent.getViewClass().getName(), viewController);
						} else {
							viewController.setIntent(null);
						}
					} else {
						throw new RuntimeException("Could not load \""+viewFilePath+"\"");
					}
				}
			} else {
				viewController.setIntent(intent);
			}
			if(intent.getCallerContext().getIntent() != null) {
				intent.getCallerContext().getIntent().clearExtra();
			}
			stageContext.setViewStageTitle(viewController.getViewTitle());
			stageContext.swapContent(viewController);
			viewController.onStart();
		} else {
			throw new RuntimeException("ViewStage is not initialized");
		}
	}
	
	public void unload(Class<? extends ViewContext> viewClass)
	{
		if(viewCache.containsKey(viewClass.getName())) {
			viewCache.remove(viewClass.getName());
		}
	}
	
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
				} catch (IllegalArgumentException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				
				if(region != null) {
					region.setOnMouseClicked((clickEvent) -> {
						load(new Intent(context, viewLinkAnnotation.value()));
					});
				}
			}
		}
	}
	
	private URL getResourceURL(String path)
	{
		if(!path.startsWith("/")) path = "/"+path;
		return getClass().getResource(path);
	}
}
