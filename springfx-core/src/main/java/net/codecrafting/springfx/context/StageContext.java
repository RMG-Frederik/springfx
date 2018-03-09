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

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import net.codecrafting.springfx.annotation.ViewController;

/**
 * This class is used to abstract the root JavaFX controller. The goal of {@link StageContext} is
 * to provide a standard abstraction of the root JavaFX controller for the {@link ViewStage}. This class
 * is also responsible for swapping the main node.
 * 
 * @author Lucas Marotta
 * @see #setViewStageTitle(String)
 * @see #getMainNode()
 * @see #swapContent(ViewContext)
 */
public abstract class StageContext extends ViewContext
{	
	/**
	 * Create a new instance of a abstraction of {@link StageContext}.
	 * Use this constructor to set {@link #viewName} and {@link #viewTitle} by
	 * the controller name or via {@link ViewController} annotation
	 */
	public StageContext()
	{
		super();
	}
	
	/**
	 * Create a new instance of a abstraction of {@link StageContext}.
	 * This constructor allow manual setup for viewName and viewTitle.
	 * @param viewName the name of the view that will be used for find a corresponding FXML file
	 * @param viewTitle the name that can be use for a title of this view context
	 */
	public StageContext(String viewName, String viewTitle)
	{
		super(viewName, viewTitle);
	}
	
	/**
	 * Method that can perform the configuration of the {@link ViewContext#viewTitle} to the interface
	 * The {@link ViewStage} will call this method passing the {@link ViewContext#viewTitle} from the
	 * loaded {@link ViewContext} {@link Intent}. Use this to set a title for your current view.
	 * @param title the {@link ViewContext#viewTitle} to be set as title on the interface.
	 */
	public abstract void setViewStageTitle(String title);
	
	/**
	 * Get the root main {@link AnchorPane} node for this view context. The main node for the {@link StageContext}
	 * needs to be a {@link AnchorPane} in order to perform a correct layout positioning for internal nodes.
	 * Your {@link Scene} root node node don't need to be this main node, but you need to put a {@link AnchorPane} 
	 * inside that node. Need to be implemented.
	 * @return the root main {@link AnchorPane} node of this stage context.
	 */
	@Override
	public abstract AnchorPane getMainNode();
	
	/**
	 * Swap the stage context main node first child (if present) to the new {@link ViewContext} loaded main node.
	 * This method will call for {@link ViewContext#swapAnimation(Node)} to perform the node animation.
	 * @param viewController the {@link ViewContext} containing the main node to be swapped.
	 * @throws IllegalArgumentException if the viewController is null
	 * @throws NullPointerException if the {@link StageContext} and {@link ViewContext} mainNode is null
	 */
	public void swapContent(final ViewContext viewController)
	{
		if(viewController != null) {
			final AnchorPane stageContextNode = getMainNode();
			final Node viewNode = viewController.getMainNode();
			if(stageContextNode != null && viewNode != null) {
				viewNode.setVisible(true);
				viewNode.autosize();
				if(stageContextNode.getChildren().size() > 0) stageContextNode.getChildren().remove(0);
				stageContextNode.getChildren().add(0, viewNode);
				AnchorPane.setTopAnchor(viewNode, 0.0);
				AnchorPane.setBottomAnchor(viewNode, 0.0);
				AnchorPane.setLeftAnchor(viewNode, 0.0);
				AnchorPane.setRightAnchor(viewNode, 0.0);
				viewController.swapAnimation(viewNode);
			} else {
				throw new NullPointerException("StageContext mainNode and content must not be null");
			}	
		} else {
			throw new IllegalArgumentException("viewController must not be null");
		}
	}
}
