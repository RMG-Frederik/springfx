package net.codecrafting.springfx.controls;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public abstract class StageContext extends ViewContext
{	
	public abstract void setViewStageTitle(String title);
	
	@Override
	public abstract AnchorPane getMainNode();
	
	public void swapContent(final ViewContext viewController)
	{
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
			throw new NullPointerException("StageContext mainNode and content must exist");
		}
	}
}
