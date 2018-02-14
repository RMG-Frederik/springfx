package net.codecrafting.springfx.animation;

import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.util.Duration;

public final class SkewTransition extends Transition 
{
    /**
     * The target node of this {@code Transition}.
     * <p>
     * It is not possible to change the target {@code node} of a running
     * {@code SkewTransition}. If the value of {@code node} is changed for a
     * running {@code SkewTransition}, the animation has to be stopped and
     * started again to pick up the new value.
     */
    private ObjectProperty<Node> node;
    private static final Node DEFAULT_NODE = null;
    private Node cachedNode;
    
    /**
     * The duration of this {@code SkewTransition}.
     * <p>
     * It is not possible to change the {@code duration} of a running
     * {@code SkewTransition}. If the value of {@code duration} is changed for a
     * running {@code SkewTransition}, the animation has to be stopped and
     * started again to pick up the new value.
     * <p>
     * Note: While the unit of {@code duration} is a millisecond, the
     * granularity depends on the underlying operating system and will in
     * general be larger. For example animations on desktop systems usually run
     * with a maximum of 60fps which gives a granularity of ~17 ms.
     *
     * Setting duration to value lower than {@link Duration#ZERO} will result
     * in {@link IllegalArgumentException}.
     *
     * @defaultValue 400ms
     */
    private ObjectProperty<Duration> duration;
    private static final Duration DEFAULT_DURATION = Duration.millis(400);
    
