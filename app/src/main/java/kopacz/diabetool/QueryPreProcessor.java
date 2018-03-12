package kopacz.diabetool;

import android.util.Log;

/**
 * Created by Kopac on 10.01.2017.
 */

public class QueryPreProcessor {
    int lenght;
    String[] original;
    StringBuilder sb = new StringBuilder();

    public QueryPreProcessor (int wordLenght)
    {
        this.lenght = wordLenght;
    }

    public void preProcess (String stringWithSpaces)
    {
        //free stringbuilder cache
        sb.setLength(0);

        original = stringWithSpaces.split(" ");
    }

    public String getLikesQuery(String like)
    {
        String[] split = new String[original.length];

        Log.v("preprocek", "" + original.length);
        if (original[0].length() == 0)
        {
            sb.append(like);
            sb.append(" LIKE '%");
            sb.append("%'");
        }
        else {
            for (int i = 0; i < original.length; i++)
            {
                if (i != 0)
                {
                    sb.append(" OR ");
                }

                if (original[i].length() > lenght) //odpowiednio długi string
                {
                    split[i] = original[i].substring(0, lenght);
                    sb.append(like);
                }
                else {
                    split[i] = original[i];
                    sb.append(like);
                }

                sb.append(" LIKE '%");
                sb.append(split[i]);
                sb.append("%'");
            }
        }

        return sb.toString();
    }

    public String getLikesQueryIncludingBarcode(String like, String barcodeColumnName)
    {
        String[] split = new String[original.length];

        Log.v("preprocek", "" + original.length);
        if (original[0].length() == 0)
        {
            sb.append(like);
            sb.append(" LIKE '%");
            sb.append("%'");
        }
        else {
            for (int i = 0; i < original.length; i++)
            {
                if (i != 0)
                {
                    sb.append(" OR ");
                }

                if (original[i].length() == 13) //ean 13 czyli europejskie kody kreskowe
                {
                    sb.append(barcodeColumnName);
                    split[i] = original[i];
                }
                else if (original[i].length() > lenght) //odpowiednio długi string
                {
                    split[i] = original[i].substring(0, lenght);
                    sb.append(like);
                }
                else {
                    split[i] = original[i];
                    sb.append(like);

                }

                sb.append(" LIKE '%");
                sb.append(split[i]);
                sb.append("%'");
            }
        }

        return sb.toString();
    }
}
