package kopacz.diabetool;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import NullAware.NullAwareFloat;


class Calculator implements UnifiedCalculator{
    public float ww(float carbohydrates)
    {
        NullAwareFloat nullAwareFloat = new NullAwareFloat(carbohydrates);
        nullAwareFloat.divideBy(10);
        return nullAwareFloat.getValue();
    }
    public float wbt(float proteins, float fat)
    {
        NullAwareFloat nullAwareProteins = new NullAwareFloat(proteins);
        NullAwareFloat nullAwareFat = new NullAwareFloat(fat);
        nullAwareProteins.multiplyBy(9);
        nullAwareFat.multiplyBy(4);
        nullAwareProteins.add(nullAwareFat.getValue());
        nullAwareProteins.divideBy(100);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        decimalFormat.setRoundingMode(RoundingMode.UP);

        //String temp = decimalFormat.format((fat*9 + proteins*4)/100);
        String temp = decimalFormat.format(nullAwareProteins.getValue());
        return Float.parseFloat(temp);
    }
//    1 g węglowodanów = 4 kcal
//    1g białek = 4 kcal
//    1g tłuszczów = 9 kcal
//    1 WW (wymiennik węglowodanowy) = 10 g węglowodanów zawartych w produkcie.
//    1 WBT (wymiennik białkowo-tłuszczowy) = 100 kcal z białek i tłuszczów w danym produkcie

//    Na opakowaniu czekolady mamy opis:
//            100 g zawiera: białka- 7,6 g, węglowodany- 53,3 g, tłuszcze 27,9 g
//    czyli: 53,3g węglowodanów = 5,3 WW

//    Jak obliczyć WBT
//    Obliczanie kcal tłuszczy: 9 * 27,9g = 251,1 kcal
//    Obliczanie kcal białek: 4 * 7,6 g = 30,4 kcal
//    Dodajemy łącznie kalorie uzyskane z białek i tłuszczów- 251,1+30,4= 281,5 kcal
//    Uzyskany wynik dzielimy przez sto i zaokrąglamy, czyli 281,5 : 100 = 2,8

}
