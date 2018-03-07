package net.codecrafting.springfx.util;

import javafx.scene.image.Image;

public class MipmapLevel
{
	private int level;
	private Image image;
	
	public MipmapLevel(int level, Image image)
	{
		this.level = level;
		this.image = image;
	}

	public int getLevel() 
	{
		return level;
	}

	public void setLevel(int level) 
	{
		this.level = level;
	}

	public Image getImage() 
	{
		return image;
	}

	public void setImage(Image image) 
	{
		this.image = image;
	}

	@Override
	public String toString() 
	{
		return "MipmapLevel [level=" + level + ", image=" + image + "]";
	}
}
