package kopacz.diabetool;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;

/**
 * Created by Kopac on 18.05.2016.
 */
class HelperClass {
    public static String TAG = HelperClass.class.getSimpleName();

    public static String stripHTML(String html)
    {
//        html = html.replaceAll("<(.*?)>", " ");//Removes all items in brackets
//        html = html.replaceAll("<(.*?)\n", " ");//Must be underneath
//        html = html.replaceFirst("(.*?)>", " ");//Removes any connected item to the last bracket
//        html = html.replaceAll("&nbsp;", " ");
//        html = html.replaceAll("&amp;", " ");
//        return html;
        return Html.fromHtml(html).toString();
    }
    public static final String TAG_START = "[intron]";
    public static final String TAG_END = "[ekson]";

    public static String unwrapJSONfromInterfaceSrc (String sitesource)
    {
        //Log.v("tague", "jest prośba");
        return sitesource.substring(sitesource.indexOf(TAG_START) + TAG_START.length(), sitesource.indexOf(TAG_END));
    }
    public String concat(Resources resources, int resource, Bundle bundle, String key)
    {
        //Log.v(TAG, "" + bundle.getFloat(key, 0) + " float is being transformed to: " + String.valueOf(bundle.getFloat(key, 0)));
        return resources.getString(resource) + ": " + String.valueOf(bundle.getFloat(key, 0));
    }
    public static Float proportionDefault(Float input, Float remainder)
    {
        if (input == -1) //imitacja nulla
        {
            return input;
        }
        else
        {
            return (input * remainder) / 100f;
        }
    }
    public static Float proportionNullAware(Float input, Float is, Float shouldBe)
    {
        if (input == -1) //imitacja nulla
        {
            return input;
        }
        else
        {
            return (input * shouldBe) / is;
        }
    }
    public static String appendColon (String string)
    {
        return string + ": ";
    }
}
