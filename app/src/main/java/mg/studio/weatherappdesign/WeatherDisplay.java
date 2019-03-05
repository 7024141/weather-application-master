package mg.studio.weatherappdesign;

import android.widget.ImageView;
import android.widget.TextView;

public class WeatherDisplay {
    private ImageView imgView;
    private TextView texView;

    public ImageView getImgView(){ return imgView;}
    public TextView getTexView(){return texView;}
    public void setImgView(ImageView imgView){this.imgView = imgView;}
    public void setTexView(TextView texView){this.texView = texView;}
}
