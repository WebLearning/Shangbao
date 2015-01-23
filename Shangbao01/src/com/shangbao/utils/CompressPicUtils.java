package com.shangbao.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;    
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.stereotype.Service;

@Service
public class CompressPicUtils {
	
	public boolean compressPic(File inputFile,// 文件对象
							   File outputFile,// 输出图路径
							   int outputWidth,// 默认输出图片宽
							   int outputHeight,// 默认输出图片高
							   boolean tag){// 是否等比缩放标记
		if(!inputFile.exists()){
			return false;
		}
		Image img;
		try {
			img = ImageIO.read(inputFile);
			if(img.getWidth(null) == -1){
				return false;
			}else {
				int newWidth;
				int newHeight;
				if(tag){
					if(outputHeight == 0){
						double rate = ((double) img.getWidth(null)) / (double) outputWidth + 0.01;
						newWidth = (int) (img.getWidth(null) / rate);
						newHeight = (int) (img.getHeight(null) / rate);
					}else if(outputWidth == 0){
						double rate = ((double) img.getHeight(null)) / (double) outputHeight + 0.01;
						newWidth = (int) (img.getWidth(null) / rate);
						newHeight = (int) (img.getHeight(null) / rate);
					}else{
						double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.01;
						double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.01;
						double rate = rate1 > rate2 ? rate1 : rate2;
						newWidth = (int) (img.getWidth(null) / rate);
						newHeight = (int) (img.getHeight(null) / rate);
					}
				}else{
					newWidth = outputWidth; // 输出的图片宽度
					newHeight = outputHeight; // 输出的图片高度
				}
				BufferedImage buffImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
				buffImage.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = new FileOutputStream(outputFile);
				// JPEGImageEncoder可适用于其他图片类型的转换
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(buffImage);
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean compressByThumbnailator(File inputFile,// 文件对象
										   File outputFile,// 输出图路径
										   int outputWidth,// 默认输出图片宽
										   int outputHeight,// 默认输出图片高
										   double outputQutity,
										   boolean tag){
		if(!inputFile.exists()){
			return false;
		}
		Image img;
		try {
			img = ImageIO.read(inputFile);
			if(img.getWidth(null) == -1){
				return false;
			}
			int newWidth = 0;
			int newHeight = 0;
			if(tag){
				if(outputHeight == 0){
					double rate = ((double) img.getWidth(null)) / (double) outputWidth + 0.01;
					newWidth = (int) (img.getWidth(null) / rate);
					newHeight = (int) (img.getHeight(null) / rate);
				}else if(outputWidth == 0){
					double rate = ((double) img.getHeight(null)) / (double) outputHeight + 0.01;
					newWidth = (int) (img.getWidth(null) / rate);
					newHeight = (int) (img.getHeight(null) / rate);
				}else{
					double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.01;
					double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.01;
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (img.getWidth(null) / rate);
					newHeight = (int) (img.getHeight(null) / rate);
				}
				Thumbnails.of(inputFile)
				  .size(newWidth, newHeight)
				  .outputQuality(outputQutity)
				  .toFile(outputFile);
				return true;
			}else{
				Thumbnails.of(inputFile)
						  .size(newWidth, newHeight)
						  .outputQuality(outputQutity)
						  .toFile(outputFile);
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
