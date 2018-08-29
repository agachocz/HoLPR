import java.io.Serializable;

public class Authority implements Serializable {

	private String name;
	private String title;
	private String imgS;
	
	public Authority(String name, String title, String imgS)
	{
		this.name = name;
		this.title = title;
		this.imgS = imgS;
	}
	
	public String getName(){return name;}
	public String getTitle(){return title;}
	public String getImgS(){return imgS;}
	
}