    /**
     * Specifies the start x angle value for this {@code SkewTransition}.
     * <p>
     * It is not possible to change {@code fromXAngle} of a running
     * {@code SkewTransition}. If the value of {@code fromAngle} is changed
     * for a running {@code SkewTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty fromXAngle;
    private static final double DEFAULT_FROM_XANGLE = Double.NaN;
    
    /**
     * Specifies the start y angle value for this {@code SkewTransition}.
     * <p>
     * It is not possible to change {@code fromYAngle} of a running
     * {@code SkewTransition}. If the value of {@code Angle} is changed
     * for a running {@code SkewTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty fromYAngle;
    private static final double DEFAULT_FROM_YANGLE = Double.NaN;
    
    /**
     * Specifies the stop x angle value for this {@code SkewTransition}.
     * <p>
     * It is not possible to change {@code toXAngle} of a running
     * {@code SkewTransition}. If the value of {@code toXAngle} is changed
     * for a running {@code SkewTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty toXAngle;
    private static final double DEFAULT_TO_XANGLE = Double.NaN;
    
    /**
     * Specifies the stop y angle value for this {@code SkewTransition}.
     * <p>
     * It is not possible to change {@code toYAngle} of a running
     * {@code SkewTransition}. If the value of {@code toYAngle} is changed
     * for a running {@code SkewTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty toYAngle;
    private static final double DEFAULT_TO_YANGLE = Double.NaN;
	
	public SkewTransition()
	{
		this(DEFAULT_DURATION, null);
	}
	
	public SkewTransition(Duration duration)
	{
		this(duration, null);
	}
    
	public SkewTransition(Duration duration, Node node)
	{
        setDuration(duration);
        setNode(node);
        setCycleDuration(duration);		
	}
	
    public final void setNode(Node value) 
    {
        if ((node != null) || (value != null /* DEFAULT_NODE */)) {
            nodeProperty().set(value);
        }
    }

    public final Node getNode() 
    {
        return (node == null)? DEFAULT_NODE : node.get();
    }

    public final ObjectProperty<Node> nodeProperty() 
    {
        if (node == null) {
            node = new SimpleObjectProperty<Node>(this, "node", DEFAULT_NODE);
        }
        return node;
    }
    
    public final void setDuration(Duration value) 
    {
        if ((duration != null) || (!DEFAULT_DURATION.equals(value))) {
            durationProperty().set(value);
        }
    }

    public final Duration getDuration() 
    {
        return (duration == null)? DEFAULT_DURATION : duration.get();
    }
	
    public final ObjectProperty<Duration> durationProperty() 
    {
        if (duration == null) {
            duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) {

                @Override
                public void invalidated() {
                    try {
                        setCycleDuration(getDuration());
                    } catch (IllegalArgumentException e) {
                        if (isBound()) {
                            unbind();
                        }
                        set(getCycleDuration());
                        throw e;
                    }
                }

                @Override
                public Object getBean() {
                    return SkewTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }
        return duration;
    }
    
    public final void setToXAngle(double value) 
    {
        if ((toXAngle != null) || (!Double.isNaN(value))) {
            toXAngleProperty().set(value);
        }
    }

    public final double getToXAngle() 
    {
        return (toXAngle == null)? DEFAULT_TO_XANGLE : toXAngle.get();
    }

    public final DoubleProperty toXAngleProperty() 
    {
        if (toXAngle == null) {
            toXAngle = new SimpleDoubleProperty(this, "toXAngle", DEFAULT_TO_XANGLE);
        }
        return toXAngle;
    }
    
    public final void setFromXAngle(double value) 
    {
        if ((fromXAngle != null) || (!Double.isNaN(value))) {
        	fromXAngleProperty().set(value);
        }
    }

    public final double getFromXAngle() 
    {
        return (fromXAngle == null)? DEFAULT_FROM_XANGLE : fromXAngle.get();
    }

    public final DoubleProperty fromXAngleProperty() 
    {
        if (fromXAngle == null) {
            fromXAngle = new SimpleDoubleProperty(this, "fromXAngle", DEFAULT_FROM_XANGLE);
        }
        return fromXAngle;
    }
   
    public final double getToYAngle() 
    {
        return (toYAngle == null)? DEFAULT_TO_YANGLE : toYAngle.get();
    }
    
    public final void setToYAngle(double value) 
    {
        if ((toYAngle != null) || (!Double.isNaN(value))) {
        	toYAngleProperty().set(value);
        }
    }

    public final DoubleProperty toYAngleProperty() 
    {
        if (toYAngle == null) {
            toYAngle = new SimpleDoubleProperty(this, "toYAngle", DEFAULT_TO_YANGLE);
        }
        return toYAngle;
    }
    
    public final void setFromYAngle(double value) 
    {
        if ((fromYAngle != null) || (!Double.isNaN(value))) {
        	fromYAngleProperty().set(value);
        }
    }

    public final double getFromYAngle() 
    {
        return (fromYAngle == null)? DEFAULT_FROM_YANGLE : fromYAngle.get();
    }

    public final DoubleProperty fromYAngleProperty() 
    {
        if (fromYAngle == null) {
            fromYAngle = new SimpleDoubleProperty(this, "fromYAngle", DEFAULT_FROM_YANGLE);
        }
        return fromYAngle;
    }
	
    /**
     * Make skew transformation based on MatrixTransformation.
     * The origin will is on the center
     * 
     * @param ax x angle (in degrees) of the MatrixTransformation {@code Double.NaN}
     * @param ay y angle (in degrees) of the MatrixTransformation {@code Double.NaN}
     */
	protected void skew(double ax, double ay)
	{
		double tanAX = Math.tan(Math.toRadians(ax));
		double tanAY = Math.tan(Math.toRadians(ay));
		cachedNode = getTargetNode();
		final Affine aff = new Affine(1, tanAX, -tanAY*cachedNode.getLayoutBounds().getHeight()/2,  tanAY, 1.0, -tanAX*cachedNode.getLayoutBounds().getWidth()/2);
		cachedNode.getTransforms().clear(); 
		cachedNode.getTransforms().add(aff);
	}
    
    private Node getTargetNode() 
    {
        final Node node = getNode();
        return (node != null) ? node : getParentTargetNode();
    }	
	
    /**
     * {@inheritDoc}
     */
	@Override
	protected void interpolate(double frac) 
	{
        double _fromXAngle = getFromXAngle();
        double _toXAngle = getToXAngle();
        double _fromYAngle = getFromYAngle();
        double _toYAngle = getToYAngle();	
        double startX = (!Double.isNaN(_fromXAngle)) ? _fromXAngle : 0;
        double startY = (!Double.isNaN(_fromYAngle)) ? _fromYAngle : 0;
        double deltaX = (!Double.isNaN(_toXAngle)) ? _toXAngle - startX : 0;
        double deltaY = (!Double.isNaN(_toYAngle)) ? _toYAngle - startY : 0;
        skew(startX + frac * deltaX, startY + frac * deltaY);
	}
}
