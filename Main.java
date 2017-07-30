package gaussian;

import java.awt.Color;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Main {
	private static BufferedImage img;
	
	public static void main(String args[])
	{
		readImage();
		blur(10, 49);
		writeImage();
	}
	
	public static void readImage()
	{
            Main.setImg(null);
		try {
			setImg(ImageIO.read(new File("C:\\Users\\Aditya\\Pictures\\GaussianNoise.jpg")));
		} catch (IOException e) {
		}
	}
	
	public static void blur(double sigma, int kernelsize)
	{
		double[] kernel = createKernel(sigma, kernelsize);
		for(int i = 0; i < getImg().getWidth(); i++)
		{
			for(int j = 0; j < getImg().getHeight(); j++)
			{
				double overflow = 0;
				int counter = 0;
				int kernelhalf = (kernelsize - 1)/2;
				double red = 0;
				double green = 0;
				double blue = 0;
				for(int k = i - kernelhalf; k < i + kernelhalf; k++)
				{
					for(int l = j - kernelhalf; l < j + kernelhalf; l++)
					{
						if(k < 0 || k >= getImg().getWidth() || l < 0 || l >= getImg().getHeight())
						{
							counter++;
							overflow += kernel[counter];
							continue;
						}
						
						Color c = new Color(getImg().getRGB(k, l));
						red += c.getRed() * kernel[counter];
						green += c.getGreen() * kernel[counter];
						blue += c.getBlue() * kernel[counter];
						counter++;
					}
					counter++;
				}
				
				if(overflow > 0)
				{
					red = 0;
					green = 0;
					blue = 0;
					counter = 0;
					for(int k = i - kernelhalf; k < i + kernelhalf; k++)
					{
						for(int l = j - kernelhalf; l < j + kernelhalf; l++)
						{
							if(k < 0 || k >= getImg().getWidth() || l < 0 || l >= getImg().getHeight())
							{
								counter++;
								continue;
							}
							
							Color c = new Color(getImg().getRGB(k, l));
							red += c.getRed() * kernel[counter]*(1/(1-overflow));
							green += c.getGreen() * kernel[counter]*(1/(1-overflow));
							blue += c.getBlue() * kernel[counter]*(1/(1-overflow));
							counter++;
						}
						counter++;
					}
                                }
                                
				Main.getImg().setRGB(i, j, new Color((int)red, (int)green, (int)blue).getRGB());
			}
		}
	}
	
	public static double[] createKernel(double sigma, int kernelsize)
	{
		double[] kernel = new double[kernelsize*kernelsize];
		for(int i = 0;  i < kernelsize; i++)
		{
			double x = i - (kernelsize -1) / 2;
			for(int j = 0; j < kernelsize; j++)
			{
				double y = j - (kernelsize -1)/2;
				kernel[j + i*kernelsize] = 1 / (2 * Math.PI * sigma * sigma) * Math.exp(-(x*x + y*y) / (2 * sigma *sigma));
			}
		}
		float sum = 0;
		for(int i = 0; i < kernelsize; i++)
		{
			for(int j = 0; j < kernelsize; j++)
			{
				sum += kernel[j + i*kernelsize];
			}
		}
		for(int i = 0; i < kernelsize; i++)
		{
			for(int j = 0; j < kernelsize; j++)
			{
				kernel[j + i*kernelsize] /= sum;
			}
		}
		return kernel;
	}
	
	public static void lineareAbbildung(double a, double b)
	{
		for(int x = 0; x < getImg().getWidth(); x++)
		{
                    for(int y = 0; y < getImg().getHeight(); y++)
                    {
				int rgb = (int) (a*Main.getImg().getRGB(x, y) + b);
				Main.getImg().setRGB(x, y, rgb);
			}
		}
	}
	
	public static void blackAndWhite()
	{
		for(int x = 0; x < getImg().getWidth(); x++)
		{
			for(int y = 0; y < getImg().getHeight(); y++)
			{
				int rgb = Main.getImg().getRGB(x, y);
				Color c = new Color(rgb);
                                int grey = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114*c.getBlue());
				Color c2 = new Color(grey, grey, grey);
				Main.getImg().setRGB(x, y, c2.getRGB());
			}
		}
	}
	
	public static void improveGrey()
	{
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int x = 0; x < getImg().getWidth(); x++)
		{
			for(int y = 0; y < getImg().getHeight(); y++)
			{
				int rgb = Main.getImg().getRGB(x, y);
				if(rgb > max)
				{
					max = rgb;
				}
				if(rgb < min)
				{
					min = rgb; 
				}
			}
		}
		lineareAbbildung(255 / (max - min), - 255* min / (max -min));
	}
	
	public static void writeImage()
	{
		File output = new File("C:\\Users\\Aditya\\Pictures\\GaussianNoiseChanged.jpg");
		try {
			ImageIO.write(getImg(), "jpg", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
     * @return the img
     */
    public static BufferedImage getImg() {
        return img;
    }

    /**
     * @param aImg the img to set
     */
    public static void setImg(BufferedImage aImg) {
        img = aImg;
    }
}
