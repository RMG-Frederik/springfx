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
package net.codecrafting.springfx.animation;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.ParallelTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationNode 
{
	private ParallelTransition transition;
	private EventHandler<ActionEvent> event;
	
	public AnimationNode(ParallelTransition transition)
	{
		super();
		this.transition = transition;
	}
	
	public void play()
	{
		transition.playFromStart();
	}
	
	public void stop()
	{
		transition.stop();
	}
	
	public void continuePlay()
	{
		transition.play();
	}
	
	public void pause()
	{
		transition.pause();
	}
	
	public Status getStatus()
	{
		return transition.getStatus();
	}
	
	public AnimationNode onFinished(EventHandler<ActionEvent> event)
	{
		this.event = event;
		transition.setOnFinished(this.event);
		return this;
	}
	
	public Duration getCurrentTime()
	{
		return transition.getCurrentTime();
	}
	
	public Node getContent() 
	{
		return transition.getNode();
	}
	
	public AnimationNode setDuration(Duration duration)
	{
		Double rate = 1.0;
		Duration curDuration = transition.getTotalDuration();
		if(duration.toMillis() > 0) {
			rate = curDuration.toMillis()/duration.toMillis();
		}
		if(transition.getStatus() == Status.RUNNING) {
			Duration curTime = transition.getCurrentTime();
			transition.stop();
			ObservableList<Animation> transitions = transition.getChildren();
			for (Animation animation : transitions) {
				animation.setRate(rate);
			}
			transition.playFrom(curTime);
		} else {
			ObservableList<Animation> transitions = transition.getChildren();
			for (Animation animation : transitions) {
				animation.setRate(rate);
			}
		}
		return this;
	}
	
	public Duration getDuration()
	{
		return transition.getTotalDuration();
	}
	
	public ParallelTransition getTransition() 
	{
		return transition;
	}
	
	public AnimationNode setTransition(ParallelTransition transition) 
	{
		if(transition.getStatus() == Status.STOPPED) {
			this.transition = transition;
		}
		return this;
	}
}
