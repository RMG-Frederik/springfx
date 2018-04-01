/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2018 The SpringFX Contributors
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
package net.codecrafting.springfx.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javafx.scene.image.Image;

public class Mipmap 
{
	private String path;
	private String name;
	private String extension;
	private List<MipmapLevel> mapList;
	private static final Log LOGGER = LogFactory.getLog(Mipmap.class);
	
	public Mipmap(String mipmapBaseFile, int[] levels)
	{
		init(mipmapBaseFile);
		loadLevels(levels);
	}

	public String getPath() 
	{
		return path;
	}

	public String getName() 
	{
		return name;
	}

	public String getExtension() 
	{
		return extension;
	}

	public List<MipmapLevel> getAllLevels()
	{
		return mapList;
	}
	
	public Image getImage(int level) 
	{
		int inf = 0, sup = mapList.size()-1, middle;
		if(sup > 0) {
			while(inf <= sup) {
				middle = (inf+sup)/2;
				MipmapLevel mipmapLevel = mapList.get(middle);
				if(level == mipmapLevel.getLevel()) {
					return mipmapLevel.getImage();
				}
				if(level < mipmapLevel.getLevel()) {
					inf = middle + 1;
				} else {
					sup = middle - 1;
				}
			}			
		}
		return null;
	}

	private void init(String mipmapBaseFile)
	{
		mapList = new ArrayList<MipmapLevel>();
		path = FilenameUtils.getFullPathNoEndSeparator(mipmapBaseFile).replace("\\", "/");
		name = FilenameUtils.getBaseName(mipmapBaseFile);
		extension = "."+FilenameUtils.getExtension(mipmapBaseFile);
	}
	
	private void loadLevels(int[] levels)
	{
		Arrays.sort(levels);
		reverse(levels);
		Image tempImage = null;
		try {
			tempImage = new Image(path+"/"+name+extension);
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		if(tempImage != null) {
			for (int level : levels) 
			{
				String imagePath;
				if(level == 1) imagePath = path+"/"+name+extension;
				else imagePath = path+"/"+name+"@"+level+"x"+extension;
				try {
					mapList.add(new MipmapLevel(level, 
							new Image(imagePath, tempImage.getWidth()*level, tempImage.getHeight()*level, true, true)));
				} catch(Exception e) {
					//LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}
	
	private void reverse(final int[] array)
	{
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
	}
}
